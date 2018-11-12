package detect.weather.jimmykr.weather;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

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

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import android.util.Log;
import android.support.v7.widget.Toolbar;


public class MainActivity extends ActionBarActivity {

    String City;
    EditText place;
    Button search;
    TextView mTextView01;
    TextView mTextView02;
    TextView mTextView03;
    TextView mTextView04;
    TextView mTextView05;
    TextView mTextView06;
    TextView mTextView07;
    private LocationManager mLocationManager;
    private static final String TAG = "WeatherMAIN";

    static class MyWeather1{

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

            s1 = description + " -\n" + "city: " + city + "\n"
                    + "country: " + country + "\n"
                    + "clouds\n"
                    + "clouds: " + windDirection + "\n"
                    + "temperature(F): " + temperature + "\n"
                    + "Min temperature(F): "+Mintemperature +"\n"
                    + "Max temperature(F): "+Maxtemperature +"\n";

            return s1;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(MainActivity.this,"GPS permissions not open",Toast.LENGTH_SHORT).show();
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        Log.d(TAG,"GPS TEST " );
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);*/
        place = (EditText) findViewById(R.id.place);
        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(searchOnClickListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        Log.d(TAG, "open Menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Log.d(TAG, "select Menu Item");
        switch(item.getItemId()) {
            case R.id.action_about:
                openOptionsDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openOptionsDialog() {
        Log.d(TAG, "open Dialog");
        new AlertDialog.Builder(this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.about_msg)
                .setPositiveButton(R.string.ok_label,
                        new DialogInterface.OnClickListener(){
                            public void onClick(
                                    DialogInterface dialoginterface, int i){
                            }
                        })
                .show();
    }

    Button.OnClickListener searchOnClickListener
            = new Button.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            if (place.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(),
                        "Enter place!",
                        Toast.LENGTH_LONG).show();
            } else {
                City = place.getText().toString();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, GetWeather.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("City", City);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

    //在Resume階段設定mLocationListener介面，可以獲得地理位置的更新資料
    @Override
    protected void onResume() {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(MainActivity.this,"GPS permissions not open",Toast.LENGTH_SHORT).show();
//            如果是6.0以上的去需求權限

                return;
            }
            Log.d(TAG,"GPS TEST RESUME " );
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    mLocationListener);
        }
        super.onResume();
    }
    //在Pause階段關閉mLocationListener介面，不再獲得地理位置的更新資料
    @Override
    protected void onPause() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
        Log.d(TAG,"GPS TEST PAUSE" );
        super.onPause();
    }
    String[] weatherResult = new String[7];;
    String weatherResult1;
    String weatherResult2;
    String http;
    private String[] Arr = new String[2];
    String PlaceApisBase;
    String Today;
    String week;
    public LocationListener mLocationListener = new LocationListener()
    {
        //GPS位置資訊被更新
        public void onLocationChanged(Location location) {
            Log.d(TAG,"GPS TEST " );
            mTextView01 = (TextView) findViewById(R.id.today);
            mTextView02 = (TextView) findViewById(R.id.Tomorrow);
            mTextView03 = (TextView) findViewById(R.id.week1);
            mTextView04 = (TextView) findViewById(R.id.week2);
            mTextView05 = (TextView) findViewById(R.id.week3);
            mTextView06 = (TextView) findViewById(R.id.week4);
            mTextView07 = (TextView) findViewById(R.id.week5);
            String Latitude=String.valueOf(location.getLatitude());Log.d(TAG, "Latitude"+ Latitude);
            String Lat = Latitude.substring(0, Latitude.indexOf(".") + 3);Log.d(TAG, "Lat"+ Lat);
            String Longitude=String.valueOf(location.getLongitude());Log.d(TAG, "Longitude"+ Longitude);
            String Lon = Longitude.substring(0, Longitude.indexOf(".") + 3);Log.d(TAG, "Lon"+ Lon);
            Arr[0] = "http://api.openweathermap.org/data/2.5/weather?lat=" + Lat + "&lon=" + Lon + "&mode=xml&appid=6c407f412bf644e72fa060adb84c6263";
            PlaceApisBase = "http://api.openweathermap.org/data/2.5/forecast?lat=" + Lat + "&lon=" + Lon + "&mode=xml&appid=6c407f412bf644e72fa060adb84c6263";
            Arr[1]="http://api.openweathermap.org/data/2.5/forecast/daily?lat="+ Lat +"&lon=" + Lon + "&mode=xml&appid=6c407f412bf644e72fa060adb84c6263";
            //Log.d(TAG, "PlaceApisBase"+ PlaceApisBase);
            Thread thread = new Thread(mutiThread);
            thread.start();

            mTextView01.setText(weatherResult[0]);
            mTextView02.setText(weatherResult[1]);
            mTextView03.setText(weatherResult[2]);
            mTextView04.setText(weatherResult[3]);
            mTextView05.setText(weatherResult[4]);
            mTextView06.setText(weatherResult[5]);
            mTextView07.setText(weatherResult[6]);
           // Log.d(TAG,"Latitude： " + String.valueOf(location.getLatitude()));
            //mTextView02.setText("經度-Longitude：  " );

        }
        //GPS位置資訊的狀態被更新
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.v("Status", "AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.v("Status", "OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.v("Status", "TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }


        public void onProviderDisabled(String provider) {

        }
        public void onProviderEnabled(String provider) {

        }

        protected Runnable mutiThread = new Runnable(){
            public void run(){
                Document weatherDoc[]=new Document[2];

                for(int i=0;i<2;i++) {

                http = GetWeather.Dealxmldata(Arr[i]);
                    weatherDoc[i] = convertStringToDocument(http);
                }

                    if (weatherDoc != null) {
                       try {
                                weatherResult[0] = parseWeather(weatherDoc[0]).toString();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }} else {
                        weatherResult[0] = "Cannot convertStringToDocument!";
                    }
                       for( int count=1;count<7;count++)
                       {
                            weatherResult[count] = Forecast.parseWeather(weatherDoc[1], count).toString();
                            //weatherResult[2] = parseWeather(weatherDoc).toString();
                        }

                //weatherResult1 = Forecast.tomorrow(PlaceApisBase);
                    //Forecast.week(week);



                           }
        };


        };




    private Document convertStringToDocument(String src){
        Document dest = null;
        DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder parser;

        try {
            parser = dbFactory.newDocumentBuilder();
            dest = parser.parse(new ByteArrayInputStream(src.getBytes()));//Log.d(TAG, "dest :" + dest);Log.d(TAG, "src.getBytes() :" + src.getBytes());Log.d(TAG, "src :" + src);
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace(); Log.d(TAG,"e1.printStackTrace() ");
        } catch (SAXException e) {
            e.printStackTrace();  Log.d(TAG," e.printStackTrace() ");
        } catch (IOException e) {
            e.printStackTrace();  Log.d(TAG,"e.printStackTrace(); ");
        }

        return dest;
    }

    private MyWeather1 parseWeather(Document srcDoc) throws ParseException {

        MyWeather1 myWeather = new MyWeather1();

        //<current></current> make sure weather info get
        NodeList descNodelist = srcDoc.getElementsByTagName("current");
        // Log.d(TAG,"srcDoc :  " + srcDoc);
       // myWeather.city = ;Log.d(TAG,"jkrname " + myWeather.city);
        NodeList descNodelist1 = srcDoc.getElementsByTagName("lastupdate");
        Node locationNode1 = descNodelist1.item(0);
        NamedNodeMap locNamedNodeMap1 = locationNode1.getAttributes();
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //df.setTimeZone(TimeZone.getTimeZone("GMT")); // or set GMT
        //Date date = df.parse(locNamedNodeMap1.getNamedItem("value").getNodeValue().toString());

//透過SimpleDateFormat的format方法將Date轉為字串

        //String dts=df.format(date);
        if(descNodelist != null && descNodelist.getLength() > 0){
            myWeather.description = "Today : " + locNamedNodeMap1.getNamedItem("value").getNodeValue().toString();
        }else{
            myWeather.description = "EMPTY";
        }

        //<city id="1668341" name="Taipei">
        NodeList locationNodeList = srcDoc.getElementsByTagName("city");
        if(locationNodeList != null && locationNodeList.getLength() > 0){
            Node locationNode = locationNodeList.item(0);
            NamedNodeMap locNamedNodeMap = locationNode.getAttributes();
           // Log.d(TAG,"srcDoc :  " + srcDoc);
            myWeather.city = locNamedNodeMap.getNamedItem("name").getNodeValue().toString();Log.d(TAG,"jkrname " + myWeather.city);
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
        NodeList windNodeList = srcDoc.getElementsByTagName("clouds");
        if(windNodeList != null && windNodeList.getLength() > 0){
            Node windNode = windNodeList.item(0);
            NamedNodeMap windNamedNodeMap = windNode.getAttributes();

            //myWeather.windChill = windNamedNodeMap.getNamedItem("value").getNodeValue().toString();
            myWeather.windDirection = windNamedNodeMap.getNamedItem("name").getNodeValue().toString();
            //<speed value="4.6" name="Gentle Breeze"/>
            /*NodeList windNodeList1 = srcDoc.getElementsByTagName("speed");
            Node windNode1 = windNodeList1.item(0);
            NamedNodeMap windNamedNodeMap1 = windNode.getAttributes();
            myWeather.windSpeed = windNamedNodeMap.getNamedItem("value").getNodeValue().toString();*/
        }else{
            myWeather.windChill = "EMPTY";
            myWeather.windDirection = "EMPTY";
            myWeather.windSpeed = "EMPTY";
        }



        return myWeather;
    }

}
