package com.example.observer;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.Toast;

public class SmsSender {

	 public static void sendSMS(EditText phoneNumber, EditText message,Context c) {
	        //String phoneNo = "0123456789";
	        //String message = "Hello World!";

	        SmsManager smsManager = SmsManager.getDefault();
	        smsManager.sendTextMessage(phoneNumber.getText().toString(), null, message.getText().toString(), null, null);
	        
	        Toast.makeText(c, "Message Sent!", Toast.LENGTH_LONG).show();
	    }


	    public  static void sendLongSMS(EditText phoneNumber, EditText message,Context c ) {    	 
	        //String phoneNo = "0123456789";
	        //String message = "Hello World! Now we are going to demonstrate how to send a message with more than 160 characters from your Android application.";

	        SmsManager smsManager = SmsManager.getDefault();
	        ArrayList<String> parts = smsManager.divideMessage(message.getText().toString()); 
	        smsManager.sendMultipartTextMessage(phoneNumber.getText().toString(), null, parts, null, null);
	        
	        Toast.makeText(c, "Message Sent!", Toast.LENGTH_LONG).show();
	    }
	    
	    public  static void sendLongSMS(String phoneNumber, String message) {    	 
	        //String phoneNo = "0123456789";
	        //String message = "Hello World! Now we are going to demonstrate how to send a message with more than 160 characters from your Android application.";

	       
	    	SmsManager smsManager = SmsManager.getDefault();
	        ArrayList<String> parts = smsManager.divideMessage(message); 
	        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
	        
	    }

	    
	    public  static void saveInSent(EditText message,EditText phoneNumber,ContentResolver c) {
	    	ContentValues values = new ContentValues(); 
	        
	        values.put("address", phoneNumber.getText().toString()); 
	                  
	        values.put("body", message.getText().toString()); 
	             
	        c.insert(Uri.parse("content://sms/sent"), values);
	    }        
	    
}
