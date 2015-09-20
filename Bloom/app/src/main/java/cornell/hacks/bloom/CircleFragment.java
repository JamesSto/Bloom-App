package cornell.hacks.bloom;


import android.app.ActionBar;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Use the {@link CircleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CircleFragment extends Fragment implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    public static int movingintensity = 20;

    //REFERENCE LOCATION for testing
    private Location refLoc = new Location("");

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    String TAG = "ACTIVITY MAIN";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private double distanceTo;  //Distance in meters to other phone
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CircleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CircleFragment newInstance() {
        CircleFragment fragment = new CircleFragment();
        return fragment;
    }

    public CircleFragment() {
        // Required empty public constructor
        //Reference Location Setup
        refLoc.setLatitude(42.4496769);
        refLoc.setLongitude(-76.4822992);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        createLocationRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_circle, container, false);
        FrameLayout layoutContainer = (FrameLayout) v.findViewById(R.id.fragment_circle_container);

        layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Location here = LocationServices.FusedLocationApi.getLastLocation(
//                        mGoogleApiClient);
//                if(here!= null)
//                    displayToast(here.getLatitude() + " " + here.getLongitude());
//                else
//                    displayToast("Not connected");
            }
        });
        
        DrawCircle x = new DrawCircle(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        x.setLayoutParams(lp);
        layoutContainer.addView(x);

        // Inflate the layout for this fragment
        return v;
    }
    private void getLastBestLocation() {
        Location here;
        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            here = locationGPS;
        }
        else {
            here =  locationNet;
        }
        String latitude = here.getLatitude()+"";
        String longitude = here.getLongitude()+"";
        displayToast(latitude + " " + longitude);
    }
    public void displayToast(String text)
    {
        if(text!=null) {
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("connected");
        startLocationUpdates();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        displayToast(mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());
    }
    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        displayToast(location.getLatitude() +" " + location.getLongitude());

        //Every Time location is changed, calculate distance between two locations and new intensity
        //Using Reference Location
        distanceTo = mCurrentLocation.distanceTo(refLoc);  //Change refLoc to location of second.
        movingintensity = GPSTracker.calcIntensity(distanceTo);
        System.out.println("DISTANCE TO: " + distanceTo);
        System.out.println("Intensity: " + movingintensity);
        System.out.println("Current Lat: " + mCurrentLocation.getLatitude());
        System.out.println("Current Long: " + mCurrentLocation.getLongitude());
        System.out.println("REF Lat: " + refLoc.getLatitude());
        System.out.println("REF Long: " + refLoc.getLongitude());

        String latString = String.valueOf(mCurrentLocation.getLatitude());
        String longString = String.valueOf(mCurrentLocation.getLongitude());
        new UpdateGPSData().execute(latString,longString);


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("connection failed");
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //new UpdateGPSData().execute("latitude", "logitude");
    }



    private class UpdateGPSData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... param) {
            try {
                System.out.println("http://10.128.13.169//update_location.php?User-Agent="+MainActivity.ident+
                        "&latitude=" + param[0]+ "&longitude=" + param[1]);
                HttpGet httppost = new HttpGet("http://10.128.13.169//update_location.php?User-Agent="+MainActivity.ident+
                        "&latitude=" + param[0]+ "&longitude=" + param[1]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    return data;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            System.out.println("EXECUTE");
            Toast.makeText(getActivity(), "POST EXEUTE", Toast.LENGTH_SHORT).show();
            System.out.println(result);
            String resultString = result;
            if (resultString.equals("") || !resultString.startsWith("(") || !resultString.endsWith(")")){
                //pass
                System.out.println("INPUT INVALID FROM SERVER.");
            }
            else{
                int commaIndex = resultString.indexOf(",");
                String latString = resultString.substring(1, commaIndex);
                String longString = resultString.substring(commaIndex + 1, resultString.indexOf(")"));
                refLoc.setLatitude(Double.parseDouble(latString));
                refLoc.setLongitude(Double.parseDouble(longString));
            }
        }



    }

}
