package com.flipthenumber.beta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelPayment {

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
        private String user_id;

        @SerializedName("total_token")
        @Expose
        private String total_token;

        @SerializedName("total_joker")
        @Expose
        private String total_joker;

        @SerializedName("total_photoframe")
        @Expose
        private String total_photoframe;

        @SerializedName("total_dice")
        @Expose
        private String total_dice;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getTotal_photoframe() {
            return total_photoframe;
        }

        public void setTotal_photoframe(String total_photoframe) {
            this.total_photoframe = total_photoframe;
        }

        public String getTotal_dice() {
            return total_dice;
        }

        public void setTotal_dice(String total_dice) {
            this.total_dice = total_dice;
        }
    }

}
