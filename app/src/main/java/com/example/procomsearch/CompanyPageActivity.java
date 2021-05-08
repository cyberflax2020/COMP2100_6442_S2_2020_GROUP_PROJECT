package com.example.procomsearch;
/**
 * Author: Chaofan Li, Yuchen Wang
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procomsearch.dataFrame.ArrayListIntent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CompanyPageActivity extends AppCompatActivity {

    private int userId, imageId;
    ImageButton like;
    ImageView imageView;
    SQLiteDatabase db;
    TextView companyName, stockNo, npaValue, npgValue, toiaValue,toigValue, oeValue, seValue, meValue,
    feValue, toeValue, tpValue,opValue,promotedvalue;
    private ArrayListIntent AI;
    private String strCompanyName, strStockNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_page);

        //get the favorite database
        db = SQLiteDatabase.openOrCreateDatabase("data/data/"+getPackageName()+"/databases/"+getString(R.string.database_name),null);
        strCompanyName = getIntent().getStringExtra("companyName");
        strStockNo = getIntent().getStringExtra("stockNo");
        imageId = getIntent().getIntExtra("imageId",-1);
        userId = getIntent().getIntExtra("user id",-1);
        /**
         * Author: Yuchen Wang
         */
        ArrayList<String> Companyinfo = new ArrayList<>();
        //Add 0 to strStockNo in order to make every company's CODE is 6 digit.
        if(strStockNo.length()<6){
            int length = 0;
            length = strStockNo.length();
            for(int i = 0;i<6-length;i++){
                strStockNo = "0" +strStockNo;
            }
        }
        Boolean promoted_or_not = false;
        Companyinfo = CompanyInformation(strStockNo);
        promoted_or_not = Promoted_or_not(strStockNo);


        //getAllTextView
        companyName = findViewById(R.id.company_name);
        stockNo = findViewById(R.id.stock_no);
        npaValue = findViewById(R.id.npa_value);
        npgValue = findViewById(R.id.npg_value);
        toiaValue = findViewById(R.id.toia_value);
        toigValue = findViewById(R.id.toig_value);
        oeValue = findViewById(R.id.oe_value);
        seValue = findViewById(R.id.se_value);
        meValue = findViewById(R.id.me_value);
        feValue = findViewById(R.id.fe_value);
        toeValue = findViewById(R.id.toe_value);
        opValue = findViewById(R.id.op_value);
        tpValue = findViewById(R.id.tp_value);
        promotedvalue = findViewById(R.id.promoted_value);

        companyName.setText(Companyinfo.get(0));
        stockNo.setText(Companyinfo.get(1));
        String npa_v = Companyinfo.get(2)+" million";
        npaValue.setText(npa_v);
        String npg_v = Companyinfo.get(3)+ " %";
        npgValue.setText(npg_v);
        String toia_v = Companyinfo.get(4)+" million";
        toiaValue.setText(toia_v);
        String toig_v = Companyinfo.get(5)+" %";
        toigValue.setText(toig_v);
        String oe_v = Companyinfo.get(6)+" million";
        oeValue.setText(oe_v);
        String se_v = Companyinfo.get(7)+" million";
        seValue.setText(se_v);
        String me_v = Companyinfo.get(8)+" million";
        meValue.setText(me_v);
        String fe_v = Companyinfo.get(9)+" million";
        feValue.setText(fe_v);
        String toe_v = Companyinfo.get(10)+" million";
        toeValue.setText(toe_v);
        String op_v = Companyinfo.get(11)+" million";
        opValue.setText(op_v);
        String tp_v = Companyinfo.get(12)+" million";
        tpValue.setText(tp_v);
        String prom_v = promoted_or_not.toString();
        promotedvalue.setText(prom_v);
        //get ImageView
        imageView = findViewById(R.id.company_img);

        //set title in this page
        companyName.setText(strCompanyName);
        stockNo.setText("Stock Number: "+ strStockNo);

        //set the image
        imageView.setImageDrawable(getDrawable(imageId));

        like = (ImageButton)findViewById(R.id.like);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId<=0){
                    Toast.makeText(v.getContext(),"You need to login to add favorite.",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!favoriteIsInDatabase(strStockNo, userId)) {
                        if(addToFavoriteDatabase()){
                            Toast.makeText(v.getContext(),"The company is added to your favorite!",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        System.out.println("you already have this company");
                        warningDialog();
                    }
                }
            }
        });

        //get user_id
        userId = getIntent().getIntExtra("user id",-1);
        //Toast.makeText(this,"user id: "+userId, Toast.LENGTH_SHORT).show();
    }

    private void warningDialog(){
        final AlertDialog mDialog = new AlertDialog.Builder(this).setPositiveButton("Yes",null)
                .setNegativeButton("Cancel",null).create();
        mDialog.setTitle("Remove From Favorite?");
        mDialog.setMessage("You have add this company to favorite. Do you want to remove it?");
        mDialog.setCancelable(false);
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                Button positiveButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = mDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(removeFromFavoriteDatabase()){
                            Toast.makeText(CompanyPageActivity.this,"You have removed "+ CompanyPageActivity.this.strCompanyName,
                                    Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
            }
        });
        mDialog.show();
    }

    /**
     * delete this stockNo-userId from database
     * @return
     */
    private boolean removeFromFavoriteDatabase() {
        db.execSQL("delete from Favorite where userId = ? and stockNo = ?", new String[]{
                this.userId+"", this.strStockNo
        });
        return true;
    }

    /**
     * check whether this stockNo-userId has exist in database
     * @param strStockNo
     * @param userId
     * @return
     */
    private boolean favoriteIsInDatabase(String strStockNo, int userId) {
        Cursor cursor = db.rawQuery("select stockNo from Favorite where userId = ? and stockNo = ?",
                new String[]{userId+"",strStockNo});
        //if the cursor doesn't have any row in it, this company is never favorite
        if (cursor.getCount()!=0){
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * add this stockNo-userId to database
     * @return
     */
    private boolean addToFavoriteDatabase(){
        db.execSQL("insert into Favorite (stockNo, companyName, userId) values(?,?,?)",new String[]{this.strStockNo, this.strCompanyName, this.userId+""});
        return true;
    }

    /**
     * get information from csv file
     * @param strStockNo
     * @return
     */
    public ArrayList<String> CompanyInformation(String strStockNo) {
        /**
         * Author: Yuchen Wang
         */
        int row = 0;
        int colum = 0;
        ArrayList<String> allItems = new ArrayList<>();
        ArrayList<String> rst = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.original_database), "GB2312"));
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
                if(code.length()<6){
                    int length = 0;
                    length = code.length();
                    for(int z = 0;z<6-length;z++){
                        code = "0" +code;
                    }
                }

                if(code.equals(strStockNo)){
                    rst.add(code);
                    rst.add(name);

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

                    NPA=(double)Math.round(NPA*100)/100;
                    NPG=(double)Math.round(NPG*100)/100;
                    TOIA=(double)Math.round(TOIA*100)/100;
                    TOIG=(double)Math.round(TOIG*100)/100;
                    OE=(double)Math.round(OE*100)/100;
                    SE=(double)Math.round(SE*100)/100;
                    ME=(double)Math.round(ME*100)/100;
                    FE=(double)Math.round(FE*100)/100;
                    TOE=(double)Math.round(TOE*100)/100;
                    OP=(double)Math.round(OP*100)/100;
                    TP=(double)Math.round(TP*100)/100;


                    rst.add(String.valueOf(NPA));
                    rst.add(String.valueOf(NPG));
                    rst.add(String.valueOf(TOIA));
                    rst.add(String.valueOf(TOIG));
                    rst.add(String.valueOf(OE));
                    rst.add(String.valueOf(SE));
                    rst.add(String.valueOf(ME));
                    rst.add(String.valueOf(FE));
                    rst.add(String.valueOf(TOE));
                    rst.add(String.valueOf(OP));
                    rst.add(String.valueOf(TP));

                }



            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return rst;


}

    public boolean Promoted_or_not(String strStockNo){
        /**
         * Author: Yuchen Wang
         */
        int row = 0;
        int colum = 0;
        ArrayList<String> allItems = new ArrayList<>();
        Boolean rst = false;
        //
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.promoted_comps), "GB2312"));
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
                if(code.length()<6){
                    int length = 0;
                    length = code.length();
                    for(int z = 0;z<6-length;z++){
                        code = "0" +code;
                    }
                }

                if(code.equals(strStockNo)){
                    rst = true;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rst;
    }
}