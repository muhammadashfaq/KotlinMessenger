package com.example.inventoryapp.Model;

public class POLocationsModel {
    String locationId,locationName;

    public POLocationsModel() {

    }

    public POLocationsModel(String locationId, String locationName) {
        this.locationId = locationId;
        this.locationName = locationName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
