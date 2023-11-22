package com.example.s360680s349520;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

public class LeggTilStedActivity extends AppCompatActivity {
    private DatabaseHjelper db;
    TextView textViewBeskrivelseValue;
    TextView textViewAdresseValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leggtilsted);

        // Retrieve LatLng from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        float latitude = sharedPreferences.getFloat("latitude", 0);
        float longitude = sharedPreferences.getFloat("longitude", 0);
        Log.d("SharedPrefs recieved", "latitude: " + latitude + ", longitude: " + longitude);

        LatLng location = new LatLng(latitude, longitude);

        // ... (previous code)

        // Use Geocoder to get address from LatLng
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String addr="";
        String besk="";

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);

                // Now you have the address details
                String addressLine = address.getAddressLine(0);
                String city = address.getLocality();
                String featureName = address.getFeatureName(); // Get the specific name of the location (if available)
                String locality = address.getLocality(); // Get the city or locality name

                besk = featureName != null ? featureName : locality;
                // ... (you can retrieve other address details as needed)
                // Create an object with the whole address
                addr = addressLine + ", " + city;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        Steder nyttSted = new Steder();
        nyttSted.setBeskrivelse(besk);
        nyttSted.setGateadresse(addr);
        nyttSted.setLatidtude(String.valueOf(latitude));
        nyttSted.setLongitude(String.valueOf(longitude));
        TextView textViewBeskrivelseValue = findViewById(R.id.textViewBeskrivelseValue);
        TextView textViewAdresseValue = findViewById(R.id.textViewAdresseValue);
        textViewBeskrivelseValue.setText(nyttSted.getBeskrivelse());
        textViewAdresseValue.setText(nyttSted.getGateadresse());

        Button buttonYes = findViewById(R.id.buttonYes);
        Button buttonNo = findViewById(R.id.buttonNo);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle "Yes" button click (e.g., add to the database)
                db.leggTilSteder(nyttSted);
                Intent intent = new Intent(LeggTilStedActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close this activity and go back to MainActivity
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeggTilStedActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }

    public void onBackPressed() {
        // Custom back button logic (if needed)
        // For example, go back to a specific activity
        finish();
        Intent intent = new Intent(LeggTilStedActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
