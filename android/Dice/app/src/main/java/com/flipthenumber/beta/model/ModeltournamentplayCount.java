package com.flipthenumber.beta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeltournamentplayCount {
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

        @SerializedName("tournament_id")
        @Expose
        private Integer tournament_id;

        @SerializedName("tournament_name")
        @Expose
        private String tournament_name;

        @SerializedName("tournament_image")
        @Expose
        private String tournament_image;

        @SerializedName("total_player")
        @Expose
        private String total_player;

        @SerializedName("tournament_player")
        @Expose
        private String tournament_player;

        @SerializedName("userExists")
        @Expose
        private String player_in_out;

        @SerializedName("round")
        @Expose
        private String round;

        public String getRound() {
            return round;
        }

        public void setRound(String round) {
            this.round = round;
        }

        public String getPlayer_in_out() {
            return player_in_out;
        }

        public void setPlayer_in_out(String player_in_out) {
            this.player_in_out = player_in_out;
        }

        public Integer getTournament_id() {
            return tournament_id;
        }

        public void setTournament_id(Integer tournament_id) {
            this.tournament_id = tournament_id;
        }

        public String getTournament_name() {
            return tournament_name;
        }

        public void setTournament_name(String tournament_name) {
            this.tournament_name = tournament_name;
        }

        public String getTournament_image() {
            return tournament_image;
        }

        public void setTournament_image(String tournament_image) {
            this.tournament_image = tournament_image;
        }

        public String getTotal_player() {
            return total_player;
        }

        public void setTotal_player(String total_player) {
            this.total_player = total_player;
        }

        public String getTournament_player() {
            return tournament_player;
        }

        public void setTournament_player(String tournament_player) {
            this.tournament_player = tournament_player;
        }
    }

}
