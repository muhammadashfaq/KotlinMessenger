package com.example.inventoryapp.Model;

public class POSerialNumberModel {

    String number,item;

    public POSerialNumberModel() {
    }

    public POSerialNumberModel(String number, String item) {
        this.number = number;
        this.item = item;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
