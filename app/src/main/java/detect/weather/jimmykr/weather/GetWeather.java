package detect.weather.jimmykr.weather;


        import java.io.BufferedReader;
        import java.io.ByteArrayInputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.Reader;
        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.ParserConfigurationException;

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

        import android.app.Activity;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.TextView;
        import android.widget.Toast;


public class GetWeather extends Activity {
    private static final String TAG = "Weather";
    TextView weather;

    static class MyWeather{

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

            String s,s1;

            s = description + " -\n\n" + "city: " + city + "\n"
                    + "country: " + country + "\n\n"
                    + "Wind\n"
                    + "Windname: " + windChill + "\n"
                    + "direction: " + windDirection + "\n"
                    + "speed: " + windSpeed + "\n\n"
                    + "Sunrise: " + sunrise + "\n"
                    + "Sunset: " + sunset + "\n\n"
                    + "temperature(F): " + temperature + "\n"
                    + "Min temperature(F): "+Mintemperature +"\n"
                    + "Max temperature(F): "+Maxtemperature +"\n";

            return s;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_weather);
        weather = (TextView)findViewById(R.id.weather);

        Bundle bundle = this.getIntent().getExtras();
        String city = (String)bundle.getCharSequence("City");
        Log.d(TAG,"STRING : " + city);
        new MyQueryWeatherTask(city).execute();

        Toast.makeText(getApplicationContext(),
                city,
                Toast.LENGTH_LONG).show();
    }

    private class MyQueryWeatherTask extends AsyncTask<Void, Void, Void>{

        String woeid;
        String weatherResult;
        String weatherString;

        MyQueryWeatherTask(String w){
            woeid = w;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            weatherString = QueryWeather();
            Document weatherDoc = convertStringToDocument(weatherString);

            if(weatherDoc != null){
                weatherResult = parseWeather(weatherDoc).toString();Log.d(TAG,"weatherDoc != null ");
            }else{
                weatherResult = "Cannot convertStringToDocument!";Log.d(TAG,"Cannot convertStringToDocument! ");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            weather.setText(weatherResult);
            super.onPostExecute(result);
        }

       private String QueryWeather(){
            String qResult = "";
            //"http://api.openweathermap.org/data/2.5/weather?id=2172797&mode=xml&appid=6c407f412bf644e72fa060adb84c6263
            String queryString = "http://api.openweathermap.org/data/2.5/weather?q="+ woeid +"&mode=xml&appid=6c407f412bf644e72fa060adb84c6263";

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(queryString);

            try {
                HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

                if (httpEntity != null){
                    InputStream inputStream = httpEntity.getContent();
                    Reader in = new InputStreamReader(inputStream);
                    BufferedReader bufferedreader = new BufferedReader(in);
                    StringBuilder stringBuilder = new StringBuilder();

                    String stringReadLine = null;

                    while ((stringReadLine = bufferedreader.readLine()) != null) {
                        stringBuilder.append(stringReadLine + "\n");
                    }

                    qResult = stringBuilder.toString();
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return qResult;
        }

        private Document convertStringToDocument(String src){
            Document dest = null;

            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder parser;

            try {
                parser = dbFactory.newDocumentBuilder();
                dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
            } catch (ParserConfigurationException e1) {
                e1.printStackTrace(); Log.d(TAG,"e1.printStackTrace() ");
            } catch (SAXException e) {
                e.printStackTrace();  Log.d(TAG," e.printStackTrace() ");
            } catch (IOException e) {
                e.printStackTrace();  Log.d(TAG,"e.printStackTrace(); ");
            }

            return dest;
        }

        private MyWeather parseWeather(Document srcDoc){

            MyWeather myWeather = new MyWeather();

            //<current></current> make sure weather info get
            NodeList descNodelist = srcDoc.getElementsByTagName("current");
            if(descNodelist != null && descNodelist.getLength() > 0){
                myWeather.description = "OpenWeatherMap! Weather for " + woeid;
            }else{
               myWeather.description = "EMPTY";
            }

            //<city id="1668341" name="Taipei">
            NodeList locationNodeList = srcDoc.getElementsByTagName("city");
            if(locationNodeList != null && locationNodeList.getLength() > 0){
                Node locationNode = locationNodeList.item(0);
                NamedNodeMap locNamedNodeMap = locationNode.getAttributes();
                Log.d(TAG,"srcDoc :  " + srcDoc);
                myWeather.city = locNamedNodeMap.getNamedItem("name").getNodeValue().toString();Log.d(TAG,"name " + myWeather.city);
              // <country>TW</country>
                NodeList locationNodeList1 = srcDoc.getElementsByTagName("country");
               myWeather.country= locationNodeList1.item(0).getTextContent();Log.d(TAG,"myWeather.country " + myWeather.country);
            }else{
                myWeather.city = "EMPTY";
                myWeather.country = "EMPTY";
            }
            //<sun rise="2017-05-16T21:08:21" set="2017-05-17T10:32:31"/>
            NodeList astNodeList = srcDoc.getElementsByTagName("sun");
            if(astNodeList != null && astNodeList.getLength() > 0){
                Node astNode = astNodeList.item(0);
                NamedNodeMap astNamedNodeMap = astNode.getAttributes();

                myWeather.sunrise = astNamedNodeMap.getNamedItem("rise").getNodeValue().toString();
                myWeather.sunset = astNamedNodeMap.getNamedItem("set").getNodeValue().toString();
            }else{
                myWeather.sunrise = "EMPTY";
                myWeather.sunset = "EMPTY";
            }

            //<temperature value="296.01" min="295.15" max="297.15" unit="kelvin"/>
            NodeList conditionNodeList = srcDoc.getElementsByTagName("temperature");
            if(conditionNodeList != null && conditionNodeList.getLength() > 0){
                Node conditionNode = conditionNodeList.item(0);
                NamedNodeMap conditionNamedNodeMap = conditionNode.getAttributes();

                myWeather.temperature = conditionNamedNodeMap.getNamedItem("value").getNodeValue().toString();
                myWeather.Mintemperature = conditionNamedNodeMap.getNamedItem("min").getNodeValue().toString();
                myWeather.Maxtemperature=conditionNamedNodeMap.getNamedItem("max").getNodeValue().toString();
            }else{
                myWeather.temperature = "EMPTY";
                myWeather.Mintemperature = "EMPTY";
                myWeather.Maxtemperature = "EMPTY";
            }
            //<direction value="90" code="E" name="East"/>
            NodeList windNodeList = srcDoc.getElementsByTagName("direction");
            if(windNodeList != null && windNodeList.getLength() > 0){
                Node windNode = windNodeList.item(0);
                NamedNodeMap windNamedNodeMap = windNode.getAttributes();

                myWeather.windChill = windNamedNodeMap.getNamedItem("value").getNodeValue().toString();
               myWeather.windDirection = windNamedNodeMap.getNamedItem("name").getNodeValue().toString();
                //<speed value="4.6" name="Gentle Breeze"/>
                NodeList windNodeList1 = srcDoc.getElementsByTagName("speed");
                Node windNode1 = windNodeList1.item(0);
                NamedNodeMap windNamedNodeMap1 = windNode.getAttributes();
                myWeather.windSpeed = windNamedNodeMap.getNamedItem("value").getNodeValue().toString();
            }else{
                myWeather.windChill = "EMPTY";
                myWeather.windDirection = "EMPTY";
                myWeather.windSpeed = "EMPTY";
            }



            return myWeather;
        }

    }
    protected static  String Dealxmldata(String placeApisBase) {
        String Result = " ";
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(placeApisBase);

        try {
            HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

            if (httpEntity != null){
                InputStream inputStream = httpEntity.getContent();
                Reader in = new InputStreamReader(inputStream);
                BufferedReader bufferedreader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();

                String stringReadLine = null;

                while ((stringReadLine = bufferedreader.readLine()) != null) {
                    stringBuilder.append(stringReadLine + "\n");
                }

                Result = stringBuilder.toString();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result;
    }
}
