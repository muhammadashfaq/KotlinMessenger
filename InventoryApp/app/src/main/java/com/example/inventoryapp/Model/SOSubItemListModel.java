package com.example.inventoryapp.Model;

public class SOSubItemListModel {
    String itemName,iskititem;

    public SOSubItemListModel(String itemName,String iskititem) {
        this.itemName = itemName;
        this.iskititem=iskititem;
    }

    public String getIskititem() {
        return iskititem;
    }

    public void setIskititem(String iskititem) {
        this.iskititem = iskititem;
    }

    public SOSubItemListModel() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
