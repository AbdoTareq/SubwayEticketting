package com.abdotareq.subwaye_ticketting.model.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class User {

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("image")
    private byte[] image;

    @SerializedName("birth_date")
    private String birth_date;

    @SerializedName("gender")
    private String gender;

    @SerializedName("admin")
    private int admin;

    @SerializedName("otp_token")
    private String otp_token;


    public User(String email, String password, int admin) {
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public User(String email, String otp_token) {
        this.email = email;
        this.otp_token = otp_token;
    }

    public String getOtp_token() {
        return otp_token;
    }

    public void setOtp_token(String otp_token) {
        this.otp_token = otp_token;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", password=" + password + ", first_name=" + first_name
                + ", last_name=" + last_name + ", image=" + Arrays.toString(image) + ", birth_date=" + birth_date + ", gender="
                + gender + "]";
    }


    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getBirth_date() {
        return birth_date;
    }
}
