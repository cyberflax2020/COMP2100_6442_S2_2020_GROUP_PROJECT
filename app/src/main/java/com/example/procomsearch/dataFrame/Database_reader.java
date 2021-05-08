package com.example.procomsearch.dataFrame;
/**
 * Author:Yuliang Ma, Yuchen Wang
 * This is a simple dataFrame.
 * We stored every companies's information into 11 BSTs according to the attributes.
 */


public class Database_reader {

    private BST NPAT = new BST();
    private BST NPGT = new BST();
    private BST TOIAT = new BST();
    private BST TOIGT = new BST();
    private BST OET = new BST();
    private BST SET = new BST();
    private BST MET = new BST();
    private BST FET = new BST();
    private BST TOET = new BST();
    private BST OPT = new BST();
    private BST TPT = new BST();

    private BST PROMT=new BST();             //This BST only store the promotingPowers of the promoted companies.


    public void setTreeByName(BST bst, String name) {
        switch (name) {
            case "NPAT":
                this.NPAT = bst;
                break;
            case "NPGT":
                this.NPGT = bst;
                break;
            case "TOIAT":
                this.TOIAT = bst;
                break;
            case "TOIGT":
                this.TOIGT = bst;
                break;
            case "OET":
                this.OET = bst;
                break;
            case "SET":
                this.SET = bst;
                break;
            case "MET":
                this.MET = bst;
                break;
            case "FET":
                this.FET = bst;
                break;
            case "TOET":
                this.TOET = bst;
                break;
            case "OPT":
                this.OPT = bst;
                break;
            case "TPT":
                this.TPT = bst;
                break;
            case"PROMT":
                this.PROMT=bst;

        }
    }

    public BST getTreeByName(String name) {
        switch (name) {
            case "NPAT":
                return NPAT;

            case "NPGT":
                return NPGT;

            case "TOIAT":
                return TOIAT;

            case "TOIGT":
                return TOIGT;

            case "OET":
                return OET;

            case "SET":
                return SET;

            case "MET":
                return MET;

            case "FET":
                return FET;

            case "TOET":
                return TOET;

            case "OPT":
                return OPT;

            case "TPT":
                return TPT;
        }
        return PROMT;
    }

}
