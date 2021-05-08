package com.example.procomsearch;
/**
 * Author:Chaofan Li, Yuchen Wang, Yuliang Ma
 */


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procomsearch.dataFrame.BST;
import com.example.procomsearch.dataFrame.Company_Index;
import com.example.procomsearch.dataFrame.Database_reader;
import com.example.procomsearch.history.HistoryUtil;
import com.example.procomsearch.searcher.Searcher;
import com.example.procomsearch.userDatabase.MyDatabaseHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity_sign_in extends AppCompatActivity {

    MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    String inputUsername, inputPassword;
    EditText signInUsernameView, signInPasswordView;
    TextView forgetPassword, enterWithoutSignIn;
    private String userName;
    int userId;
    final String forgetPass = "Forget password?";
    final String enterWithout = "Enter without sign in";
    private String hintQuestion;
    private String hintAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_sign_in);
        //create and set the companies database(search tree)
        iniDatabaseReader();



        //create and set the Users database
        dbHelper = new MyDatabaseHelper(this, "Users.db", null, 1);
        db = dbHelper.getWritableDatabase();
        HistoryUtil.initUtil();

        signInUsernameView = (EditText) findViewById(R.id.sign_in_username);
        signInPasswordView = (EditText) findViewById(R.id.sign_in_password);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        enterWithoutSignIn = (TextView) findViewById(R.id.open_without_signin);
        Button btn_signIn = (Button) findViewById(R.id.btn_signin);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputUsername = signInUsernameView.getText().toString();
                inputPassword = signInPasswordView.getText().toString();
                switch (checkUserInfo(inputUsername, inputPassword)) {

                    case -1:
                        Toast.makeText(MainActivity_sign_in.this, "This user does " +
                                "not exist.", Toast.LENGTH_SHORT).show();
                        break;
                    case -2: {
                        Toast.makeText(MainActivity_sign_in.this, "Your password does not " +
                                "correct.", Toast.LENGTH_SHORT).show();
                        //the user name is correct, so this user name can be used
                        userName = inputUsername;
                        break;
                    }
                    default: {
                        //this id can be used in other activity
                        userId = checkUserInfo(inputUsername, inputPassword);
                        Intent intent = new Intent(MainActivity_sign_in.this, SelectActivity.class);
                        //pass userId to next activity
                        intent.putExtra("User Id", userId);
                        startActivity(intent);
                        Toast.makeText(MainActivity_sign_in.this, "Welcome " + inputUsername + "! \nRead in 4015 stocks, ready to search!" ,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Button btn_signUp = (Button) findViewById(R.id.sign_up1);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_sign_in.this, Page_sign_up.class);
                startActivity(intent);
            }
        });
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                hintDialog(hintQuestion, hintAnswer);
            }
        };
        setTextViewClick(forgetPassword, forgetPass, 0, forgetPass.length(), clickableSpan1);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                userId = 0;
                Toast.makeText(MainActivity_sign_in.this, "Read in 4015 stocks, ready to search!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity_sign_in.this, SelectActivity.class);
                startActivity(intent);
            }
        };
        setTextViewClick(enterWithoutSignIn, enterWithout, 0, enterWithout.length(), clickableSpan2);
    }

    /**
     * the dialog to check whether the hint question and hint answer is correct
     *
     * @param hintQuestion
     * @param hintAnswer
     */
    private void hintDialog(final String hintQuestion, final String hintAnswer) {
        LayoutInflater factory = LayoutInflater.from(MainActivity_sign_in.this);
        final View view = factory.inflate(R.layout.forget_dialog, null);
        final EditText edit = (EditText) view.findViewById(R.id.hint_answer_input);
        final TextView text = (TextView) view.findViewById(R.id.hint_question_text);
        text.setText(hintQuestion);

        final AlertDialog mDialog = new AlertDialog.Builder(this).setPositiveButton("Check", null).setNegativeButton("Cancel", null).create();
        mDialog.setTitle("Hint Question:");
        mDialog.setView(view);
        mDialog.setCancelable(false);
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = mDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String inputAnswer = edit.getText().toString();
                        if (hintAnswer.equals(inputAnswer)) {
                            Toast.makeText(MainActivity_sign_in.this, "Your answer is correct!", Toast.LENGTH_SHORT).show();
                            resetPasswordDialog();
                            mDialog.dismiss();

                        } else {
                            Toast.makeText(MainActivity_sign_in.this, "Your answer is incorrect!", Toast.LENGTH_SHORT).show();
                            edit.setText("");
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
     * dialog to reset password
     */
    private void resetPasswordDialog() {
        LayoutInflater factory = LayoutInflater.from(MainActivity_sign_in.this);
        final View view = factory.inflate(R.layout.reset_password_dialog, null);
        final TextView text = (TextView) view.findViewById(R.id.reset_textview);
        final EditText newPassword1 = (EditText) view.findViewById(R.id.reset_password1);
        final EditText newPassword2 = (EditText) view.findViewById(R.id.reset_password2);

        final AlertDialog mDialog = new AlertDialog.Builder(this).setPositiveButton("Reset", null).setNegativeButton("Cancel", null).create();
        mDialog.setTitle("Reset Password");
        mDialog.setView(view);
        mDialog.setCancelable(false);
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = mDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String new1 = newPassword1.getText().toString();
                        String new2 = newPassword2.getText().toString();
                        System.out.println(new1);
                        if (new1.equals(new2)) {
                            if (updateDatabase("Users", "password", "name", new1, userName)) {
                                Toast.makeText(MainActivity_sign_in.this, "Reset successful", Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(MainActivity_sign_in.this, "The two password are not consistent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity_sign_in.this, "dismiss", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }
                });
            }
        });
        mDialog.show();
    }

    /**
     * below EditTex there is a TextView "forget password?", try to add a ClickListener to this text
     *
     * @param textView the textView we need to add ClickListener to
     * @param text     the text content
     * @param start    the first letter of the click part
     * @param end      the last letter of the click part
     */
    private void setTextViewClick(TextView textView, String text, int start, int end, ClickableSpan clickableSpan) {
        final SpannableStringBuilder style = new SpannableStringBuilder();
        //set text
        style.append(text);

        style.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(style);

        //set Text Color
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#D5D5D5"));
        style.setSpan(foregroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //set to TextView
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(style);
    }


    /**
     * check whether this user exists and whether his/her password is correct
     *
     * @param inputUsername
     * @param inputPassword
     * @return -1: this user does not exits
     * -2: this user's password is not correct
     */
    private int checkUserInfo(String inputUsername, String inputPassword) {
        Cursor cursor = db.rawQuery("select * from Users where name = ?", new String[]{inputUsername});
        cursor.moveToFirst();
        //if database has this user
        if (cursor.getCount() != 0) {
            String truePassword = cursor.getString(cursor.getColumnIndex("password"));
            hintQuestion = cursor.getString(cursor.getColumnIndex("hintQuestion"));
            hintAnswer = cursor.getString(cursor.getColumnIndex("hintAnswer"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            if (inputPassword.equals(truePassword)) {
                return id;
            } else {
                return -2;
            }
        }
        cursor.close();
        return -1;
    }

    /**
     * update data in the database
     *
     * @param tableName          the table need to by update
     * @param attributeName      the attribute need to be update
     * @param conditionAttribute condition's attribute
     * @param setValue           the value we want to use to replace the original value
     * @param conditionValue     condition's value
     * @return
     */
    private boolean updateDatabase(String tableName, String attributeName, String conditionAttribute, String setValue, String conditionValue) {
        db.execSQL("update " + tableName + " set " + attributeName + " = ? where " + conditionAttribute + " = ?", new String[]{setValue, conditionValue});
        return true;
    }

    private void iniDatabaseReader() {
        /**
         * Author:Yuliang Ma
         * In the initial step ,we read in the promoting information for a csv file.
         */

        int row = 0;
        int colum = 0;
        ArrayList<String> allItems = new ArrayList<>();
        ArrayList<Company_Index> allPromotedComps = new ArrayList<>();

        Database_reader d = runDatabaseReader();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.promoted_comps), "GB2312"));
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
        /**
         * Author:Yuchen Wang, Yuliang Ma
         */

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

