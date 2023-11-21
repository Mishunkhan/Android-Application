package com.example.s360680s349520;

public class Steder {
    int ID;
    String beskrivelse, gateadresse, latidtude, longitude;

    public Steder() {
    }

    public Steder(int ID, String beskrivelse,String gateadresse, String latidtude, String longitude)
    {
        this.ID=ID;
        this.beskrivelse=beskrivelse;
        this.gateadresse=gateadresse;
        this.latidtude=latidtude;
        this.longitude=longitude;
    }

    public Steder(String beskrivelse,String gateadresse, String latidtude, String longitude)
    {
        this.beskrivelse=beskrivelse;
        this.gateadresse=gateadresse;
        this.latidtude=latidtude;
        this.longitude=longitude;
    }


    public int getID() {
        return ID;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public String getGateadresse() {
        return gateadresse;
    }

    public String getLatidtude() {
        return latidtude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public void setGateadresse(String gateadresse) {
        this.gateadresse = gateadresse;
    }

    public void setLatidtude(String latidtude) {
        this.latidtude = latidtude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}