package com.example.s360680s349520;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class DatabaseHjelper extends SQLiteOpenHelper {
    static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAVN = "s360680";

    static ArrayList<Steder> alleSteder;

    public DatabaseHjelper(Context context) {
        super(context, DATABASE_NAVN, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
       // new getJson();
        initializeDatabase();
    }

    private void initializeDatabase() {
        // Add code to initialize your database tables if needed
        // ...

        // Call the method to fetch data from the server
        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        // Execute AsyncTask to fetch data from the server
        getJson task = new getJson();
        String[] send = {"https://dave3600.cs.oslomet.no/~s360680/jsonut.php", "GET"};

        try {
            alleSteder = task.execute(send).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oVer, int nVer){
    }
    private class getJson extends AsyncTask<String, Void,ArrayList<Steder>> {
        @Override
        protected  ArrayList<Steder> doInBackground(String... urls) {
            ArrayList <Steder> retur = new ArrayList<>();
            String s = "";
            String output = "";
            for (String url : urls) {
                try {
                    URL urlen = new URL(urls[0]);
                    HttpsURLConnection conn = (HttpsURLConnection)
                            urlen.openConnection();
                    conn.setRequestMethod(urls[1]);
                    conn.setRequestProperty("Accept","application/json");
                    if (urls[1].equals("POST")){
                        conn.setDoOutput(true);
                        try(OutputStream out = conn.getOutputStream()) {
                            byte[] input = urls[2].getBytes(StandardCharsets.UTF_8);
                            out.write(input, 0, input.length);
                        }
                    }
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code :"+
                                conn.getResponseCode());
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));
                    System.out.println("Output from Server .... \n");
                    while ((s = br.readLine()) != null) {
                        output = output + s;
                    }
                    conn.disconnect();
                    try {
                        JSONArray stedd = new JSONArray(output);
                        for (int i = 0; i < stedd.length(); i++) {
                            JSONObject jsonget = stedd.getJSONObject(i);
                            String id = jsonget.getString("id");
                            String beskrivelse = jsonget.getString("beskrivelse");
                            String gateadresse = jsonget.getString("gateadresse");
                            String latitude = jsonget.getString("gps_latitude");
                            String longitude = jsonget.getString("gps_longitude");
                            Steder sted= new Steder();
                            sted.setID(Integer.parseInt(id));
                            sted.setBeskrivelse(beskrivelse);
                            sted.setGateadresse(gateadresse);
                            sted.setLatidtude(latitude);
                            sted.setLongitude(longitude);
                            retur.add(sted);
                        }
                        return retur;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return retur;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return retur;
        }
        @Override
        protected void onPostExecute(ArrayList<Steder> steder) {
            alleSteder=steder;
        }
    }

    public void leggTilSteder(Steder sted){
        JSONObject json=new JSONObject();
        try {
            json.put("beskrivelse",sted.getBeskrivelse());
            json.put("gateadresse",sted.getGateadresse());
            json.put("gps_latitude",sted.getLatidtude());
            json.put("gps_longitude",sted.getLongitude());
        } catch (JSONException e){
            e.printStackTrace();
        }

        String jsonOut=json.toString();

        getJson i= new getJson();
        String[] send={"https://dave3600.cs.oslomet.no/~s360680/jsonin.php","POST",jsonOut};
        i.execute(send);
    }

    public List<Steder> listSteder() {
        getJson json = new getJson();
        String[] send = {"https://dave3600.cs.oslomet.no/~s360680/jsonut.php","GET"};

        try {
            return json.execute(send).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();

            return alleSteder;
        }
    }


}
