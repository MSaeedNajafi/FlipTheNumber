package com.flipthenumber.beta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelCollectReward {

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

        @SerializedName("id")
        @Expose
        private Integer id;

        @SerializedName("user_id")
        @Expose
        private String userId;


        @SerializedName("total_token")
        @Expose
        private String total_token;

        @SerializedName("total_joker")
        @Expose
        private String total_joker;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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
