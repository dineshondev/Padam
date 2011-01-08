package org.me.tablayoutdemo;

import java.io.InputStream;
import java.net.URL;


import android.app.Activity;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Tab1Activity extends Activity implements FlickrImagesInterface{
    private int globalIterator =0;
    private String[] strArray = new String[10];
    ArrayList<String> arrURLS =null;
    WSCaller caller;

    private static final String SOAP_ACTION = "http://services/tweetImage";
    private static final String METHOD_NAME = "tweetImage";
    private static final String NAMESPACE = "http://services/";
    private static final String URL = "http://cmu-418885.wv.cc.cmu.edu:8080/xpic/picServiceService?WSDL";

    private String service_url;

    public String getService_url() {
        return service_url;
    }

    public void setService_url(String service_url) {
        this.service_url = service_url;
    }

    private void initCaller( )
    {
             if ( this.caller ==null )
                this.caller= new WSCaller( URL, SOAP_ACTION, METHOD_NAME );

    }

    private String tweetImage(String a,String b,String c) throws IOException
    {
        initCaller();
        String r = caller.tweetImage(a,b,c);
        return r;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.tab1);
        //Install the receiver to receive any intents of the action cmu.me.flickrIntent
        ImagesReceiver img2 = new ImagesReceiver(Tab1Activity.this);
        IntentFilter intentF2 = new IntentFilter("cmu.me.flickrIntent");
        registerReceiver(img2, intentF2);

    }

    //Loads the image from the website
    private Drawable LoadImageFromWebsite(String url)
    {
     try
     {
     InputStream is = (InputStream) new URL(url).getContent();
     Drawable d = Drawable.createFromStream(is, "src name");
     return d;
     }
     catch (Exception e)
     {
     return null;
     }
    }

    //This method is called whenever a new intent is received
    /**
     * Our global arrayList takes the value of processLinks - which contains URLS of
     * all the photos obtained from flickr
     *
     * The three buttons get setup initially
     * The next button allows you to browse through all the photos to the right of the current photo
     * On hitting the end, a toast is generated saying that the end is reached
     *
     * The previous button allows you to browse through all the photos to the left of the current photo
     * On hitting the end, a toast message is generated saying that the end is reached
     *
     * The Tweet button allows you to tweet a message along with the image to Twitter
     *
     * */
    public void processMessage(ArrayList<String> processLinks)
    {

     arrURLS = processLinks;
        ImageView imgView = (ImageView)findViewById(R.id.ImageView01);
        if (arrURLS!=null)
        {
            Drawable drawable = LoadImageFromWebsite(arrURLS.get(globalIterator));
            imgView.setImageDrawable(drawable);
       Button leftButton = (Button)findViewById(R.id.mybutton1);
       Button rightButton = (Button)findViewById(R.id.mybutton2);
       leftButton.setOnClickListener( new OnClickListener() {

            public void onClick(View arg0) {
             ImageView imgView = (ImageView)findViewById(R.id.ImageView01);
             if (globalIterator ==0)
             {
             Toast.makeText(Tab1Activity.this, "Hit the left end of the array", Toast.LENGTH_SHORT).show();
             }
             else
             {
             globalIterator--;
             Drawable drawable = LoadImageFromWebsite(arrURLS.get(globalIterator));
             imgView.setImageDrawable(drawable);
             }
            }
       });

       rightButton.setOnClickListener( new OnClickListener() {
            public void onClick(View arg0) {
             ImageView imgView = (ImageView)findViewById(R.id.ImageView01);
             if (globalIterator >=9)
             {
             Toast.makeText(Tab1Activity.this, "Hit the right end of the array", Toast.LENGTH_SHORT).show();
             }
             else
             {
             globalIterator++;
             Drawable drawable = LoadImageFromWebsite(arrURLS.get(globalIterator));
             imgView.setImageDrawable(drawable);
             }
            }
         });

          Button tweetButton = (Button)findViewById(R.id.mybutton3);
          tweetButton.setOnClickListener( new OnClickListener() {

            public void onClick(View arg0)
            {
                EditText picname= (EditText) findViewById(R.id.pic_name);
             EditText myedittext = (EditText)findViewById(R.id.myedittext);
                 String str=myedittext.getText().toString();
                 String picnm=picname.getText().toString();
                try
                {
                   String r = tweetImage(picnm,arrURLS.get(globalIterator),str);
                }
                catch (IOException ex)
                {
                    Logger.getLogger(MyMapActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
         });
        }
    }

}

