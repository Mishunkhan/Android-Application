package com.example.s360680s349520;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.s360680s349520.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private ActivityMainBinding binding;
    private DatabaseHjelper db;
    private List<Steder> stederList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Retrieve latitude and longitude data from the database
        db = new DatabaseHjelper(this);
        stederList = db.listSteder();
        // Add markers to the map
        //addMarkersToMap(stederList);
    }



    private void addMarkersToMap(List<Steder> stederList) {
        for (Steder sted : stederList) {
            try {
                double latitude = Double.parseDouble(sted.getLatidtude());
                double longitude = Double.parseDouble(sted.getLongitude());
                LatLng location = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(location).title(sted.getBeskrivelse())
                        .snippet(sted.getGateadresse()));
            } catch (NumberFormatException e) {
                // Handle parsing errors if latitude or longitude is not a valid double
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng oslo = new LatLng(59.919571, 10.735560);
        mMap.setMinZoomPreference(15);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(59.919571, 10.735560), 17));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(oslo));
        if (!stederList.isEmpty()) {
            addMarkersToMap(stederList);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(oslo));
        }
        mMap.setOnMapClickListener(this);


    }

    @Override
    public void onMapClick(LatLng latLng) {
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat("latitude", (float) latLng.latitude);
        editor.putFloat("longitude", (float)latLng.longitude);
        editor.apply();
        Log.d("SharedPrefs sent", "Latitude: " + latLng.latitude + ", longitude : " + latLng.longitude);

        // Launch another activity to add a new Steder object
        Intent intent = new Intent(MainActivity.this, LeggTilStedActivity.class);
        startActivity(intent);
    }
    public void onBackPressed() {
        // Custom back button logic (if needed)
        // For example, go back to a specific activity
        finish();
    }
}