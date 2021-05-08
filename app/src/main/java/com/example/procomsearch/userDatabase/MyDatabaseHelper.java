package com.example.procomsearch.userDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * @author Chaofan Li
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    /**
     * the SQL of user table
     */
    private static final String CREATE_USER = "create table Users(" +
            "id integer primary key autoincrement,"+
            "name text," +
            "password text," +
            "hintQuestion text," +
            "hintAnswer)";
    /**
     * the SQL of user's history, can use the stock number to search companies in the tree
     */
    private static final String CREATE_HISTORY = "create table History(" +
            "id integer primary key autoincrement," +
            "companyName text,"+
            "stockNo text," +
            "userId integer)";
    /**
     * the SQL of user's favorite, can use the stock number to search companies in the tree
     */
    private static final String CREATE_FAVORITE = "create table Favorite(" +
            "id integer primary key autoincrement," +
            "companyName text,"+
            "stockNo text," +
            "userId integer)";

    private static final String CREATE_SEARCH_HISTORY = "create table SearchHistory(" +
            "id integer primary key autoincrement," +
            "history text,"+
            "userId integer)";

    private Context mContext;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_HISTORY);
        db.execSQL(CREATE_FAVORITE);
        db.execSQL(CREATE_SEARCH_HISTORY);
        Toast.makeText(mContext,"All 4 tables are created",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
