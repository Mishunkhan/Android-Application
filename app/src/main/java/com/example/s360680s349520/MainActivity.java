package com.example.s360680s349520;


import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.s360680s349520.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMainBinding binding;
    private DatabaseHjelper db;
    private List<Steder> stederList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
                mMap.addMarker(new MarkerOptions().position(location).title(sted.getBeskrivelse()));
            } catch (NumberFormatException e) {
                // Handle parsing errors if latitude or longitude is not a valid double
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addMarkersToMap(stederList);

        LatLng oslo = new LatLng(59.919571, 10.735560);
        mMap.addMarker(new
                MarkerOptions().position(oslo).title("Marker in Oslo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(oslo));
        if (!stederList.isEmpty()) {
            mMap.addMarker(new
                    MarkerOptions().position(oslo).title("Marker in Oslo"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(oslo));
        }
    }
}