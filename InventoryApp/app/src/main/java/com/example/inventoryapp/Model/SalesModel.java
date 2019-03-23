package com.example.inventoryapp.Model;

public class SalesModel {
    String soId,soName;

    public SalesModel() {

    }

    public SalesModel(String soId, String soName) {
        this.soId = soId;
        this.soName = soName;
    }

    public String getSoId() {
        return soId;
    }

    public void setSoId(String soId) {
        this.soId = soId;
    }

    public String getSoName() {
        return soName;
    }

    public void setSoName(String soName) {
        this.soName = soName;
    }
}
