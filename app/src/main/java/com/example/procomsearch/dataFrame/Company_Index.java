package com.example.procomsearch.dataFrame;
/**
 * Author:Yuliang Ma
 * This is a class of objects to be stored in BSTs.
 * We only need key information.
 */
import java.io.Serializable;

public class Company_Index implements Serializable {
    public String Name;
    public Double Key;
    String Code;
    private Double promotedPower=0.0;                // init=0.0 means this is not promoted.

    public Company_Index(String code, String name, Double key) {
        this.Code = code;
        this.Name = name;
        this.Key = key;                              //key can be any attributes of the stock. It depends on which BST it will be stored in.
    }

    public String getCode() {
        return this.Code;
    }

    public void setPromotedPower(Double promotedPower){
        this.promotedPower=promotedPower;
    }

    public double getPromotedPower(){
        return this.promotedPower;
    }

}
