package com.example.muhammadashfaq.eatitserver.Model;

public class UserModel
{
    private String name, password,isStaff,image;
    String phone;

    public UserModel() {
    }

    public UserModel(String name, String password) {
        this.name = name;
        this.password = password;
        this.isStaff="false";
    }

    public UserModel(String name, String password, String phone, String isStaff,String image) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.image=image;
        this.isStaff = isStaff;
        this.isStaff="false";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }
}
