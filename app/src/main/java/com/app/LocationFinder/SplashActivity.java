package com.app.LocationFinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.Toast;

import com.app.searchlocation.R;

public class SplashActivity extends Activity {

	private GPSTracker gps;
	private double latitude , longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		gps = new GPSTracker(SplashActivity.this);
		if(gps.canGetLocation()){
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			//Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();   
			
				 			
			if(latitude >1){
			Handler handler = new Handler(); 
		    handler.postDelayed(new Runnable() { 
		         public void run() { 
		        	 Intent i = new Intent(SplashActivity.this , MapActivity.class);
		 			i.putExtra("latitude", latitude);
		 			i.putExtra("longitude",longitude);
		 			startActivity(i);
		 			finish(); 
		         } 
		    }, 3000);
			}
			else{
				Toast.makeText(getApplicationContext(), "Location Not Found" , Toast.LENGTH_LONG).show();
				gps.showSettingsAlert();
			}
			
		}
		
		else{
			gps.showSettingsAlert();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
