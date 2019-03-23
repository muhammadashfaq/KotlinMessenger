package com.example.inventoryapp.Model;

public class IsKitItemNoModel {
    String iskititem,qty,qtyshipped,item,itemName,qtycommitted,qtybackordered;


    public IsKitItemNoModel(String iskititem, String qty, String qtyshipped, String item, String itemName, String qtycommitted, String qtybackordered) {
        this.iskititem = iskititem;
        this.qty = qty;
        this.qtyshipped = qtyshipped;
        this.item = item;
        this.itemName = itemName;
        this.qtycommitted = qtycommitted;
        this.qtybackordered = qtybackordered;
    }

    public IsKitItemNoModel() {
    }

    public String getIskititem() {
        return iskititem;
    }

    public void setIskititem(String iskititem) {
        this.iskititem = iskititem;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQtyshipped() {
        return qtyshipped;
    }

    public void setQtyshipped(String qtyshipped) {
        this.qtyshipped = qtyshipped;
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

    public String getQtycommitted() {
        return qtycommitted;
    }

    public void setQtycommitted(String qtycommitted) {
        this.qtycommitted = qtycommitted;
    }

    public String getQtybackordered() {
        return qtybackordered;
    }

    public void setQtybackordered(String qtybackordered) {
        this.qtybackordered = qtybackordered;
    }
}
