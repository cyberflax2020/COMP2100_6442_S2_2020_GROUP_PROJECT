package com.example.procomsearch.dataFrame;
/**
 * Author:Yuliang Ma
 * Reference: Lab materials
 */

import java.util.ArrayList;

public class BST {

    public Node root;

    public void insert(Double key, Company_Index value) {                         // Implemented inserting iteratively
        Node parent = null;
        Node current = this.root;
        while (current != null) {                                                 //First find the right place to insert.
            if (current.key.compareTo(key) < 0) {
                parent = current;
                current = current.right;

            } else if (current.key.compareTo(key) > 0) {
                parent = current;
                current = current.left;
            } else {
                parent = current;
                break;
            }
        }

        if (parent == null) {
            this.root = new Node(key, value);                                       // no parent = root is empty

        } else {

            if (parent.key.compareTo(key) < 0) {                                    //No such a key value, crate a new Node with an empty companies list
                Node newNode = new Node(key, value);
                parent.right = newNode;
                newNode.parent = parent;
            } else if (parent.key.compareTo(key) > 0) {
                Node newNode = new Node(key, value);
                parent.left = newNode;
                newNode.parent = parent;
            } else {
                parent.values.add(value);                                            //Find the Node with the same value, just add into this node's list
            }

        }
    }


    public ArrayList<Company_Index> getPreOrderList(Node start) {                    //Implemented recursively.

        ArrayList<Company_Index> rst = new ArrayList<>();

        if (start.left == null && start.right == null) {                             //Final case, add all items in this node's list in rst.
            rst.addAll(start.values);
            return rst;
        } else {

            ArrayList<Company_Index> leftAr = new ArrayList<>();
            ArrayList<Company_Index> rightAr = new ArrayList<>();


            if (start.left != null) {
                leftAr = getPreOrderList(start.left);                                //get all left items
            }
            if (start.right != null) {
                rightAr = getPreOrderList(start.right);                              //get all right items
            }

            rst.addAll(start.values);                                                //add all items by order
            leftAr.addAll(rst);
            rst = leftAr;
            rst.addAll(rightAr);

            return rst;

        }

    }


}
