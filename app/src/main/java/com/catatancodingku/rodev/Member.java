package com.catatancodingku.rodev;

public class Member {

    private String user;
    private String email;
    private String judul;
    private String deskripsi;
    private String key;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getUser(){
        return user;
    }

    public void setUser(String value){
        this.user   =   value;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String value){
        this.email  =   value;
    }

    public Member(){

    }
    //account

    public Member(String user, String email){

        this.user   =   user;
        this.email  =   email;

    }

    // Task

    public Member(String user, String judul, String deskripsi, boolean status) {
        this.user = user;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.status = status;
    }


}
