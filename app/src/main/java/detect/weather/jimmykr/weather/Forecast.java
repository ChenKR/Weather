package detect.weather.jimmykr.weather;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Jimmy on 2017/5/21.
 */

public class Forecast {

    static String http;
    //static int count=0;
    private static final String TAG = "forecast";
    static String weatherResult;
    static class Tomorrow{

        String description;
        String city;
        String country;

        String windChill;
        String windDirection;
        String windSpeed;

        String sunrise;
        String sunset;

        String temperature;
        String Mintemperature;
        String Maxtemperature;

        public String toString(){

            String s1;

            s1 = description + " -\n"
                    + "clouds\n"
                    + "clouds: " + windDirection + "\n"
                    + "temperature(F): " + temperature + "\n"
                    + "Min temperature(F): "+Mintemperature +"\n"
                    + "Max temperature(F): "+Maxtemperature +"\n";

            return s1;
        }
    }
   /* public static String tomorrow(String placeApisBase) {
        String tom = placeApisBase;
        http=Dealxmldata(tom);
        //if (http != null)

        Document weatherDoc =  convertStringToDocument(http);

        if(weatherDoc != null){
            weatherResult = parseWeather(weatherDoc, count).toString();Log.d("weatherResult : ", weatherResult);
        }else{
            weatherResult = "Cannot convertStringToDocument!";
        }
        return weatherResult ;
    }*/

    protected static Tomorrow parseWeather(Document weatherDoc, int count) {
        Tomorrow Tomo = new Tomorrow();

        //<current></current> make sure weather info get
        NodeList descNodelist = weatherDoc.getElementsByTagName("weatherdata");
        // Log.d(TAG,"srcDoc :  " + srcDoc);
        // myWeather.city = ;Log.d(TAG,"jkrname " + myWeather.city);
        NodeList descNodelist1 = weatherDoc.getElementsByTagName("time");
        Node locationNode1 = descNodelist1.item(count);Log.d(TAG, "count :" + count);
        NamedNodeMap locNamedNodeMap1 = locationNode1.getAttributes();
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //df.setTimeZone(TimeZone.getTimeZone("GMT")); // or set GMT
        //Date date = df.parse(locNamedNodeMap1.getNamedItem("value").getNodeValue().toString());

//透過SimpleDateFormat的format方法將Date轉為字串

        //String dts=df.format(date);
        if(descNodelist != null && descNodelist.getLength() > 0){
           Tomo.description = "Time : " + locNamedNodeMap1.getNamedItem("day").getNodeValue().toString();
        }else{
            Tomo.description = "EMPTY";
        }

        //<temperature value="296.01" min="295.15" max="297.15" unit="kelvin"/>
        NodeList conditionNodeList = weatherDoc.getElementsByTagName("temperature");
        if(conditionNodeList != null && conditionNodeList.getLength() > 0){
            Node conditionNode = conditionNodeList.item(count);
            NamedNodeMap conditionNamedNodeMap = conditionNode.getAttributes();

            Tomo.temperature = conditionNamedNodeMap.getNamedItem("day").getNodeValue().toString();
            Tomo.Mintemperature = conditionNamedNodeMap.getNamedItem("min").getNodeValue().toString();
            Tomo.Maxtemperature=conditionNamedNodeMap.getNamedItem("max").getNodeValue().toString();
        }else{
            Tomo.temperature = "EMPTY";
            Tomo.Mintemperature = "EMPTY";
            Tomo.Maxtemperature = "EMPTY";
        }
        //<direction value="90" code="E" name="East"/>
        NodeList windNodeList = weatherDoc.getElementsByTagName("clouds");
        if(windNodeList != null && windNodeList.getLength() > 0){
            Node windNode = windNodeList.item(count);
            NamedNodeMap windNamedNodeMap = windNode.getAttributes();

            //myWeather.windChill = windNamedNodeMap.getNamedItem("value").getNodeValue().toString();
            Tomo.windDirection = windNamedNodeMap.getNamedItem("value").getNodeValue().toString();
            //<speed value="4.6" name="Gentle Breeze"/>
            /*NodeList windNodeList1 = srcDoc.getElementsByTagName("speed");
            Node windNode1 = windNodeList1.item(0);
            NamedNodeMap windNamedNodeMap1 = windNode.getAttributes();
            myWeather.windSpeed = windNamedNodeMap.getNamedItem("value").getNodeValue().toString();*/
        }else{
           // Tomo.windChill = "EMPTY";
            Tomo.windDirection = "EMPTY";
            //Tomo.windSpeed = "EMPTY";
        }



        return Tomo;
    }



}
