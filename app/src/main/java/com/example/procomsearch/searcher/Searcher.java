package com.example.procomsearch.searcher;
/**
 * Author:Yuliang Ma, Yuchen Wang
 */


import com.example.procomsearch.dataFrame.BST;
import com.example.procomsearch.dataFrame.Company_Index;
import com.example.procomsearch.dataFrame.Database_reader;
import com.example.procomsearch.parser.Exp;
import com.example.procomsearch.parser.UnknownRst;

import java.util.ArrayList;
import java.util.Collections;

public class Searcher {


    private static Database_reader database_reader = null;
    private static ArrayList<Company_Index> rst = new ArrayList<>();



    public static ArrayList<Company_Index> search(Exp exp, String EXP_type) {
        /**
         * Author:Yuchen Wang
         */
        rst.clear();

        switch (EXP_type) {
            case "Bigger":
                String tree_name = exp.toString().split(">")[0];
                Double value = Double.valueOf(exp.toString().split(">")[1]);

                BST bst = database_reader.getTreeByName(tree_name + "T");

                ArrayList<Company_Index> arrayList_result = bst.getPreOrderList(bst.root);

                int z = -1;

                for (int i = 0; i < arrayList_result.size(); i++) {
                    if (arrayList_result.get(i).Key.equals(value)) {
                        z = i;
                        break;
                    } else if (arrayList_result.get(i).Key > (value)) {
                        z = i;
                        break;
                    }
                }
                if (z != -1) {
                    for (int i = 0; i < arrayList_result.size() - z; i++) {
                        rst.add(arrayList_result.get(i + z));
                    }
                }


                return rst;
            case "Smaller":


                String tree_n = exp.toString().split("<")[0];
                Double value_s = Double.valueOf(exp.toString().split("<")[1]);


                BST bst1 = database_reader.getTreeByName(tree_n + "T");
                ArrayList<Company_Index> arrayList_r = bst1.getPreOrderList(bst1.root);

                int z1 = -50;
                for (int i = 0; i < arrayList_r.size(); i++) {
                    if (arrayList_r.get(i).Key.equals(value_s)) {
                        z1 = i - 1;
                        break;
                    } else if (arrayList_r.get(i).Key > (value_s)) {
                        z1 = i - 1;
                        break;
                    }
                }

                if (z1 != -50) {
                    for (int i = 0; i < z1; i++) {
                        rst.add(arrayList_r.get(i));
                    }
                }

                return rst;
            case "Equal":
                String tree_e = exp.toString().split("=")[0];
                Double value_e = Double.valueOf(exp.toString().split("=")[1]);
                BST bst2 = database_reader.getTreeByName(tree_e + "T");
                ArrayList<Company_Index> arrayList_e = bst2.getPreOrderList(bst2.root);

                int z2 = -1;
                for (int i = 0; i < arrayList_e.size(); i++) {
                    if (arrayList_e.get(i).Key.equals(value_e)) {
                        z2 = i;
                        break;
                    }
                }
                if (z2 != -1) {
                    rst.add(arrayList_e.get(z2));
                }
                return rst;

        }

        return UnknownRst.getRst();
    }

    public static void setDatabase_reader(Database_reader database_reader, ArrayList<Company_Index> promotedComps) {
        /**
         * Author:Yuliang Ma
         * In this method we add and store the promoting information into the Searcher
         */

        BST PROMT = new BST();
        for (Company_Index ci : promotedComps) {
            PROMT.insert(ci.getPromotedPower(), ci);
        }
        database_reader.setTreeByName(PROMT, "PROMT");
        Searcher.database_reader = database_reader;
    }

    public static ArrayList<Company_Index> getAllPromotedSortedList() {
        /**
         * Author:Yuliang Ma
         * This is a simple method to get a preOrder list of the PROMT.
         */

        ArrayList<Company_Index> rst = new ArrayList<>();
        rst.addAll(database_reader.getTreeByName("PROMT").getPreOrderList(database_reader.getTreeByName("PROMT").root));
        Collections.reverse(rst);
        return rst;
    }

    public static ArrayList<Company_Index> getAllComps() {
        /**
         * Author:Yuchen Wang
         * This is a simple method to get a preOrder list of the NPAT, because we dont care about the key values.
         */

        BST bst = database_reader.getTreeByName("NPAT");

        ArrayList<Company_Index> arrayList_result = bst.getPreOrderList(bst.root);

        return arrayList_result;
    }

    public static ArrayList<Company_Index> sortCompanies(ArrayList<Company_Index> inputList, String attr) {
        /**
         * Author:Yuliang Ma
         * The sorted Rst should be: First comes the promoted companies and we rank them by their promoted power.
         * After the promoted items we ranked the rest by the given key value.
         */

        ArrayList<Company_Index> rst1 = new ArrayList<>();
        BST bst = database_reader.getTreeByName(attr + "T");
        ArrayList<Company_Index> allComps = bst.getPreOrderList(bst.root);

        for (Company_Index ac : allComps) {
            for (Company_Index in : inputList) {
                if (ac.getCode().equals(in.getCode())) {
                    rst1.add(ac);

                }
            }
        }


        ArrayList<Company_Index> promotedComps = new ArrayList<>();

        promotedComps.addAll(getAllPromotedSortedList());

        ArrayList<Company_Index> rst = new ArrayList<>();

        for (Company_Index cp : promotedComps) {
            for (Company_Index r1 : rst1) {
                if (cp.getCode().equals(r1.getCode())) {
                    r1.setPromotedPower(cp.getPromotedPower());
                    rst.add(r1);
                }

            }
        }

        ArrayList<Company_Index> temp = new ArrayList<>();
        temp.addAll(rst1);

        for (Company_Index company_index : rst1) {
            for (Company_Index company_index1 : rst) {
                if (company_index.getCode().equals(company_index1.getCode()))
                    temp.remove(company_index);
            }
        }

        rst.addAll(temp);

        return rst;

    }

}
