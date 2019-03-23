package com.example.inventoryapp.Model;

public class POModel {
    String poId,poName;

    public POModel() {
    }

    public POModel(String poId, String poName) {
        this.poId = poId;
        this.poName = poName;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public String getPoName() {
        return poName;
    }

    public void setPoName(String poName) {
        this.poName = poName;
    }
}
