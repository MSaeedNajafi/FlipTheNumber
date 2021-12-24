package com.flipthenumber.beta.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnFriendModel {

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
        private Integer userId;
        @SerializedName("friend_id")
        @Expose
        private Integer friendId;
        @SerializedName("request_status")
        @Expose
        private Integer requestStatus;

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

        public Integer getFriendId() {
            return friendId;
        }

        public void setFriendId(Integer friendId) {
            this.friendId = friendId;
        }

        public Integer getRequestStatus() {
            return requestStatus;
        }

        public void setRequestStatus(Integer requestStatus) {
            this.requestStatus = requestStatus;
        }

    }
}