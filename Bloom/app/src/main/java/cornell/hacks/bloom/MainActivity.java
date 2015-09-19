package cornell.hacks.bloom;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

//Big Red Hacks 2015
//Authors: Jimmy Stoyell, Yuqi Zhao, Han Li, Jesse Lupica
import android.app.Activity;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_activity_viewpager)
    ViewPager viewPager;
    GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(fm);
        // Here you would declare which page to visit on creation
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);



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
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0)
                return ProfileFragment.newInstance();
            else
                return CircleFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            String latitude = "Latitude: " + loc.getLatitude();

//        /*------- To get city name from coordinates -------- */
//            String cityName = null;
//            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//            List<Address> addresses;
//            try {
//                addresses = gcd.getFromLocation(loc.getLatitude(),
//                        loc.getLongitude(), 1);
//                if (addresses.size() > 0)
//                    System.out.println(addresses.get(0).getLocality());
//                cityName = addresses.get(0).getLocality();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
//                    + cityName;
//            editLocation.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}
