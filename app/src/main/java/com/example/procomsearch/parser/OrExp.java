package com.example.procomsearch.parser;
/**
 * Author:Yuchen Wang
 */


import com.example.procomsearch.dataFrame.Company_Index;

import java.util.ArrayList;

public class OrExp extends Exp {
    private Exp exp1;
    private Exp exp2;

    public OrExp(Exp exp1, Exp exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public String getType() {
        return "Or";
    }


    public String show() {
        return exp1.show() + " | " + exp2.show();
    }

    @Override
    public ArrayList<Company_Index> evaluate() {
        if ((this.exp1.getType().equals("Unknown")) || (this.exp2.getType().equals("Unknown"))) {       //handle invalid input, then get all unique companies who has the same stock
                                                                                                        // code and appeared in LHS.evaluate() or RHS.evaluate()
            return UnknownRst.getRst();
        }
        ArrayList<Company_Index> termE = new ArrayList<>();
        ArrayList<Company_Index> expE = new ArrayList<>();
        ArrayList<Company_Index> rst = new ArrayList<>();

        termE.addAll(exp1.evaluate());
        expE.addAll(exp2.evaluate());
        rst.addAll(termE);
        rst.addAll(expE);

        for (Company_Index t : termE) {
            for (Company_Index e : expE) {
                if (t.getCode().equals(e.getCode())) {
                    rst.remove(t);

                }
            }
        }

        return rst;
    }
}
