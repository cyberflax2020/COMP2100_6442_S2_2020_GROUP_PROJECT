package com.example.procomsearch;


import android.content.res.Resources;
import android.provider.ContactsContract;

import com.example.procomsearch.dataFrame.BST;
import com.example.procomsearch.dataFrame.Company_Index;
import com.example.procomsearch.dataFrame.Database_reader;
import com.example.procomsearch.parser.Exp;
import com.example.procomsearch.parser.Parser;
import com.example.procomsearch.searcher.Searcher;
import com.example.procomsearch.tokenizer.Tokenizer;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class parserTest {
    private static Tokenizer tokenizer;

    private static final String SIMPLECASE = "NPA=251";
    private static final String COMPLEXCASE = "(NPA>5000)&(!(NPG<120.2))";

    @Test
    public void TestSim() {
        iniDatabaseReader();
        Tokenizer tokenizer = new Tokenizer(SIMPLECASE);
        try{
            Exp e = new Parser(tokenizer).LogicalExp1();
            ArrayList<Company_Index> rst = new ArrayList<>();
            if (tokenizer.checkBracketMatching()){
                rst.addAll(e.evaluate());
            }
            assertEquals("incorrect evaluate value","603758", rst.get(0).getCode());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void TestComplex() {
        iniDatabaseReader();
        Tokenizer tokenizer = new Tokenizer(COMPLEXCASE);
        try{
            Exp e = new Parser(tokenizer).LogicalExp1();
            ArrayList<Company_Index> rst = new ArrayList<>();
            if (tokenizer.checkBracketMatching()){
                rst.addAll(e.evaluate());
            }
            assertEquals("incorrect evaluate value","002714", rst.get(0).getCode());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }
    private void iniDatabaseReader() {
        int row = 0;
        int colum = 0;
        ArrayList<String> allItems = new ArrayList<>();
        ArrayList<Company_Index> allPromotedComps = new ArrayList<>();

        Database_reader d = runDatabaseReader();

        try {
            File file = new File("C:\\Users\\user\\IdeaProjects\\comp2100_6442_s2_2020_group_project\\app\\src\\main\\res\\raw\\original_database.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GB2312"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                while ((line = reader.readLine()) != null) {
                    String[] item = line.split(",");
                    for (String i : item) {
                        allItems.add(i);
                    }
                    row++;
                }

                colum = allItems.size() / row;

                for (int i = 0; i < row; i++) {
                    String code = allItems.get(colum * i);
                    String name = allItems.get(colum * i + 1);

                    int codeL = code.length();
                    if (codeL != 6) {
                        for (int j = 0; j < 6 - codeL; j++) {
                            code = "0" + code;
                        }
                    }


                    double promotedPower = Double.parseDouble(allItems.get(colum * i + 2));
                    Company_Index c = new Company_Index(code, name, 0.0);
                    c.setPromotedPower(promotedPower);
                    allPromotedComps.add(c);
                }
            }

        } catch (Exception e) {
        }

        Searcher.setDatabase_reader(d, allPromotedComps);
    }

    /**
     * database reader
     *
     * @return
     */
    public Database_reader runDatabaseReader() {
        Database_reader database_reader = new Database_reader();
        int row = 0;
        int colum = 0;
        ArrayList<String> allItems = new ArrayList<>();

        BST NPAT = new BST();
        BST NPGT = new BST();
        BST TOIAT = new BST();
        BST TOIGT = new BST();
        BST OET = new BST();
        BST SET = new BST();
        BST MET = new BST();
        BST FET = new BST();
        BST TOET = new BST();
        BST OPT = new BST();
        BST TPT = new BST();


        try {
            File file = new File("C:\\Users\\user\\IdeaProjects\\comp2100_6442_s2_2020_group_project\\app\\src\\main\\res\\raw\\original_database.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GB2312"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] item = line.split(",");
                for (String i : item) {
                    allItems.add(i);
                }
                row++;
            }

            colum = allItems.size() / row;


            for (int i = 0; i < row; i++) {
                String code = allItems.get(colum * i);
                String name = allItems.get(colum * i + 1);

                int codeL = code.length();
                if (codeL != 6) {
                    for (int j = 0; j < 6 - codeL; j++) {
                        code = "0" + code;
                    }
                }


                String NPAS = allItems.get(colum * i + 2);
                String NPGS = allItems.get(colum * i + 3);
                String TOIAS = allItems.get(colum * i + 4);
                String TOIGS = allItems.get(colum * i + 5);
                String OES = allItems.get(colum * i + 6);


                String SES = allItems.get(colum * i + 7);
                String MES = allItems.get(colum * i + 8);
                String FES = allItems.get(colum * i + 9);
                String TOES = allItems.get(colum * i + 10);
                String OPS = allItems.get(colum * i + 11);
                String TPS = allItems.get(colum * i + 12);

                if (NPGS.equals("-")) {
                    NPGS = "0";
                }

                if (TOIAS.equals("-")) {
                    TOIAS = "00";
                }
                if (TOIGS.equals("-")) {
                    TOIGS = "0";
                }
                if (OES.equals("-")) {
                    OES = "00";
                }

                if (SES.equals("-")) {
                    SES = "00";
                }
                if (MES.equals("-")) {
                    MES = "00";
                }
                if (FES.equals("-")) {
                    FES = "00";
                }


                if (NPAS.equals("0")) {
                    NPAS = "00";
                }


                if (TOIAS.equals("0")) {
                    TOIAS = "00";
                }


                if (OES.equals("0")) {
                    OES = "00";
                }

                if (SES.equals("0")) {
                    SES = "00";
                }
                if (MES.equals("0")) {
                    MES = "00";
                }
                if (FES.equals("0")) {
                    FES = "00";
                }


                double NPA = Double.parseDouble(NPAS.substring(0, NPAS.length() - 1));
                double NPG = Double.parseDouble(NPGS);
                double TOIA = Double.parseDouble(TOIAS.substring(0, TOIAS.length() - 1));
                double TOIG = Double.parseDouble(TOIGS);
                double OE = Double.parseDouble(OES.substring(0, OES.length() - 1));
                double SE = Double.parseDouble(SES.substring(0, SES.length() - 1));
                double ME = Double.parseDouble(MES.substring(0, MES.length() - 1));
                double FE = Double.parseDouble(FES.substring(0, FES.length() - 1));
                double TOE = Double.parseDouble(TOES.substring(0, TOES.length() - 1));
                double OP = Double.parseDouble(OPS.substring(0, OPS.length() - 1));
                double TP = Double.parseDouble(TPS.substring(0, TPS.length() - 1));


                if (NPAS.charAt(NPAS.length() - 1) == '万') {
                    NPA = NPA / 100;
                }
                if (NPAS.charAt(NPAS.length() - 1) == '亿') {
                    NPA = NPA * 100;
                }

                if (TOIAS.charAt(TOIAS.length() - 1) == '万') {
                    TOIA = TOIA / 100;
                }
                if (TOIAS.charAt(TOIAS.length() - 1) == '亿') {
                    TOIA = TOIA * 100;
                }

                if (OES.charAt(OES.length() - 1) == '万') {
                    OE = OE / 100;
                }
                if (OES.charAt(OES.length() - 1) == '亿') {
                    OE = OE * 100;
                }

                if (SES.charAt(SES.length() - 1) == '万') {
                    SE = SE / 100;
                }
                if (SES.charAt(SES.length() - 1) == '亿') {
                    SE = SE * 100;
                }

                if (MES.charAt(MES.length() - 1) == '万') {
                    ME = ME / 100;
                }
                if (MES.charAt(MES.length() - 1) == '亿') {
                    ME = ME * 100;
                }

                if (FES.charAt(FES.length() - 1) == '万') {
                    FE = FE / 100;
                }
                if (FES.charAt(FES.length() - 1) == '亿') {
                    FE = FE * 100;
                }

                if (TOES.charAt(TOES.length() - 1) == '万') {
                    TOE = TOE / 100;
                }
                if (TOES.charAt(TOES.length() - 1) == '亿') {
                    TOE = TOE * 100;
                }

                if (OPS.charAt(OPS.length() - 1) == '万') {
                    OP = OP / 100;
                }
                if (OPS.charAt(OPS.length() - 1) == '亿') {
                    OP = OP * 100;
                }

                if (TPS.charAt(TPS.length() - 1) == '万') {
                    TP = TP / 100;
                }
                if (TPS.charAt(TPS.length() - 1) == '亿') {
                    TP = TP * 100;
                }

                NPA = (double) Math.round(NPA * 100) / 100;
                NPG = (double) Math.round(NPG * 100) / 100;
                TOIA = (double) Math.round(TOIA * 100) / 100;
                TOIG = (double) Math.round(TOIG * 100) / 100;
                OE = (double) Math.round(OE * 100) / 100;
                SE = (double) Math.round(SE * 100) / 100;
                ME = (double) Math.round(ME * 100) / 100;
                FE = (double) Math.round(FE * 100) / 100;
                TOE = (double) Math.round(TOE * 100) / 100;
                OP = (double) Math.round(OP * 100) / 100;
                TP = (double) Math.round(TP * 100) / 100;


                Company_Index comp_NPA = new Company_Index(code, name, NPA);
                Company_Index comp_NPG = new Company_Index(code, name, NPG);
                Company_Index comp_TOIA = new Company_Index(code, name, TOIA);
                Company_Index comp_TOIG = new Company_Index(code, name, TOIG);
                Company_Index comp_OE = new Company_Index(code, name, OE);
                Company_Index comp_SE = new Company_Index(code, name, SE);
                Company_Index comp_ME = new Company_Index(code, name, ME);
                Company_Index comp_FE = new Company_Index(code, name, FE);
                Company_Index comp_TOE = new Company_Index(code, name, TOE);
                Company_Index comp_OP = new Company_Index(code, name, OP);
                Company_Index comp_TP = new Company_Index(code, name, TP);


                NPAT.insert(NPA, comp_NPA);
                NPGT.insert(NPG, comp_NPG);
                TOIAT.insert(TOIA, comp_TOIA);
                TOIGT.insert(TOIG, comp_TOIG);
                OET.insert(OE, comp_OE);
                SET.insert(SE, comp_SE);
                MET.insert(ME, comp_ME);
                FET.insert(FE, comp_FE);
                TOET.insert(TOE, comp_TOE);
                OPT.insert(OP, comp_OP);
                TPT.insert(TP, comp_TP);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        database_reader.setTreeByName(NPAT, "NPAT");
        database_reader.setTreeByName(NPGT, "NPGT");
        database_reader.setTreeByName(TOIAT, "TOIAT");
        database_reader.setTreeByName(TOIGT, "TOIGT");
        database_reader.setTreeByName(OET, "OET");
        database_reader.setTreeByName(SET, "SET");
        database_reader.setTreeByName(MET, "MET");
        database_reader.setTreeByName(FET, "FET");
        database_reader.setTreeByName(TOET, "TOET");
        database_reader.setTreeByName(OPT, "OPT");
        database_reader.setTreeByName(TPT, "TPT");

        return database_reader;
    }

}
