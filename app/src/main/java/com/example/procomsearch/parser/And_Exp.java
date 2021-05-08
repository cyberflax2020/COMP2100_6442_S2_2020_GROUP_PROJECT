package com.example.procomsearch.parser;
/**
 * Author:Yuchen Wang
 */


import com.example.procomsearch.dataFrame.Company_Index;

import java.util.ArrayList;

public class And_Exp extends Exp {
    private Exp term;
    private Exp exp;

    public And_Exp(Exp term, Exp exp) {
        this.term = term;
        this.exp = exp;
    }

    @Override
    public String getType() {
        return "And";
    }

    public String show() {
        return term.show() + " & " + exp.show();
    }

    @Override
    public ArrayList<Company_Index> evaluate() {

        //Justify the type of the exp and the term, if one of them is "Unknown" type,
        // consider it as "wrong input".
        if ((this.exp.getType().equals("Unknown")) || (this.term.getType().equals("Unknown"))) {
            return UnknownRst.getRst();                                                                 //handle invalid input, then get all unique companies who has the same stock
        }                                                                                               // code and appeared in both LHS.evaluate() and RHS.evaluate()

        ArrayList<Company_Index> termE = new ArrayList<>();
        ArrayList<Company_Index> expE = new ArrayList<>();
        ArrayList<Company_Index> rst = new ArrayList<>();

        //Using term and exp we can get two ArrayList containing company instances,
        //then we put them into two different ArrayLists.
        termE.addAll(term.evaluate());
        expE.addAll(exp.evaluate());

        // Since this is "And_Exp", we need to find companies satisfying conditions in
        // term and exp, so based on one ArrayList, we iterate each company to figure out
        // whether it's in another ArrayList, if so, put it into the result ArrayList.
        for (Company_Index t : termE) {
            for (Company_Index e : expE) {
                if (t.getCode().equals(e.getCode())) {
                    rst.add(t);

                }
            }
        }

        return rst;
    }
}
