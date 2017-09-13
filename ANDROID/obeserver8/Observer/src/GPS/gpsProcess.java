package GPS;

import java.util.List;

import org.json.JSONObject;

import com.example.observer.CallReciever;

import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;

public class gpsProcess extends AsyncTask<Void,Void, List<Address>>{

	double Longitude;
 	double Latitude;
 	
 	//public static String location1="",location2="",location3="",location4="",location5="";
	public gpsProcess(double Longitude,double Latitude){
 		this.Longitude=Longitude;
 		this.Latitude=Latitude;
 	}
 	
	
	


		@Override
		protected List<Address> doInBackground(Void... arg0) {
			String str2 = "";
			  
			  try
		    {
				 
		       	 List<Address> list = getlocation.getStringFromLocation(Latitude, Longitude);
		       	 if(list.size()>0){
		       	 for(int i=0; i<list.size();i++){
		       		Log.e("location:",""+i+". "+ list.get(i));
		       	 }
		       	 }
		       	 
		            return list;
			
		    } catch(Exception ex){ex.printStackTrace();   }
			return null;
		}


}
