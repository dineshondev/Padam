/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.tablayoutdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;


/**
 * This class essentially receives the broadcasts that are sent.
 * It contains a FlickrImagesInterface object
 *
 * Whenever, it receives a broadcast message, it gets the ArrayList from
 * the broadcasted message and uses that to call the processMessage(ArrayList)
 *
 * This in turn calls the corresponding message in MainActivity and Tab1Activity()
 */
public class ImagesReceiver extends BroadcastReceiver
{
    FlickrImagesInterface tempInt;
    public ImagesReceiver()
    {
        tempInt = null;
    }
    public ImagesReceiver(FlickrImagesInterface tempFlickr)
    {
        tempInt = tempFlickr;
    }

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        ArrayList<String> arrStr = new ArrayList<String>();
        if (tempInt != null)
        {
            arrStr = arg1.getStringArrayListExtra("arrayListExtra");

            tempInt.processMessage(arrStr);
        }
    }
}