package com.app.LocationFinder;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.searchlocation.R;
import com.google.android.gms.ads.AdSize;
import android.view.Gravity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends Activity implements OnClickListener{

	private double lat , lon ;
	private Button btn_search;
	private Spinner spinner_items;
	private String name ;
	String[] mPlaceType=null;
	String[] mPlaceTypeName=null;

	private GoogleMap googleMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(MapActivity.this);
		adView.setAdUnitId("ca-app-pub-1878227272753934/9967708800");
		adView.setAdSize(AdSize.BANNER);
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.addshows);        
		layout.addView(adView);
		layout.setGravity(Gravity.CENTER);
		com.google.android.gms.ads.AdRequest request = new com.google.android.gms.ads.AdRequest.Builder().build();
		adView.loadAd(request);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {			
			lat = extras.getDouble("latitude");
			lon = extras.getDouble("longitude");

		}

		btn_search = (Button) findViewById(R.id.button_search);
		btn_search.setOnClickListener(this);

		spinner_items = (Spinner) findViewById(R.id.spinner_items);

		mPlaceType = getResources().getStringArray(R.array.place_type);
		mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mPlaceTypeName);
		spinner_items.setAdapter(adapter);

		try {
			// Loading map
			initilizeMap();

			// Changing map type
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

			// Showing / hiding your current location
			googleMap.setMyLocationEnabled(true);

			// Enable / Disable zooming controls
			googleMap.getUiSettings().setZoomControlsEnabled(false);

			// Enable / Disable my location button
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			// Enable / Disable Compass icon
			googleMap.getUiSettings().setCompassEnabled(true);

			// Enable / Disable Rotate gesture
			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			// Enable / Disable zooming functionality
			googleMap.getUiSettings().setZoomGesturesEnabled(true);

			double[] randomLocation = createRandLocation(lat,lon);


			CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(new LatLng(
					lat,lon)).zoom(15).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/*
	 * creating random postion around a location for testing purpose only
	 */
	private double[] createRandLocation(double latitude, double longitude) {

		return new double[] { latitude + ((Math.random() - 0.5) / 500),
				longitude + ((Math.random() - 0.5) / 500),
				150 + ((Math.random() - 0.5) * 10) };

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_search:

			int selectedPosition = spinner_items.getSelectedItemPosition();
			name = mPlaceType[selectedPosition];
			//Toast.makeText(getApplicationContext(), "data " + lat + " , " + lon, Toast.LENGTH_LONG).show();
			new search(MapActivity.this, lat , lon , name).execute();
			break;
		default:
			break;
		}		
	}

}