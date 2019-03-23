package com.example.inventoryapp.Model;

public class PODetailsModel {
    String qty,qtyreceived,item,itemName,qtybilled;

    public PODetailsModel() {
    }

    public PODetailsModel(String qty, String qtyreceived, String item, String itemName, String qtybilled) {
        this.qty = qty;
        this.qtyreceived = qtyreceived;
        this.item = item;
        this.itemName = itemName;
        this.qtybilled = qtybilled;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQtyreceived() {
        return qtyreceived;
    }

    public void setQtyreceived(String qtyreceived) {
        this.qtyreceived = qtyreceived;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQtybilled() {
        return qtybilled;
    }

    public void setQtybilled(String qtybilled) {
        this.qtybilled = qtybilled;
    }
}
