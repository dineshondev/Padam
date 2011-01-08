
package org.me.tablayoutdemo;

import android.util.Log;
import java.util.ArrayList;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.io.IOException;




public class WSCaller {

    private final String NAMESPACE = "http://services/";     // namespace
    private String soap_action;                                 // Soap action
    protected String method_name;                            
    private String url;                                      

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public String getSoap_action() {
        return soap_action;
    }

    public void setSoap_action(String soap_action) {
        this.soap_action = soap_action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static String LOG_TAG="WSCALLER";

    
    public WSCaller(String base_url, String soap_action, String method_name) {
        this.soap_action = new String(soap_action);
        this.method_name = method_name;
        this.url = base_url;
        Log.d( LOG_TAG,"constructor" );
    }


    public SoapObject invokeWebService(String[] fieldName, String[] fieldValue) {
        SoapObject request = new SoapObject(NAMESPACE, method_name);

       
        for (int i = 0; i < fieldName.length; ++i) {
            request.addProperty(fieldName[i], fieldValue[i]);
        }

        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        try {
            androidHttpTransport.call(soap_action, soapEnvelope);
            return (SoapObject) soapEnvelope.bodyIn;
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<String> getflickrImages(String a ) throws IOException {
        SoapObject result = invokeWebService(
                                     new String[]{"location"},
                                     new String[]{a});
        ArrayList<String> response = null;
        ArrayList<String> list = new ArrayList<String>();
        Log.d( LOG_TAG,"add - process response " );
     
        if (result != null) {
            for(int i=0; i<10; i++)
            {
                //Object property = result.getProperty(i);
              list.add(result.getProperty(i).toString());
            }
          } else {
              response.add("Empty");
          }
        return list;
    }

     public String tweetImage(String a,String b,String c ) throws IOException {
        SoapObject result = invokeWebService(
                new String[]{"name","link","comment"},
                new String[]{a,b,c});
        String response = null;
                Log.d( LOG_TAG,"add - process response " );
        if (result != null) {
            response="";
        } else {
            response="Empty";
        }
        return response;
    }
}