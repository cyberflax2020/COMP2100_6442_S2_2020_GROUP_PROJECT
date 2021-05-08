package com.example.procomsearch.parser;
/**
 * Author:Yuliang Ma
 */

import com.example.procomsearch.dataFrame.Company_Index;

import java.util.ArrayList;

public abstract class Exp {
    public abstract String getType();

    public abstract String show();

    public abstract ArrayList<Company_Index> evaluate();

}
