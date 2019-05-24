package com.example.speechrecognizer;

public class Bon {

    String valoare;
    String nume;
    String data;
    String comment;


    public Bon(String valoare, String nume, String data,String comment) {
        this.valoare = valoare;
        this.nume = nume;
        this.data = data;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
