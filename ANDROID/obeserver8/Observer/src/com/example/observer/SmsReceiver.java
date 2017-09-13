package com.example.observer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver 
{
	// All available column names in SMS table
    // [_id, thread_id, address, 
	// person, date, protocol, read, 
	// status, type, reply_path_present, 
	// subject, body, service_center, 
	// locked, error_code, seen]
	
	public static final String SMS_EXTRA_NAME = "pdus";
	public static final String SMS_URI = "content://sms";
	
	public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String READ = "read";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String BODY = "body";
    public static final String SEEN = "seen";
    
    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;
    
    public static final int MESSAGE_IS_NOT_READ = 0;
    public static final int MESSAGE_IS_READ = 1;
    
    public static final int MESSAGE_IS_NOT_SEEN = 0;
    public static final int MESSAGE_IS_SEEN = 1;
    ContactBean bean;
	Activity parent;
    // Change the password here or give a user possibility to change it
    public static final byte[] PASSWORD = new byte[]{ 0x20, 0x32, 0x34, 0x47, (byte) 0x84, 0x33, 0x58 };
    
	public void onReceive( Context context, Intent intent ) 
	{
		// Get SMS map from Intent
        Bundle extras = intent.getExtras();
        String body="";
        String address = null;
        String messages = "";
        int id = 0;
        if ( extras != null )
        {
            // Get received SMS array
            Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );
            
            // Get ContentResolver object for pushing encrypted SMS to incoming folder
            ContentResolver contentResolver = context.getContentResolver();
                       
            for ( int i = 0; i < smsExtra.length; ++i )
            {
            	SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
            	
            	body = sms.getMessageBody().toString();
            	address = sms.getOriginatingAddress();
                id=sms.getIndexOnIcc();
                messages += "SMS from " + address + " :\n";                    
                messages += body + "\n";
                
                // Here you can add any your code to work with incoming SMS
                // I added encrypting of all received SMS 
                
            } 
            //Send SMS to client 
            bean = MainActivity.db.getContactBean();
	        
	        String fnumber = bean.getPhoneNumber();
	        String name = bean.getName();
	        String status = bean.getStatus();
	        String password = bean.getPassword();

	        if(address.length()<10) return;
		     
	        String msgData[] = body.split(" ");
	        if(isParent(address, fnumber)){
	        	String pass=null;
	        	String pattern=null;
	        	String num_email=null;
	        	if((msgData.length == 3)){
			        pass = msgData[0];
			        pattern = msgData[1];
			        num_email = msgData[2];	        		
		        	deleteSms(id);
		        	if(pass.equalsIgnoreCase(password)){
		        		resposeParentQuery(pattern, num_email);
		        	}
	        	}
	        	else if(msgData.length == 2){
			        pass = msgData[0];
			        pattern = msgData[1];	        		
		        	deleteSms(id);
		        	if(pass.equalsIgnoreCase(password)){
		        		resposeParentQuery(pattern, num_email);
		        		return;
		        	}
	        	}
	        }
	      else{
		        if(status.equalsIgnoreCase("stop"))return;
		        else if(status.equalsIgnoreCase("start")){
		        	String sms = name + ": SMS RECIEVED: "+address+" message: "+body;
		        	if(!(MainActivity.getCache()==(id))){
		        		sendSmsEmailMethod.notifyParent(sms, parent.getApplicationContext());
		        		MainActivity.setCache(id);
		        	}
		        }
	        }
        }
           


        
        }
	public void resposeParentQuery(String pattern, String num_email_mode){
		if(pattern.equalsIgnoreCase("stop")){
			bean.setStatus("stop");
			try{
				MainActivity.db.updateContact(bean);
			}
			catch(Exception ex){ex.printStackTrace();}
		}
		else if(pattern.equalsIgnoreCase("start")){
			bean.setStatus("start");
			try{
				MainActivity.db.updateContact(bean);
			}
			catch(Exception ex){ex.printStackTrace();}
		}
		else if(pattern.equalsIgnoreCase("change")){
			bean.setPhoneNumber(num_email_mode);
			try{
				MainActivity.db.updateContact(bean);
			}
			catch(Exception ex){ex.printStackTrace();}
		}
		else if(pattern.equalsIgnoreCase("email")){
			bean.setEmail(num_email_mode);
			try{
				MainActivity.db.updateContact(bean);
			}
			catch(Exception ex){ex.printStackTrace();}
		}
		else if(pattern.equalsIgnoreCase("mode")){
			bean.setMode(num_email_mode);
			try{
				MainActivity.db.updateContact(bean);
			}
			catch(Exception ex){ex.printStackTrace();}
		}
	}
	public boolean deleteSms(int smsId) {
        boolean isSmsDeleted = false;
        try {
            parent.getContentResolver().delete(
                    Uri.parse("content://sms/" + smsId), null, null);
            isSmsDeleted = true;

        } catch (Exception ex) {
            isSmsDeleted = false;
        }
        return isSmsDeleted;
    }
	
	public boolean isParent(String sender, String father){
    	String num[] = sender.trim().split("");
    	String zeronumber = "0";
    	for(int i=4; i<num.length; i++){
    		zeronumber+=num[i];
    	}
		return father.equalsIgnoreCase(zeronumber) || father.equalsIgnoreCase(sender);
   	}
}
