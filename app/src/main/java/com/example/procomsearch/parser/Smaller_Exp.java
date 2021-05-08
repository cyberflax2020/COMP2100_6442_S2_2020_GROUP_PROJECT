package com.example.procomsearch.parser;
/**
 * Author:Yuliang Ma
 */



import com.example.procomsearch.dataFrame.Company_Index;
import com.example.procomsearch.searcher.Searcher;

import java.util.ArrayList;

public class Smaller_Exp extends Exp {

    private String type = "Smaller";
    private Exp term;
    private Exp Dou;

    public Smaller_Exp(Exp term, Exp Dou) {
        this.term = term;
        this.Dou = Dou;
    }

    @Override
    public String getType() {
        return "Smaller";
    }

    @Override
    public String toString() {
        return term.show() + "<" + Dou.show();

    }

    @Override
    public String show() {
        return null;
    }

    @Override
    public ArrayList<Company_Index> evaluate() {
        //Justify the type of the exp and the term, if one of them is "Unknown" type,
        // consider it as "wrong input".
        // then send the exp to Searcher to process.
        if (this.term.getType().equals("Unknown")){
            return UnknownRst.getRst();
        }
        return Searcher.search(this, this.type);
    }
}
