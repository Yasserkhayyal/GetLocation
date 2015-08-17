package com.example.mohamedyasser.getlocation;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    AsyncTask<Double, Void, String> getAddress;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni == null) {
            if (latitude != 0.0 && longitude != 0.0) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:<" + latitude + ">," +
                                "<" + longitude + ">" +
                                "?q=<" + latitude + ">," +
                                "<" + longitude + ">"));

                startActivity(intent);
            }
        }else{
                getAddress = new GetAddress().execute(latitude,longitude);
        }
            finish();
        }




    public class GetAddress extends AsyncTask<Double,Void,String>{
        double latitude;
        double longitude;
        @Override
        protected String doInBackground(Double... params) {
            latitude = params[0];
            longitude = params[1];
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            Address address;
            String result = null;
            List<Address> list = null;
            try {
                list = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                Log.e(LOG_TAG,e.getMessage(),e);
            }

            address = list.get(0);
            result = address.getAddressLine(0) + ", " + address.getAddressLine(1);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String address = s;
            if(latitude!=0.0 && longitude!=0.0) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("geo:<" + latitude  + ">," +
                                        "<" + longitude + ">" +
                                        "?q=<" + latitude  + ">," +
                                        "<" + longitude + ">" +
                                        "(" + address + ")"));

                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
