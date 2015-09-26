package scoreboard.gabeotron.com.scoreboard;

import android.app.Activity;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by gabeheath on 9/19/15.
 */
public class HandleXML {

    /**
     * Id Search Variables
     */
    private String id;
    private String idYearPublished;
    private String idMinPlayers;
    private String idMaxPlayers;
    private String idPlayTime;
    private String idAge;
    private String idName;
    private String idDescription;
    private String idThumbnail;
    private String idImage;
    private String idAvgRating;

    /**
     * Game Search Variables
     */
    private ArrayList<String> gameIdList = new ArrayList<>();
    private ArrayList<String> gameNameList = new ArrayList<>();
    private ArrayList<String> gameYearPublishedList = new ArrayList<>();
    private ArrayList<String> gameThumbnailURL = new ArrayList<>();

    private String soloThumbnail;



    private String urlString = null;

    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete=true;

    public String getId() {
        return id;
    }

    public HandleXML(String url) {
        this.urlString = url;
    }

    public String getIdYearPublished() {
        return idYearPublished;
    }

    public String getIdMinPlayers() {
        return idMinPlayers;
    }

    public String getIdMaxPlayers() {
        return idMaxPlayers;
    }

    public String getIdPlayTime() {
        return idPlayTime;
    }

    public String getIdAge() {
        return idAge;
    }

    public String getIdName() {
        return idName;
    }

    public String getIdDescription() {
        return idDescription;
    }

    public String getIdThumbnail() {
        return idThumbnail;
    }

    public String getIdImage() {
        return idImage;
    }

    public String getIdAvgRating() {
        return idAvgRating;
    }

    public ArrayList<String> getGameId() {
        return gameIdList;
    }

    public ArrayList<String> getGameName() {
        return gameNameList;
    }

    public ArrayList<String> getGameYearPublished() {
        return gameYearPublishedList;
    }

    public String getSoloThumbnail() {
        return soloThumbnail;
    }

    public ArrayList<String> getGameThumbnailURL() {
        return gameThumbnailURL;
    }

    public void parseIdXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;

        try {
            event = myParser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(name.equals("item")){
                            id = myParser.getAttributeValue(null, "id");
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text=myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "yearpublished":
                                idYearPublished = myParser.getAttributeValue(null, "value");
                                break;
                            case "minplayers":
                                idMinPlayers = text;
                                break;
                            case "maxplayers":
                                idMaxPlayers = text;
                                break;
                            case "playtime":
                                idPlayTime = text;
                                break;
                            case "age":
                                idAge = text;
                                break;
                            case "name":
                                idName = myParser.getAttributeValue(null, "value");
                                break;
                            case "description":
                                idDescription = text;
                                break;
                            case "thumbnail":
                                idThumbnail = text;
                                break;
                            case "image":
                                idImage = text;
                                break;
                            case "average":
                                idAvgRating = myParser.getAttributeValue(null, "value");
                                break;
                            default:
                                break;
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void parseGameXMLAndStoreIt(XmlPullParser myParser) {

        int event;
        String text = null;

        try {
            event = myParser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(name.equals("item")){
                            String id = myParser.getAttributeValue(null, "id");
                            gameIdList.add(id);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text=myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "name":
                                gameNameList.add(myParser.getAttributeValue(null, "value"));
                                break;
                            case "yearpublished":
                                gameYearPublishedList.add(myParser.getAttributeValue(null, "value"));
                                break;
                            default:
                                break;
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void parseThumbnailXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text = null;

        try {
            event = myParser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text=myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("thumbnail")) {
                            soloThumbnail = text;
                        } else{}
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML(final String searchType) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                /**
                 * For some reason the HttpClient only returns 19 requests with a 200 status code. 20+ return
                 * as 503 error codes. I originally implemented this with HttpURLConnection (which you can see in
                 * the commented section below) and it actually returned a FileNotFoundException for the url request
                 * but since I switched to OkHttpClient, it no longer outputs any errors, but still behaves in the
                 * same way as HttpURLConnection except it doesn't crash when it hits the 20 limit.
                 */

                OkHttpClient client = new OkHttpClient();

                client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
                client.setReadTimeout(20, TimeUnit.SECONDS);    // socket timeout

                Request request = new Request.Builder()
                        .url(urlString)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    InputStream stream = response.body().byteStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myParser = xmlFactoryObject.newPullParser();
                    myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myParser.setInput(stream, null);
                    switch (searchType) {
                        case "id":
                            parseIdXMLAndStoreIt(myParser);
                            break;
                        case "game":
                            parseGameXMLAndStoreIt(myParser);
                            break;
                        case "thumbnail":
                            parseThumbnailXMLAndStoreIt(myParser);
                            break;
                    }
                    stream.close();
                    response.body().close();
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }

//                try {
//                    URL url = new URL(urlString);
//                    HttpURLConnection connect = (HttpURLConnection)url.openConnection();
//                    connect.setReadTimeout(10000);
//                    connect.setConnectTimeout(15000);
//                    connect.setRequestMethod("GET");
//                    connect.setDoInput(true);
//                    connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");
//                    connect.connect();
//
//                    InputStream stream;
//                    int responseCode = connect.getResponseCode();
//
//                    Log.d("uuuuuuuuuuuuuu", urlString);
//                    Log.d("jjjjjjjjjjjjjj", "value" + responseCode);
//                    Log.d("rrrrrrrrrrrrrr", connect.getResponseMessage());
//
//                    if(responseCode < 400){
//                        stream = connect.getInputStream();
//                    } else {
//                        stream = connect.getErrorStream();
//                    }
//
//                    //InputStream stream = connect.getInputStream();
//                    xmlFactoryObject = XmlPullParserFactory.newInstance();
//                    XmlPullParser myParser = xmlFactoryObject.newPullParser();
//                    myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//                    myParser.setInput(stream, null);
//                    parseThumbnailXMLAndStoreIt(myParser);
//                    stream.close();
//                    connect.disconnect();
//
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        thread.start();
    }
}
