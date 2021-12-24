package com.flipthenumber.beta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeltournamentEntry {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


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




    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("total_token")
        @Expose
        private Integer totalToken;
        @SerializedName("total_joker")
        @Expose
        private Integer totalJoker;
        @SerializedName("total_points")
        @Expose
        private Integer totalPoints;
        @SerializedName("total_photoframe")
        @Expose
        private Integer totalPhotoframe;
        @SerializedName("total_dice")
        @Expose
        private Integer totalDice;
        @SerializedName("total_diamond")
        @Expose
        private Integer totalDiamond;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getTotalToken() {
            return totalToken;
        }

        public void setTotalToken(Integer totalToken) {
            this.totalToken = totalToken;
        }

        public Integer getTotalJoker() {
            return totalJoker;
        }

        public void setTotalJoker(Integer totalJoker) {
            this.totalJoker = totalJoker;
        }

        public Integer getTotalPoints() {
            return totalPoints;
        }

        public void setTotalPoints(Integer totalPoints) {
            this.totalPoints = totalPoints;
        }

        public Integer getTotalPhotoframe() {
            return totalPhotoframe;
        }

        public void setTotalPhotoframe(Integer totalPhotoframe) {
            this.totalPhotoframe = totalPhotoframe;
        }

        public Integer getTotalDice() {
            return totalDice;
        }

        public void setTotalDice(Integer totalDice) {
            this.totalDice = totalDice;
        }

        public Integer getTotalDiamond() {
            return totalDiamond;
        }

        public void setTotalDiamond(Integer totalDiamond) {
            this.totalDiamond = totalDiamond;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }




}
