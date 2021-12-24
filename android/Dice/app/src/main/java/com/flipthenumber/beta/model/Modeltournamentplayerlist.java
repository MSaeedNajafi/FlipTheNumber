package com.flipthenumber.beta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Modeltournamentplayerlist {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("round")
    @Expose
    private String round;

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

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
        private String id;

        @SerializedName("player_t_id")
        @Expose
        private String player_t_id;

        @SerializedName("tournament_id")
        @Expose
        private String tournament_id;

        @SerializedName("tournament_name")
        @Expose
        private String tournament_name;

        @SerializedName("tournamant_image")
        @Expose
        private String tournamant_image;




        @SerializedName("user_id")
        @Expose
        private String user_id;

        @SerializedName("player_id")
        @Expose
        private String player_id;

        @SerializedName("user_name")
        @Expose
        private String user_name;

        @SerializedName("player_name")
        @Expose
        private String player_name;



        @SerializedName("user_img")
        @Expose
        private String user_img;

        @SerializedName("playerimg")
        @Expose
        private String playerimg;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlayer_t_id() {
            return player_t_id;
        }

        public void setPlayer_t_id(String player_t_id) {
            this.player_t_id = player_t_id;
        }

        public String getTournament_id() {
            return tournament_id;
        }

        public void setTournament_id(String tournament_id) {
            this.tournament_id = tournament_id;
        }

        public String getTournament_name() {
            return tournament_name;
        }

        public void setTournament_name(String tournament_name) {
            this.tournament_name = tournament_name;
        }

        public String getTournamant_image() {
            return tournamant_image;
        }

        public void setTournamant_image(String tournamant_image) {
            this.tournamant_image = tournamant_image;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getPlayer_id() {
            return player_id;
        }

        public void setPlayer_id(String player_id) {
            this.player_id = player_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPlayer_name() {
            return player_name;
        }

        public void setPlayer_name(String player_name) {
            this.player_name = player_name;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getPlayerimg() {
            return playerimg;
        }

        public void setPlayerimg(String playerimg) {
            this.playerimg = playerimg;
        }
    }
}
