package ru.za.services.upwork;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import ru.za.services.upwork.parser.ParserSettings;


public class BackgroundJobManager implements ApplicationEventListener {
    private final Logger logger = LogManager.getLogger(TestRESTfulService.class.getName());
    private TestApplication application = TestApplication.getInstance();

    public void onEvent(ApplicationEvent applicationEvent) {
        //logger.info(applicationEvent.getType().toString());
        if (applicationEvent.getType() == ApplicationEvent.Type.DESTROY_FINISHED){
            logger.info("Application destroy process event rising");
            ParserSettings settings = ParserSettings.getInstance();
            logger.info("Write config");
            settings.writeConfig();
            logger.info("Stop task executing");
            application.stop();
        }

    }

    public RequestEventListener onRequest(RequestEvent requestEvent) {
        //logger.info(requestEvent.toString());
        return null;
    }
}
