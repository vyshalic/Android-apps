package com.example.b_wi.sample1;

public class order {
    int uid;
    int oid;
    int NoOfCans;
    int amount;
    String location;
    String name;

    public order(){};

    public String getName() {
        return name;
    }

    public order(int uid, int oid, int noOfCans, int amount, String location, String name) {
        this.uid = uid;
        this.oid = oid;
        NoOfCans = noOfCans;
        this.amount = amount;
        this.location = location;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public order(int uid, int oid, int noOfCans, int amount, String location) {
        this.uid = uid;
        this.oid = oid;
        NoOfCans = noOfCans;
        this.amount = amount;
        this.location = location;
    }

    public order(int uid, int oid, int NoOfcans, int amount) {
        this.uid = uid;
        this.oid = oid;
        this.amount = amount;
        this.NoOfCans=NoOfcans;
    }

    public int getNoOfCans() {
        return NoOfCans;
    }

    public void setNoOfCans(int noOfCans) {
        NoOfCans = noOfCans;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
