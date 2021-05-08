package com.example.procomsearch.parser;
/**
 * Author:Yuliang Ma
 */

import com.example.procomsearch.dataFrame.Company_Index;

import java.util.ArrayList;

public class Dou_Exp extends Exp {

    private Double value;
    // Use double type
    public Dou_Exp(Double value) {
        this.value = value;
    }


    @Override
    public String getType() {
        return "Dou";
    }


    @Override
    public String show() {
        return this.value.toString();
    }

    @Override
    public ArrayList<Company_Index> evaluate() {
        return null;
    }
}
