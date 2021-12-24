package com.flipthenumber.beta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelAddCoins {

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

        @SerializedName("total_coins")
        @Expose
        private String total_coins;

        @SerializedName("total_points")
        @Expose
        private String total_points;

        public String getTotal_points() {
            return total_points;
        }

        public void setTotal_points(String total_points) {
            this.total_points = total_points;
        }

        public String getTotal_coins() {
            return total_coins;
        }

        public void setTotal_coins(String total_coins) {
            this.total_coins = total_coins;
        }
    }


}
