package cornell.hacks.bloom;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Use the {@link CircleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CircleFragment extends Fragment {


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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location loc) {
//                Toast.makeText(
//                        getBaseContext(),
//                        "Location changed: Lat: " + loc.getLatitude() + " Lng: "
//                                + loc.getLongitude(), Toast.LENGTH_SHORT).show();
//                String longitude = "Longitude: " + loc.getLongitude();
//                String latitude = "Latitude: " + loc.getLatitude();
                getLastBestLocation();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                displayToast("GPS Enabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                displayToast("GPS Disabled");
            }
        };

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);

        View v = inflater.inflate(R.layout.fragment_circle, container, false);
        FrameLayout layoutContainer = (FrameLayout) v.findViewById(R.id.fragment_circle_container);

        layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastBestLocation();
            }
        });
        DrawCircle x = new DrawCircle(getActivity());
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
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }



}
