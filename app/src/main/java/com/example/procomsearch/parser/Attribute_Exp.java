package com.example.procomsearch.parser;
/**
 * Author:Yuliang Ma
 */

import com.example.procomsearch.dataFrame.Company_Index;

import java.util.ArrayList;

public class Attribute_Exp extends Exp {
    //Our App's theme is about stocks, the attributions of stocks are used to
    // justify one companies states of operation, which can help to decide which
    // company we can invest.
    private String attribute;

    public Attribute_Exp(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getType() {
        return "Attr";
    }


    @Override
    public String show() {
        return this.attribute;
    }

    @Override
    public ArrayList<Company_Index> evaluate() {
        return null;
    }
}
