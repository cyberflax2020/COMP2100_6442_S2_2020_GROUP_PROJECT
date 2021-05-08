package com.example.procomsearch.parser;
/**
 * Author:Yuliang Ma
 * This is a unique Resulting Object indicating that this is an invalid result.
 */

import com.example.procomsearch.dataFrame.Company_Index;

import java.util.ArrayList;

public class UnknownRst {
    private static ArrayList<Company_Index> rst=new ArrayList<>(1);

    public static ArrayList<Company_Index> getRst() {
        rst.add(new Company_Index("Wrong input", "Wrong input", 0.0));
        return rst;
    }
}
