package org.me.tablayoutdemo;


import android.app.TabActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TabHost;
import java.util.ArrayList;


/*This class essentially is the central class which defines the other two tabs
 * 1) The Map Tab through which the users enter the location they want to search
 * 2) The Pictures tab through which people can browse through photos and also tweet them
 * */
public class MainActivity extends TabActivity implements FlickrImagesInterface{

     Intent mapIntent;
     Intent browserIntent;
     Intent mytabIntent;
     TabHost tabs;
    @Override
    public void onCreate(Bundle icicle) {
      super.onCreate(icicle);
      setContentView(R.layout.main);

      tabs = getTabHost();
      // defining intents to call activities that will be placed in tabs
      mytabIntent=new Intent(this, Tab1Activity.class);
      mapIntent=new Intent(this, MyMapActivity.class);

      // Adding tabs that will be associated to the intents
      addTab( "Map", mapIntent );
      addTab( "Pictures", mytabIntent );

      //Installing the class to receive intents with the following actions - "cmu.me.flickrintent"
      ImagesReceiver img = new ImagesReceiver(this);
      IntentFilter intentF = new IntentFilter("cmu.me.flickrIntent");
      registerReceiver(img, intentF);
    }

    //Adds the tab with the name - name associated with the intent intent
    void addTab( String name, Intent intent )
    {
        tabs.addTab(
                tabs.newTabSpec( name )
                    .setIndicator( name )
                    .setContent(intent)
              );
    }

    //On receiving the intent, the tab gets changed to tab1
    public void processMessage(ArrayList<String> processLinks) {
        tabs.setCurrentTab(1);
    }

}
