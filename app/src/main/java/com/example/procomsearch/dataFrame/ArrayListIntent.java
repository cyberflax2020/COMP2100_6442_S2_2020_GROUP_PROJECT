package com.example.procomsearch.dataFrame;
/**
 * Author:Yuliang Ma
 * This is a simple class used for passing intents between activities.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class ArrayListIntent implements Serializable {
    private ArrayList<Company_Index> ci = new ArrayList<>();
    private String inputSearch;
    private int userId;

    public ArrayListIntent(ArrayList<Company_Index> ci,String inputSearch,int userId) {
        this.ci = ci;
        this.userId=userId;
        this.inputSearch=inputSearch;
    }

    public ArrayList<Company_Index> getList(){
        return this.ci;
    }
    public String getInputSearch(){
        return this.inputSearch;
    }
    public int getuserid(){
        return this.userId;
    }

}