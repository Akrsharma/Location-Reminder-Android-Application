package com.example.avadhesh.alarmyourposition;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;


public class GoogleeMap extends FragmentActivity implements OnMapReadyCallback, OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener, PlaceSelectionListener {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private LocremainDBHelper remdbhelper;
    private Bookmarks bm;
    private GoogleMapOptions options;
    EditText et;
    AlertDialog.Builder ab;
    double lattitude,longitute;
    Geocoder geo;
    String strreturnaddress,addresssearch;
    LatLng latlongsearch;
    double latsearch,longsearch;
    int j;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlee_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        et=new EditText(this);
        remdbhelper = new LocremainDBHelper(this);
        bm = new Bookmarks(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        mapFragment.getMapAsync(this);
        flag=0;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Delhi and move the camera
        ab = new AlertDialog.Builder(this);
        LocationManager lm=(LocationManager)getSystemService(LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location myLocation = null;
        for (String provider : providers) {
            Location l = lm.getLastKnownLocation(provider);
            Log.d("last known l and p", " "+provider+ " "+l);

            if (l == null) {
                continue;
            }
            if (myLocation == null
                    || l.getAccuracy() < myLocation.getAccuracy()) {
                Log.d("last known l: %s", " "+l);
                myLocation = l;
            }
        }
        LatLng current = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(current).title("Marker in Delhi"));
//        CameraPosition.Builder cameraPosition = new CameraPosition.Builder().zoom(17);
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition.build()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        // Sets the map type to be "hybrid"
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        options = new GoogleMapOptions();
//        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
//                .compassEnabled(false)
//                .rotateGesturesEnabled(false)
//                .tiltGesturesEnabled(false);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnCameraChangeListener(this);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
//        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in tapped position"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        lattitude=latLng.latitude;
        longitute=latLng.longitude;
        geo=new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addr = geo.getFromLocation(lattitude, longitute, 1);
            if(addr!=null){
                strreturnaddress=new String("");
                if(addr.get(0).getSubLocality()!=null)
                    strreturnaddress+=addr.get(0).getSubLocality()+" ";
                if(addr.get(0).getSubAdminArea()!=null)
                    strreturnaddress+=addr.get(0).getSubAdminArea()+" ";
                if(addr.get(0).getLocality()!=null)
                    strreturnaddress+=addr.get(0).getLocality()+" ";
                if(addr.get(0).getAdminArea()!=null)
                    strreturnaddress+=addr.get(0).getAdminArea()+" ";
                if(addr.get(0).getCountryName()!=null)
                    strreturnaddress+=addr.get(0).getCountryName()+" ";
                if(addr.get(0).getPostalCode()!=null)
                    strreturnaddress+=addr.get(0).getPostalCode()+" ";
            }
            else {
                strreturnaddress="There was some error. \n try again";
            }
        }
        catch(Exception ex){
//            System.out.println()
        }
        flag=0;
        ab.setTitle("Bookmark");
        ab.setMessage("Do you want to bookmark this location");
        MyListener ml=new MyListener();
        ab.setPositiveButton("Add", ml);
        ab.setNegativeButton("Cancel", ml);
        ab.show();
    }

    @Override
    public void onPlaceSelected(Place place) {
        flag=1;
        Log.d("place", "" + place.getName());
        ab.setTitle("Bookmark");
        ab.setMessage("Do you want to bookmark this location");
        latlongsearch = place.getLatLng();
        latsearch = latlongsearch.latitude;
        longsearch = latlongsearch.longitude;
        addresssearch = (String)place.getName();
        MyListener ml=new MyListener();
        ab.setPositiveButton("Add", ml);
        ab.setNegativeButton("Cancel", ml);
        ab.show();
    }

    @Override
    public void onError(Status status) {

    }
    class MyListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            long id;
            try {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if(flag==1) {
                            id = bm.insertBookmark("" + latsearch, "" + longsearch, addresssearch);
                            Log.d("remainidis"," "+id);
                        }
                        else{
                            id = bm.insertBookmark("" + lattitude, "" + longitute, strreturnaddress);
                            Log.d("remainidis"," "+id);
                        }
                        addReminder();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        addReminder();
                        break;
                }
//                if(flag==1){
//                    id = remdbhelper.insertRemainder("" + latsearch, "" + longsearch,addresssearch , "Home", et.getText().toString());
//                }
//                else {
//                    id = remdbhelper.insertRemainder("" + lattitude, "" + longitute, strreturnaddress, "Home", et.getText().toString());
//                }
//                System.out.println("id is "+id);
//                Log.d("remainderreturn ","the id of remainder is "+id);
//                Toast.makeText(GoogleeMap.this, "Your Remainder has been saved successfully ", Toast.LENGTH_LONG).show();
//                Intent data = new Intent();
//                data.setData(Uri.parse(""+id));
//                setResult(RESULT_OK, data);
//                finish();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
   public void  addReminder(){
        ab.setTitle("Add a Reminder");
        if(flag==0)
        ab.setMessage(strreturnaddress);
        else
        ab.setMessage(addresssearch);
        ab.setView(et);
        MyListener1 ml1=new MyListener1();
        ab.setPositiveButton("Add", ml1);
        ab.show();
    }
    class MyListener1 implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            long id;
            try {
                if(flag==1){
                    id = remdbhelper.insertRemainder("" + latsearch, "" + longsearch,addresssearch , "Home", et.getText().toString());
                }
                else {
                    id = remdbhelper.insertRemainder("" + lattitude, "" + longitute, strreturnaddress, "Home", et.getText().toString());
                }
                System.out.println("id is "+id);
                Log.d("remainderreturn ","the id of reminder is "+id);
                Toast.makeText(GoogleeMap.this, "Your Reminder has been saved successfully ", Toast.LENGTH_LONG).show();
                Intent data = new Intent();
                data.setData(Uri.parse(""+id));
                setResult(RESULT_OK, data);
                finish();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}
