/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.tablayoutdemo;

import java.util.ArrayList;

/*
 * This interface is implemented by
 * 1) Tab1Activity
 * 2) Main Activity
 *
 * The ImagesReceiver class which implements the broadcast receiver also has a FlickrImagesInterface Object.
 * */
public interface FlickrImagesInterface
{
    public void processMessage(ArrayList<String> processLinks);
}

