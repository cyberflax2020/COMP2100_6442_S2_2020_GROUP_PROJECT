package com.example.procomsearch.parser;
/**
 * Author:Yuchen Wang
 */


import com.example.procomsearch.dataFrame.Company_Index;
import com.example.procomsearch.searcher.Searcher;

import java.util.ArrayList;

public class NotExp extends Exp {
    private Exp exp;

    public NotExp(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String getType() {
        return "Not";
    }

    @Override
    public String show() {
        return "!( " + exp.show() + " )";
    }

    @Override
    public ArrayList<Company_Index> evaluate() {                         //First we handle invalid input, then we subtract the given exp's list from the whole list.
        if (this.exp.getType().equals("Unknown")){
            return UnknownRst.getRst();
        }
        ArrayList<Company_Index> rst1 = new ArrayList<>();
        ArrayList<Company_Index> rst = new ArrayList<>();
        ArrayList<Company_Index> allItems = new ArrayList<>();

        rst1.addAll(exp.evaluate());
        allItems = Searcher.getAllComps();

        rst.addAll(allItems);


        if (rst1.size() == 0) {
            return allItems;
        } else {
            for (Company_Index ai : allItems) {
                for (Company_Index r1 : rst1) {
                    if (ai.getCode().equals(r1.getCode())) {
                        rst.remove(ai);
                    }
                }
            }
            return rst;
        }

    }
}
