package com.arrowland.arrowland.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mhack Bautista on 9/17/2018.
 */

public class Payment {

    @SerializedName("success")
    @Expose
    private int success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("payment_method")
    @Expose
    private String payment_method;

    @SerializedName("transaction_code")
    @Expose
    private String transaction_code;

    @SerializedName("sender_name")
    @Expose
    private String sender_name;

    @SerializedName("amount_pay")
    @Expose
    private String amount_pay;

    @SerializedName("id")
    @Expose
    private int id;







    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getAmount_pay() {
        return amount_pay;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public int getId() {
        return id;
    }
}
