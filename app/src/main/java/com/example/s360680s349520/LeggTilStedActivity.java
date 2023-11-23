package com.example.s360680s349520;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

public class LeggTilStedActivity extends AppCompatActivity {
    private DatabaseHjelper db;
    Steder nyttSted = new Steder();
    EditText editTextBeskrivelseValue;
    EditText editTextAdresseValue;
    TextView  textViewLatValue,textViewLongValue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leggtilsted);
        db = new DatabaseHjelper(this); // You need to replace this with your actual constructor


        // Retrieve LatLng from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        float latitude = sharedPreferences.getFloat("latitude", 0);
        float longitude = sharedPreferences.getFloat("longitude", 0);
        Log.d("SharedPrefs recieved", "latitude: " + latitude + ", longitude: " + longitude);

        LatLng location = new LatLng(latitude, longitude);
/*
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

*/



        nyttSted.setLatidtude(String.valueOf(latitude));
        nyttSted.setLongitude(String.valueOf(longitude));
        EditText editTextBeskrivelseValue = findViewById(R.id.editTextBeskrivelseValue);
        EditText editTextAdresseValue = findViewById(R.id.editTextAdresseValue);
        TextView textViewLatValue= findViewById(R.id.textViewLatValue);
        TextView textViewLongValue= findViewById(R.id.textViewLongValue);
        textViewLatValue.setText(nyttSted.getLatidtude());
        textViewLongValue.setText(nyttSted.getLongitude());


        Button buttonNo = findViewById(R.id.buttonNo);
        Button buttonYes = findViewById(R.id.buttonYes);


        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LeggTilStedActivity.this, "You clicked No", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LeggTilStedActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String besk = editTextBeskrivelseValue.getText().toString();
                String addr = editTextAdresseValue.getText().toString();
                nyttSted.setBeskrivelse(besk);
                nyttSted.setGateadresse(addr);

                if (besk.isEmpty() || addr.isEmpty()) {
                    Toast.makeText(LeggTilStedActivity.this, "Please enter description and address", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Handle "Yes" button click (e.g., add to the database)
                db.leggTilSteder(nyttSted);
                Intent intent = new Intent(LeggTilStedActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close this activity and go back to MainActivity
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
