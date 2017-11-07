package ru.za.services.upwork;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import ru.za.services.upwork.parser.ParserSettings;
import ru.za.services.upwork.parser.UserSettings;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

@Path("/Service")
public class TestRESTfulService {


    private static final Logger LOG = LogManager.getLogger(TestRESTfulService.class.getName());

    public static TestApplication application = TestApplication.getInstance();

    private BackgroundJobManager manager = new BackgroundJobManager();

    @Path("{param}")
    @GET
    @Produces("application/xml")
    public String getTest_XML(@PathParam("param") String param1){
        param1 = "test";
        return String.format("<xml><hello>Hello world</hello><param>%s</param></xml>", param1);
    }


    @Path("getStatus")
    @GET
    @Produces("application/xml")
    public String getStatus(){
        LOG.info("getStatus command");
        if (application == null){
            LOG.info("application is null");
        }
        switch (application.getStatus()){
            case ACTIVE:
                return String.format("<xml>ACTIVE</xml>");
            case NOT_ACTIVE:
                return String.format("<xml>NOT ACTIVE</xml>");
            default:
                return String.format("<xml>UNKNOWN</xml>");
        }
    }

    @Path("changeStatus")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public String setStatus(@FormParam("action") String action){

        if (action.toUpperCase().equals("START")){
            application.start();
        } else if (action.toUpperCase().equals("STOP")){
            application.stop();
        } else {
            return String.format("<xml><error>Unknown action: %s</error></xml>", action);
        }
        return String.format("<xml><status>%s</status></xml>", application.getStatus());
    }

    @Path("addKeyword")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String addKeyword(@FormParam("email") String email, @FormParam("keyword") String keyword ){
        ParserSettings settings = ParserSettings.getInstance();
        UserSettings user = null;
        for (int i = 0; i < settings.getUsersSettings().size(); i++){
            if (settings.getUsersSettings().get(i).getEmail().equals(email)){
                user = settings.getUsersSettings().get(i);
                break;
            }
        }
        if (user == null){
            user = new UserSettings();
            user.setEmail(email);
            settings.getUsersSettings().add(user);
        }
        user.addKeyword(keyword);
        JSONArray jsonArray = new JSONArray(user.getNeededWords());
        return jsonArray.toString();
    }

    @Path("addFeedURL")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String addFeedUrl(@FormParam("email") String email, @FormParam("url") String url){
        ParserSettings settings = ParserSettings.getInstance();
        UserSettings user = null;
        for (int i = 0; i < settings.getUsersSettings().size(); i++){
            if (settings.getUsersSettings().get(i).getEmail().equals(email)){
                user = settings.getUsersSettings().get(i);
                break;
            }
        }
        if (user == null){
            user = new UserSettings();
            user.setEmail(email);
            settings.getUsersSettings().add(user);
        }
        user.addFeedUrl(url);
        JSONArray jsonArray = new JSONArray(user.getRssUrls());
        return jsonArray.toString();
    }

    @Path("getSettings")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getSettings(){
        ParserSettings settings = ParserSettings.getInstance();
        JSONArray jsonArray = new JSONArray();
        for (UserSettings userSettings: settings.getUsersSettings()) {
            jsonArray.put(new JSONObject(userSettings.toString()));
        }
        String xml = XML.toString(jsonArray);
        return "<xml>" + xml + "</xml>";
    }

    @Path("removeEmail")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String removeEmail(@QueryParam("email") String email){
        ParserSettings settings = ParserSettings.getInstance();
        for (UserSettings userSettings: settings.getUsersSettings()) {
            if (userSettings.getEmail().equals(email)){
                settings.getUsersSettings().remove(userSettings);
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (UserSettings userSettings: settings.getUsersSettings()) {
            jsonArray.put(new JSONObject(userSettings.toString()));
        }
        String xml = XML.toString(jsonArray);
        return "<xml>" + xml + "</xml>";
    }



}
