package com.example.procomsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.Toast;

/**
 * @author Chaofan Li
 */
public class Page_sign_up extends AppCompatActivity {

    SQLiteDatabase db;
    private static String username;
    private static String password1;
    private static String password2;
    private static String hintQuestion;
    private static String hintAnswer;
    private EditText nameView, password1View, password2View, hintQuestionView, hintAnswerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_sign_up);
        //get the Users database
        db = SQLiteDatabase.openOrCreateDatabase("data/data/"+getPackageName()+"/databases/"+getString(R.string.database_name),null);
        nameView = (EditText)findViewById(R.id.et_account_name);
        password1View = (EditText)findViewById(R.id.et_password1);
        password2View = (EditText)findViewById(R.id.et_password2);
        hintQuestionView = (EditText)findViewById(R.id.et_hint);
        hintAnswerView = (EditText)findViewById(R.id.hint_answers_view);


        Button signUp_btn = (Button)findViewById(R.id.sign_up2);
        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = nameView.getText().toString();
                password1 = password1View.getText().toString();
                password2 = password2View.getText().toString();
                hintQuestion = hintQuestionView.getText().toString();
                hintAnswer = hintAnswerView.getText().toString();
                if (!password2.equals(password1)){
                    Toast.makeText(Page_sign_up.this, "The two password are not consistent", Toast.LENGTH_SHORT).show();
                }else if(username.equals("")||password1.equals("")||password2.equals("")||hintQuestion.equals("")||hintAnswer.equals("")){
                    Toast.makeText(Page_sign_up.this,"You need to fill all the details.",Toast.LENGTH_LONG).show();
                }else {
                    if(writeUserToDatabase(username,password1,hintQuestion,hintAnswer)){
                        Toast.makeText(Page_sign_up.this,"User is added!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

        /**
         * if the username has already exist, the textView will become red
         */
        hintQuestionView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showListPopupWindow();
                }
            }
        });


        nameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String inputUsername = nameView.getText().toString();
                    if (usernameIsInDatabase(inputUsername)){
                        Toast.makeText(Page_sign_up.this,"This username has already exits.",
                                Toast.LENGTH_SHORT).show();
                        nameView.setBackground(getDrawable(R.drawable.shape_edit_alert));
                    }
                    else {
                        nameView.setBackground(getDrawable(R.drawable.shape_edit_normal));
                    }
                }
            }
        });
    }

    private void showListPopupWindow() {
        final String[] hintQuestions = {
                "Name of your school?",
                "Mother's birthday?",
                "Name of your first pet?"
        };
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,hintQuestions));
        listPopupWindow.setAnchorView(hintQuestionView);
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hintQuestionView.setText(hintQuestions[position]);
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();

    }

    /**
     * write user info to database
     * @param username
     * @param password1
     * @param hintQuestion
     * @param hintAnswer
     * @return
     */
    private boolean writeUserToDatabase(String username, String password1, String hintQuestion,String hintAnswer){
        db.execSQL("insert into Users (name, password, hintQuestion, hintAnswer) values(?,?,?,?)",
                new String[]{username,password1,hintQuestion,hintAnswer});
        return true;
    }

    /**
     * check whether this username is in database
     * @param username
     * @return
     */
    private boolean usernameIsInDatabase(String username){
        Cursor cursor = db.rawQuery("select name from Users where name = ?", new String[]{username});
        cursor.moveToFirst();
        //if the cursor doesn't have any row in it, this username is never used
        if (cursor.getCount()!=0){
            return true;
        }
        cursor.close();
        return false;
    }
}