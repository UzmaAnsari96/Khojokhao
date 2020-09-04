package com.khojokhao.Model;

public class WalletHistoryModel {
    String customer_id;
    String opening_bal;
    String closing_bal;
    String credit;
    String debit;
    String c_d_date;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getOpening_bal() {
        return opening_bal;
    }

    public void setOpening_bal(String opening_bal) {
        this.opening_bal = opening_bal;
    }

    public String getClosing_bal() {
        return closing_bal;
    }

    public void setClosing_bal(String closing_bal) {
        this.closing_bal = closing_bal;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getC_d_date() {
        return c_d_date;
    }

    public void setC_d_date(String c_d_date) {
        this.c_d_date = c_d_date;
    }
}
