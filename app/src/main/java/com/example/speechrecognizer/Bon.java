package com.example.speechrecognizer;

public class Bon {

    String valoare;
    String nume;
    String data;

    public Bon(String valoare, String nume, String data) {
        this.valoare = valoare;
        this.nume = nume;
        this.data = data;
    }



    public String getValoare() {
        return valoare;
    }

    public void setValoare(String valoare) {
        this.valoare = valoare;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
