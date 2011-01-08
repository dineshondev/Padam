package org.me.tablayoutdemo;


import java.io.IOException;
import java.util.List;

import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/*
 * Activity that is responsible for interacting with the google map
 */
public class MyMapActivity extends MapActivity  {
    private MapController mapController;
    MapView mapView;
    RadioGroup mapMode;
    TabHost tabs;
    float lat;
    float longitude;
    EditText resultTxt;
    WSCaller caller=null;

    private final static String TODO="todo2.txt";
    private static final String SOAP_ACTION = "http://services/getflickrImages";
    private static final String METHOD_NAME = "getflickrImages";
    private static final String NAMESPACE = "http://services/";
    private static final String URL = "http://cmu-418885.wv.cc.cmu.edu:8080/xpic/picServiceService?WSDL";
    private String service_url;

    public String getService_url() {
        return service_url;
    }

    public void setService_url(String service_url) {
        this.service_url = service_url;
    }

    private void initCaller() {
             if ( this.caller ==null )
                this.caller= new WSCaller( URL, SOAP_ACTION, METHOD_NAME );
    }

    private ArrayList<String> getflickrImages(String a) throws IOException    {
        initCaller();
        ArrayList<String> r= caller.getflickrImages(a);
        return r;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.setContentView(R.layout.map);
        mapView=(MapView)findViewById(R.id.mapView);
        mapView.displayZoomControls(true);
        mapView.setClickable(true);

        mapController = mapView.getController();
        mapController.setZoom(9);
        mapMode= (RadioGroup) findViewById(R.id.mapMode);

        int y =10 ;
        int x =10;

        MapView.LayoutParams lp;
        lp = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
        MapView.LayoutParams.WRAP_CONTENT,
        x, y,
        MapView.LayoutParams.TOP_LEFT);
        View zoomControls = mapView.getZoomControls();

        mapView.addView(zoomControls, lp);
        mapView.setTraffic(false);

        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);

        mapView.invalidate();

        EditText lati=(EditText)findViewById(R.id.lat);
        final String la=lati.getText().toString();

        Button getPics = (Button)findViewById(R.id.getPics);

        getPics.setOnClickListener(new OnClickListener() {

        //On clicking on the 'Get Pictures' button, the location associated with that particular
        //latitude and longitude in the editboxes is generated.
        //This location is then sent to our web-service to generate an arrayList of pictures associated
        //with the location

        //A new intent is created set to a particular action and then broadcasted
            public void onClick(View arg0) {
                ArrayList<String> r = null;
                try {
                    Geocoder myLocation = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> myList = myLocation.getFromLocation(lat, longitude,1);
                    String str="";
                    if (myList.size() >0)
                    {
                        for (int i=0; i<myList.get(0).getMaxAddressLineIndex(); i++)
                        {
                        str += myList.get(0).getAddressLine(i) + "\n";
                        }
                    }
                 String[] arr = str.split(",");
                 Toast.makeText(getApplicationContext(), arr[0] ,Toast.LENGTH_SHORT).show();
                 r = getflickrImages(arr[0]);
                } catch (IOException ex) {
                    Logger.getLogger(MyMapActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
                Intent flickrIntent = new Intent();
                flickrIntent.setAction("cmu.me.flickrIntent");
                flickrIntent.putStringArrayListExtra("arrayListExtra", r);
                sendBroadcast(flickrIntent);
            }
         });

        //Keeps changing between the satellite mode and the road mode
        mapMode.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.mapR) {
                     setMapMode(true);
                }                else {
                    setMapMode(false);
                }
            }
            } );
    }


    class MapOverlay extends com.google.android.maps.Overlay
    {
    /*
     * On touching the map, the latitude and longitude are populated in the two edit text boxes
     * */
        public boolean onTouchEvent(MotionEvent event, MapView mapView)
        {
         EditText LatText = (EditText)findViewById(R.id.lat);
         EditText LongText = (EditText)findViewById(R.id.longit);

            //---when user lifts his finger---
            if (event.getAction() == 1)
            {
                GeoPoint p = mapView.getProjection().fromPixels(
                (int) event.getX(),
                (int) event.getY());
                lat = (float) (p.getLatitudeE6()/1E6);
                longitude = (float)(p.getLongitudeE6()/1E6);
                LatText.setText(lat+"");
                LongText.setText(longitude+"");
            }

            return false;
        }
    }


    //Switches it to Satellite mode
     private void setSatelliteMode(boolean b)
     {
         mapView.setSatellite(b);
     }

     //Switches it to the street location
     private void setMapMode(boolean b)
     {
         mapView.setSatellite(!b);
     }

    @Override
    protected boolean isRouteDisplayed() {
         return false;
    }
}