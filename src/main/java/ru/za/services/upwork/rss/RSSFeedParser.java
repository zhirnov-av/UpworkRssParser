package ru.za.services.upwork.rss;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.*;

public class RSSFeedParser {
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";

    private CloseableHttpClient client;
    private CloseableHttpResponse response;

    Logger logger = LogManager.getLogger(this.getClass().getName());

    ByteArrayOutputStream stream;
    byte[] feedData;

    public RSSFeedParser(String feedUrl) {



        this.client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(feedUrl);
        // for proxy
        HttpHost proxy = new HttpHost("10.2.3.10", 8080);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        httpGet.setConfig(config);
        // *********



        this.response = null;
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            stream = new ByteArrayOutputStream();
            entity.writeTo(stream);
            EntityUtils.consume(entity);
        }catch(Exception e){
            logger.error(e.getMessage());
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }

    }

    public RSSFeedParser(File xmlFile) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream( xmlFile );
            stream = new ByteArrayOutputStream();
            int i;
            while ((i = fis.read()) > 0) {
                stream.write(i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public Feed readFeed() throws Exception {
        Feed feed = null;
        try {
            boolean isFeedHeader = true;
            // Set header values intial to the empty string
            String description = "";
            String title = "";
            String link = "";
            String language = "";
            String copyright = "";
            String author = "";
            String pubdate = "";
            String guid = "";

            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            XMLEvent event;
            while (eventReader.hasNext()) {
                event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();
                    switch (localPart) {
                        case ITEM:
                            if (isFeedHeader) {
                                isFeedHeader = false;
                                feed = new Feed(title, link, description, language,
                                        copyright, pubdate);
                            }
                            //event = eventReader.nextEvent();
                            break;
                        case TITLE:
                            title = getCharacterData(event, eventReader);
                            break;
                        case DESCRIPTION:
                            description = getCharacterData(event, eventReader);
                            break;
                        case LINK:
                            link = getCharacterData(event, eventReader);
                            break;
                        case GUID:
                            guid = getCharacterData(event, eventReader);
                            break;
                        case LANGUAGE:
                            language = getCharacterData(event, eventReader);
                            break;
                        case AUTHOR:
                            author = getCharacterData(event, eventReader);
                            break;
                        case PUB_DATE:
                            pubdate = getCharacterData(event, eventReader);
                            break;
                        case COPYRIGHT:
                            copyright = getCharacterData(event, eventReader);
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().equals(ITEM)) {
                        FeedMessage message = new FeedMessage();
                        message.setAuthor(author);
                        message.setDescription(description);
                        message.setGuid(guid);
                        message.setLink(link);
                        message.setTitle(title);
                        message.setPubDate(pubdate);
                        feed.getMessages().add(message);
                        //event = eventReader.nextEvent();
                        //continue;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new Exception(e);
        }
        return feed;
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() throws XMLStreamException {
        try {
            InputStream is = new ByteArrayInputStream(stream.toByteArray());
            return is;
        }catch(Exception e){
            logger.error(e.getMessage());
            throw new XMLStreamException(e.getMessage());
        }
    }
}