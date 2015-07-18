package com.app.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.searchlocation.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import android.view.Gravity;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Ballon extends Activity {

	private Double latitude , longitude;
	private int latitudeE7;
	private int longitudeE7;
	String[] mPlaceType=null;
	String arrayData;
	private GoogleMap googleMap;


	InterstitialAd interstitial;
	com.google.android.gms.ads.AdRequest adRequest1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_ballon);

		interstitial = new InterstitialAd(Ballon.this);
		interstitial.setAdUnitId("ca-app-pub-1878227272753934/9828108009");
		adRequest1 = new com.google.android.gms.ads.AdRequest.Builder()
				.build();


		Bundle extras = getIntent().getExtras();
		if (extras != null) {			
			arrayData = extras.getString("data");
		}
		
		
		initViews();


	}


	/*public void displayInterstitial() {

		// If Ads are loaded, show Interstitial else show nothing.
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}*/
	private void initViews() {

		com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(Ballon.this);
	    adView.setAdUnitId("ca-app-pub-1878227272753934/8351374804");
	    adView.setAdSize(AdSize.BANNER);
	    RelativeLayout layout = (RelativeLayout)findViewById(R.id.addsho);        
	    layout.addView(adView);
	    layout.setGravity(Gravity.CENTER);
	    com.google.android.gms.ads.AdRequest request = new com.google.android.gms.ads.AdRequest.Builder().build();
	    adView.loadAd(request);
	    
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

		
	    
	    
		parseData(arrayData);
		
	}
	
	private void parseData(String arrayData2) 
	{
		JSONObject json;
		try {
			json = new JSONObject(arrayData2);
			
			if(json.has("results")){
				JSONArray arr = json.getJSONArray("results");
				if(arr.length() > 0 ){
				for(int result=0 ; arr.length()>0 ; result++){
					
				
					JSONObject oneObject = arr.getJSONObject(result);
					JSONObject geometry = oneObject.getJSONObject("geometry");
					JSONObject location = geometry.getJSONObject("location");
					String name = oneObject.getString("name");
					String vicinity = oneObject.getString("vicinity");
					String lat = location.getString("lat");
					String lang = location.getString("lng");
					
					latitude = Double.valueOf(lat);
					longitude = Double.valueOf(lang);
					
					latitudeE7 = (int) (latitude * 1000000);
					longitudeE7 = (int) (longitude * 1000000);
					
					Log.v("ca" , ""+latitudeE7 +","+ longitudeE7 + ",name " + name);
					
					//MapActivity(latitudeE7 , longitudeE7 , name , vicinity);
					
					double[] randomLocation = createRandLocation(latitude,
							longitude);

					// Adding a marker
					MarkerOptions marker = new MarkerOptions().position(
							new LatLng(randomLocation[0], randomLocation[1]))
							.title(name + ", " + vicinity);
					
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
					
					googleMap.addMarker(marker);
					
					CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(
							latitude,longitude)).zoom(15).build();

					googleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));



					/*new CountDownTimer(1000,5000){

						@Override
						public void onTick(long millisUntilFinished) {

						}

						@Override
						public void onFinish() {

							interstitial.loadAd(adRequest1);
							// Prepare an Interstitial Ad Listener
							interstitial.setAdListener(new AdListener() {
								public void onAdLoaded() {
									displayInterstitial();

								}
							});

						}
					}.start();*/


				}
				}
				else{
					Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
					finish();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}
	
	/*@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	private void MapActivity( int latitude , int longitude , String name , String vicinity) {
		mapView.setBuiltInZoomControls(true);
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.pin);
		CustomItemizedOverlay itemizedOverlay = new CustomItemizedOverlay(drawable, this);
		GeoPoint point = new GeoPoint(latitude, longitude);
		OverlayItem overlayitem = new OverlayItem(point, name , vicinity);
		itemizedOverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedOverlay);
		MapController mapController = mapView.getController();
		mapController.animateTo(point);
		mapController.setZoom(15);
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay.enableMyLocation();
		mapOverlays.add(myLocationOverlay);
	}*/

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	/**
	 * function to load map If map is not created it will create it for you
	 * */
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
}
