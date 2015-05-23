package com.app.LocationFinder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class search extends AsyncTask<String,String ,JSONObject> {

	InputStream is;
	JSONObject jObj;
	String json = "" , name;
	Context context;
	double latitude=0.00 , longitude = 0.00;
	private ProgressDialog pd;	

	public search(MapActivity mapActivity, double latitudeE7, double longitudeE7, String name2) {
		context = mapActivity;
		latitude = latitudeE7;
		longitude = longitudeE7;
		name = name2;
		
		pd = new ProgressDialog(context);
		pd.show();
		pd.setMessage("Loading");
		latitude =Double.parseDouble(new DecimalFormat("##.##").format(latitude));
		longitude =Double.parseDouble(new DecimalFormat("##.##").format(longitude));
	//  Toast.makeText(context, "data " + latitude + " , " + longitude + ", " + name,  Toast.LENGTH_LONG).show();
	}

	
	@Override
	protected JSONObject doInBackground(String... params) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			//String url = "https://maps.googleapis.com/maps/api/place/search/json?location=26.89,75.81&radius=500&types=food&sensor=true&key=AIzaSyCEqN42I0Y8oCa3cKeLF5JqG7eypNWr_ek";
			//AIzaSyCcqyuEqdge2OfBxpWfORo_kXVcBRJyaLs";
			String url = "https://maps.googleapis.com/maps/api/place/search/json?location="+latitude+","+longitude+"&radius=500&types="+name+"&sensor=true&key=AIzaSyAYVSuGIJVsx0qNnqLdwzLCqdokACadTv8";
					//AIzaSyCEqN42I0Y8oCa3cKeLF5JqG7eypNWr_ek";
			//AIzaSyCcqyuEqdge2OfBxpWfORo_kXVcBRJyaLs";
			HttpPost httpPost = new HttpPost(url);
			//"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.8670522,35.1957362&radius=500&types=food&sensor=false&key=AIzaSyCcqyuEqdge2OfBxpWfORo_kXVcBRJyaLs");

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();          
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return jObj;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);

		Log.e("JSON Parser", "Error data " + result);
		String results = result.toString();
		parser(results);
	}

	private void parser(String data) {
		Intent i = new Intent(context.getApplicationContext() , Ballon.class);
		i.putExtra("data", data);
		context.startActivity(i);
		pd.cancel();
	}
}
