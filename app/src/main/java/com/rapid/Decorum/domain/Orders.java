package com.rapid.Decorum.domain;

import java.io.Serializable;

public class Orders implements Serializable {

   String Addressid ;
  String  Paymentstatus ;
   String fid ;
    String id ;
   String ordered_date ;
    String qty ;
    String uid ;


    public Orders() {
    }


    public String getAddressid() {
        return Addressid;
    }

    public void setAddressid(String addressid) {
        Addressid = addressid;
    }

    public String getPaymentstatus() {
        return Paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        Paymentstatus = paymentstatus;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrdered_date() {
        return ordered_date;
    }

    public void setOrdered_date(String ordered_date) {
        this.ordered_date = ordered_date;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
