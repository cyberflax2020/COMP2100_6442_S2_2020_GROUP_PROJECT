package com.example.procomsearch.parser;
/**
 * Author:Yuliang Ma
 */

import com.example.procomsearch.dataFrame.Company_Index;

import java.util.ArrayList;

public class Unknown_Exp extends Exp {
    @Override
    public String getType() {
        return "Unknown";
    }

    @Override
    public String show() {
        return null;
    }

    @Override
     // If the Exp can not be processed by the parser, when it is sent to the Searcher, it should contain a list like below.
    public ArrayList<Company_Index> evaluate() {
        return UnknownRst.getRst();
    }
}
