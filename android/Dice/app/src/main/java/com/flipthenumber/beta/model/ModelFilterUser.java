package com.flipthenumber.beta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelFilterUser {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("player_id")
        @Expose
        private Integer userId;

        @SerializedName("player_name")
        @Expose
        private String name;

        @SerializedName("player_email")
        @Expose
        private String email;




        @SerializedName("player_dob")
        @Expose
        private String dob;

        @SerializedName("player_gender")
        @Expose
        private String gender;



        @SerializedName("player_image")
        @Expose
        private String image;

        @SerializedName("player_countryCode")
        @Expose
        private String countryCode;


        @SerializedName("player_mobile")
        @Expose
        private String mobile;

        @SerializedName("player_total_token")
        @Expose
        private String total_token;

        @SerializedName("player_total_joker")
        @Expose
        private String total_joker;


        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTotal_token() {
            return total_token;
        }

        public void setTotal_token(String total_token) {
            this.total_token = total_token;
        }

        public String getTotal_joker() {
            return total_joker;
        }

        public void setTotal_joker(String total_joker) {
            this.total_joker = total_joker;
        }
    }


}
