package com.khojokhao.Model;

import java.io.Serializable;

public class AddressModel implements Serializable {
    String id;
    String flat_no;
    String building_name;
    String landmark;
    String area_name;
    String pincode;
    String pickup_address;
    String sector_number;
    String area_id;

    public String getSector_number() {
        return sector_number;
    }

    public void setSector_number(String sector_number) {
        this.sector_number = sector_number;
    }

    public String getPickup_address() {
        return pickup_address;
    }

    public void setPickup_address(String pickup_address) {
        this.pickup_address = pickup_address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void SetAid(String area_id) {
        this.area_id = area_id;
    }

    public String getAid() {
        return area_id;
    }
}
