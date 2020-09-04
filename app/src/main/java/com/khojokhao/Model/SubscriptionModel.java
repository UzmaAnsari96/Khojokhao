package com.khojokhao.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class SubscriptionModel implements Serializable {
    String subscribe_id;
    String from_date;
    String to_date;
    String subscribe_mode;
    String deliver_timing;
    String p_mode;
    String payment_status;
    String subscribe_generate_id;
    ArrayList<String> daysArrayList;
    ArrayList<ProductModel> product_details;
    String address;
    String order_total;
    String order_date;
    String subscribe_status;

    public String getSubscribe_status() {
        return subscribe_status;
    }

    public void setSubscribe_status(String subscribe_status) {
        this.subscribe_status = subscribe_status;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getSubscribe_generate_id() {
        return subscribe_generate_id;
    }

    public void setSubscribe_generate_id(String subscribe_generate_id) {
        this.subscribe_generate_id = subscribe_generate_id;
    }

    public String getSubscribe_id() {
        return subscribe_id;
    }

    public void setSubscribe_id(String subscribe_id) {
        this.subscribe_id = subscribe_id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getSubscribe_mode() {
        return subscribe_mode;
    }

    public void setSubscribe_mode(String subscribe_mode) {
        this.subscribe_mode = subscribe_mode;
    }

    public String getDeliver_timing() {
        return deliver_timing;
    }

    public void setDeliver_timing(String deliver_timing) {
        this.deliver_timing = deliver_timing;
    }

    public String getP_mode() {
        return p_mode;
    }

    public void setP_mode(String p_mode) {
        this.p_mode = p_mode;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public ArrayList<String> getDaysArrayList() {
        return daysArrayList;
    }

    public void setDaysArrayList(ArrayList<String> daysArrayList) {
        this.daysArrayList = daysArrayList;
    }

    public ArrayList<ProductModel> getProduct_details() {
        return product_details;
    }

    public void setProduct_details(ArrayList<ProductModel> product_details) {
        this.product_details = product_details;
    }
}
