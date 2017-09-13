package com.example.observer;

//import java.util.List;


import java.util.List;

import GPS.GPSTracker;
import GPS.gpsProcess;
import GPS.getlocation;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallReciever extends BroadcastReceiver{        

	public static List<Address> listlocation = null;
    
    private static final String TAG = "PhoneStatReceiver"; 

    private static boolean incomingFlag = false;

    private static String incoming_number = null;

    private   List<Address> result;
    
    Context myRefr;
    
    private String getlocation1(Context context){
    	GPSTracker gps = new GPSTracker(context);
        try{
    	if(gps.canGetLocation()){     
      	  	double  latitude = gps.getLatitude();
      	  	double  longitude = gps.getLongitude();
              gpsProcess GL =new gpsProcess(longitude,latitude){
            	  @Override
            	protected void onPostExecute(List<Address> result) {
            		super.onPostExecute(result);
            		sendSmsEmailMethod.notifyParent(": RINGING :"+incoming_number+"\nLocation"+result.get(0).getAddressLine(0), myRefr); 		
            	}
              };
              GL.execute();
    	
           // gpsProcess list =new gpsProcess(Void,Void,,listlocation);             
              return "";
               	
             }else{
              gps.showSettingsAlert();
          }
        }catch(Exception ex){ex.printStackTrace();}
        	
    	return "";
    	
    }
    public void onReceive(Context context, Intent intent) {
    	try{
    	
    	myRefr = context;
    	LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    	
    	ContactBean bean = MainActivity.db.getContactBean();
    	
        String name = bean.getName();
        String status = bean.getStatus();

        if(status != null && status.equals("start")){

            if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){                        

                    incomingFlag = false;

                    String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);        
                         
                    Log.v(TAG, "call OUT:"+phoneNumber);
                    
                   String loc=getlocation1(context);
                    String str_loc = loc.substring(loc.indexOf('\"'), loc.lastIndexOf('\"') );
                  
                    //send message to parent number
        
                   // sendSmsEmailMethod.notifyParent(name + ": call OUT:"+phoneNumber, context);
                    
                    System.out.println("call OUT:"+phoneNumber);

                    sendSmsEmailMethod.notifyParent(name + ": call OUT:"+phoneNumber+"\nLocation:"+loc, context);
                     
                    System.out.println("call OUT:"+phoneNumber+" Location"+loc);

            }else{                        

                    
            TelephonyManager tm =(TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);                        

                    switch (tm.getCallState()) {
                    		case TelephonyManager.CALL_STATE_RINGING:

                            incomingFlag = true;

                            incoming_number = intent.getStringExtra("incoming_number");
                            
                          //send message to parent number
                            getlocation1(context);
                        	
                            break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:                                

                            if(incomingFlag){

                                  //send message to parent number
                                sendSmsEmailMethod.notifyParent(name + ": incoming ACCEPT :"+ incoming_number, context);
                                         
                            }
                            break;
                    case TelephonyManager.CALL_STATE_IDLE:                                

                            if(incomingFlag){

                                    Log.v(TAG, "incoming IDLE");                                
        
                            }

                            break;

                    } 

            }
        }
    }catch(Exception ex){ex.printStackTrace();}
    }//end of method
}
    

