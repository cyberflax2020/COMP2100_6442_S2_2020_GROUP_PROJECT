package com.example.procomsearch.dataFrame;
/**
 * Author:Yuliang Ma
 * Reference: Lab materials
 */

import java.util.ArrayList;

public class Node {
    public Double key;                    //key can be any attributes of the stock. It depends on which BST it will be stored in.
    public ArrayList<Company_Index> values;  // A list of all companies who have this key value.
    Node left;
    Node right;
    Node parent;

    public Node(Double key, Company_Index value) {
        this.key = key;
        values = new ArrayList<>();
        this.values.add(value);
        this.left = null;
        this.right = null;
        this.parent = null;
    }

}
