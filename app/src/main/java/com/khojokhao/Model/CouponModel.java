package com.khojokhao.Model;

public class CouponModel {
    String coupon_id;
    String coupon_name;
    String description;
    String coupon_value;
    String capping_value;

    public String getCapping_value() {
        return capping_value;
    }

    public void setCapping_value(String capping_value) {
        this.capping_value = capping_value;
    }

    public String getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(String coupon_value) {
        this.coupon_value = coupon_value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }
}
