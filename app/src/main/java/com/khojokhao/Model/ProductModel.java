package com.khojokhao.Model;

import java.io.Serializable;

public class ProductModel implements Serializable {
    String product_id;
    String product_name;
    String image;
    String flag;
    String unit;
    String price;
    String discount;
    String qty;
    String spi;
    String subscribe_status;
    String type;

    public String getSubscribe_status() {
        return subscribe_status;
    }

    public void setSubscribe_status(String subscribe_status) {
        this.subscribe_status = subscribe_status;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getSpi() {
        return spi;
    }

    public void setSpi(String spi) {
        this.spi = spi;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
