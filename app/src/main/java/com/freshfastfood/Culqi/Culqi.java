package com.freshfastfood.Culqi;

import org.json.JSONObject;

public class Culqi {
    String object;
    String id;
    long creation_date;
    int amount;
    int amount_refunded;
    int current_amount;
    int installments;
    long installments_amount;
    String currency;
    String email;
    String description;
    JSONObject source;
    JSONObject outcome;
    int fraud_score;
    JSONObject antifraud_details;
    JSONObject fee_details;
    int total_fee_taxes;
    int transfer_amount;
    boolean duplicated;
    String user_message;

    public Culqi() {
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount_refunded() {
        return amount_refunded;
    }

    public void setAmount_refunded(int amount_refunded) {
        this.amount_refunded = amount_refunded;
    }

    public int getCurrent_amount() {
        return current_amount;
    }

    public void setCurrent_amount(int current_amount) {
        this.current_amount = current_amount;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public long getInstallments_amount() {
        return installments_amount;
    }

    public void setInstallments_amount(long installments_amount) {
        this.installments_amount = installments_amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONObject getSource() {
        return source;
    }

    public void setSource(JSONObject source) {
        this.source = source;
    }

    public JSONObject getOutcome() {
        return outcome;
    }

    public void setOutcome(JSONObject outcome) {
        this.outcome = outcome;
    }

    public int getFraud_score() {
        return fraud_score;
    }

    public void setFraud_score(int fraud_score) {
        this.fraud_score = fraud_score;
    }

    public JSONObject getAntifraud_details() {
        return antifraud_details;
    }

    public void setAntifraud_details(JSONObject antifraud_details) {
        this.antifraud_details = antifraud_details;
    }

    public JSONObject getFee_details() {
        return fee_details;
    }

    public void setFee_details(JSONObject fee_details) {
        this.fee_details = fee_details;
    }

    public int getTotal_fee_taxes() {
        return total_fee_taxes;
    }

    public void setTotal_fee_taxes(int total_fee_taxes) {
        this.total_fee_taxes = total_fee_taxes;
    }

    public int getTransfer_amount() {
        return transfer_amount;
    }

    public void setTransfer_amount(int transfer_amount) {
        this.transfer_amount = transfer_amount;
    }

    public boolean isDuplicated() {
        return duplicated;
    }

    public void setDuplicated(boolean duplicated) {
        this.duplicated = duplicated;
    }

    public String getUser_message() {
        return user_message;
    }

    public void setUser_message(String user_message) {
        this.user_message = user_message;
    }
}
