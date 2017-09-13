package com.example.observer;

import java.util.List;

//import GmailSender.GMailSender;

import GmailSender.GMailSender;
import android.content.Context;
import android.widget.Toast;

public  class sendSmsEmailMethod {

	/**
	 * @deprecated Use {@link #notifyParent(String,Context)} instead
	 */
	public static void sendSmSParnetNumber(String message,Context context){
		notifyParent(message, context);
	}

	public static void notifyParent(String message,Context context){
		try{
			
			List<ContactBean> cbList= (List<ContactBean>) MainActivity.db.getAllContacts();
			ContactBean cb = cbList.get(0);
//	    	Toast.makeText(context, "contact bean. . ." , Toast.LENGTH_LONG).show();	 
			String mode = cb.getMode();
			String[] email = {cb.getEmail()};
	    	Toast.makeText(context, "mode: "+mode , Toast.LENGTH_LONG).show();	 			
			if(mode != null && mode.equalsIgnoreCase("sms")){
				sendSms(message, context);
			}
			else if(cb.getMode().equalsIgnoreCase("email") && email !=null){
				sendEmail("Message Notifaction",message, email, context);	
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void sendSms(String message,Context context)throws Exception{
	   	  List<ContactBean> l = MainActivity.db.getAllContacts();
	         if(l.size()>0){
	          SmsSender.sendLongSMS(l.get(0).getPhoneNumber(), message);
	         }
	}
	
	public static void sendEmail(String subject,String body,String[] email, Context context){
		
  	try {
  		//GMailSender mailsender = new GMailSender("sender@gmail.com", "password123");
  		GMailSender mailsender = new GMailSender("perfectmaster123@gmail.com", "perfectmaster");
    
   		// String[] toArr = { "receiver1@gmail.com", "receiver2@gmail.com" };
  		//String[] toArr  = { "raja21068@facebook.com", "jay_tharwani1992@yahoo.com" };
    
       mailsender.set_to(email);
       mailsender.set_from("SMS & Call Tracking");
       mailsender.set_subject(subject);
       mailsender.setBody(body);

            //      mailsender.addAttachment("/sdcard/filelocation");

          if (mailsender.send()) {
              Toast.makeText(context,"Email was sent successfully.",Toast.LENGTH_LONG).show();
           } else {
              Toast.makeText(context, "Email was not sent.",Toast.LENGTH_LONG).show();
           }
       } catch (Exception e) {
           
           Toast.makeText(context,  "Could not send email", Toast.LENGTH_LONG).show();
       }
	}
	
}
