package com.example.procomsearch.parser;
/**
 * Author:Yuliang Ma, Yuchen Wang
 */

/**
 * Grammar Rule:
 * Author:Yuliang Ma
 *
 * <LogicalExp1> ::= <LogicalExp2> | <LogicalExp2> OR <LogicalExp1>
 * <LogicalExp2> ::= <LogicalExp3> | <LogicalExp3> AND <LogicalExp2>
 * <LogicalExp3> ::= <Exp>  | NOT <LogicalExp1> | (<LogicalExp1>)
 * <Exp> ::= <Attribute> = <Double> | <Attribute> > <Double> | <Attribute> < <Double>
 */



import com.example.procomsearch.dataFrame.Company_Index;
import com.example.procomsearch.tokenizer.Token;
import com.example.procomsearch.tokenizer.Tokenizer;

import java.util.ArrayList;

public class Parser {
    Tokenizer _tokenizer;

    public Parser(Tokenizer tokenizer) {
        _tokenizer = tokenizer;
    }


    public ArrayList<Company_Index> Search_rst(String querry) {
        Tokenizer tokenizer = new Tokenizer(querry);
        Exp querry_Exp = new Parser(tokenizer).LogicalExp1();
        return querry_Exp.evaluate();

    }

    public Exp LogicalExp1() {

        try {

            Exp result = new Parser(_tokenizer).LogicalExp2();
            if (_tokenizer.hasNext() && _tokenizer.current().type() == Token.Type.OR) {
                _tokenizer.next();
                Exp exp = new Parser(_tokenizer).LogicalExp1();
                result = new OrExp(result, exp);
            }
            return result;
        } catch (Exception e) {
            return new Unknown_Exp();                                                 //add try-catch to handle part of the invalid input problems
        }

    }

    public Exp LogicalExp2() {
        try {
            Exp result = new Parser(_tokenizer).LogicalExp3();
            if (_tokenizer.hasNext() && _tokenizer.current().type() == Token.Type.AND) {
                _tokenizer.next();
                Exp exp = new Parser(_tokenizer).LogicalExp2();
                result = new And_Exp(result, exp);
            }
            return result;
        } catch (Exception e) {
            return new Unknown_Exp();
        }
    }

    public Exp LogicalExp3() {

        try {

            if (_tokenizer.hasNext() && _tokenizer.current().type() == Token.Type.LBRA) {
                _tokenizer.next();
                Exp exp = new Parser(_tokenizer).LogicalExp1();
                _tokenizer.next();                                                                         //use _tokenizer.next() to skip ")".
                return exp;
            } else if (_tokenizer.hasNext() && _tokenizer.current().type() != Token.Type.NOT) {
                Exp exp = new Parser(_tokenizer).Exp1();
                return exp;
            } else {
                _tokenizer.next();
                Exp exp = new Parser(_tokenizer).LogicalExp1();
                return new NotExp(exp);
            }

        } catch (Exception e) {
            return new Unknown_Exp();
        }
    }

    public Exp Exp1() {
        try {
            Exp attri = new Attribute_Exp(_tokenizer.current().token());
            _tokenizer.next();
            if (_tokenizer.hasNext() && _tokenizer.current().type() == Token.Type.BIGGER) {
                _tokenizer.next();
                Exp Dou = new Dou_Exp(Double.parseDouble(_tokenizer.current().token()));
                _tokenizer.next();                                                                            //In the bottom layer of the parseTree, use _tokenizer.next()
                return new Bigger_Exp(attri, Dou);
            } else if (_tokenizer.hasNext() && _tokenizer.current().type() == Token.Type.SMALLER) {
                _tokenizer.next();
                Exp Dou = new Dou_Exp(Double.parseDouble(_tokenizer.current().token()));
                _tokenizer.next();
                return new Smaller_Exp(attri, Dou);
            } else {
                _tokenizer.next();
                Exp Dou = new Dou_Exp(Double.parseDouble(_tokenizer.current().token()));
                _tokenizer.next();
                return new Equal_Exp(attri, Dou);
            }
        } catch (Exception e) {
            return new Unknown_Exp();
        }
    }


}
