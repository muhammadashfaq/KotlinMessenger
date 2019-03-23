package com.example.inventoryapp.Model;

public class KitComponentsModel {
    String memberitem,memberitemname,memberQty;

    public KitComponentsModel() {
    }

    public KitComponentsModel(String memberitem, String memberitemname, String memberQty) {
        this.memberitem = memberitem;
        this.memberitemname = memberitemname;
        this.memberQty = memberQty;
    }

    public String getMemberitem() {
        return memberitem;
    }

    public void setMemberitem(String memberitem) {
        this.memberitem = memberitem;
    }

    public String getMemberitemname() {
        return memberitemname;
    }

    public void setMemberitemname(String memberitemname) {
        this.memberitemname = memberitemname;
    }

    public String getMemberQty() {
        return memberQty;
    }

    public void setMemberQty(String memberQty) {
        this.memberQty = memberQty;
    }
}
