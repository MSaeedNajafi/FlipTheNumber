package com.flipthenumber.beta.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreModel {

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
        private String id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("price")
        @Expose
        private String price;

        @SerializedName("store_img")
        @Expose
        private String storeImg;

        @SerializedName("number")
        @Expose
        private String number;


        @SerializedName("type")
        @Expose
        private String type;

        @SerializedName("payment_type")
        @Expose
        private String payment_type;

        @SerializedName("total")
        @Expose
        private String total;

        @SerializedName("add_type")
        @Expose
        private String add_type;

        @SerializedName("total_add_type")
        @Expose
        private String total_add_type;




        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(String payment_type) {
            this.payment_type = payment_type;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getAdd_type() {
            return add_type;
        }

        public void setAdd_type(String add_type) {
            this.add_type = add_type;
        }

        public String getTotal_add_type() {
            return total_add_type;
        }

        public void setTotal_add_type(String total_add_type) {
            this.total_add_type = total_add_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStoreImg() {
            return storeImg;
        }

        public void setStoreImg(String storeImg) {
            this.storeImg = storeImg;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

    }
}