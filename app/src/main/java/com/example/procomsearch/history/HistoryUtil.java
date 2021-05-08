package com.example.procomsearch.history;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.procomsearch.Company;
import com.example.procomsearch.CompanyFactory;

import java.util.ArrayList;

/**
 * @author Chaofan Li
 * this class is used for storeing history data
 */

public class HistoryUtil {
    //browse history
    private static CompanyFactory history = new CompanyFactory();
    //search history
    private static ArrayList<String> searchHistories = new ArrayList<>();

    private static SQLiteDatabase db;

    public static void initUtil(){
        db = SQLiteDatabase.openOrCreateDatabase("data/data/"+"com.example.procomsearch"+"/databases/"+"Users.db",null);
    }

    public static void addHistory(Company company){
        if (!checkRedundant(company)) {
            history.add(company);
        }
    }

    public static void clearHistory(int userId, String tableName){
        db.execSQL("delete from "+tableName+" where userId = ?",new String[]{userId+""});
        history.clear();
    }

    public static boolean writeToDatabase(int userId){
        for (Company c:history
             ) {
            System.out.println("history:"+c.getName());
            if (!checkCompanyIsInDatabase(c,userId)){
                db.execSQL("insert into History (companyName, stockNo, userId) values(?,?,?)", new String[]{
                        c.getName(),c.getStockNo(),userId+""
                });
            }
        }
        System.out.println("history is successfully added!!!!!!!!!!!!!!!!!!");
        return true;
    }

    private static boolean checkCompanyIsInDatabase(Company company, int userId){
        Cursor cursor = db.rawQuery("select stockNo from History where userId = ? and stockNo = ?",new String[]{userId+"",company.getStockNo()});
        if (cursor.getCount()!=0){
            return true;
        }
        cursor.close();
        return false;
    }

    public static CompanyFactory getHistory(){
        return history;
    }

    private static boolean checkRedundant(Company company){
        return history.contains(company);
    }

    public static boolean readFromDatabase(int userId){
        history.clear();
        Cursor cursor = db.rawQuery("select * from History where userId = ?",new String[]{userId+""});
        if (cursor.moveToFirst()){
            do{
                String companyName = cursor.getString(cursor.getColumnIndex("companyName"));
                String stockNo = cursor.getString(cursor.getColumnIndex("stockNo"));
                history.add(new Company(companyName,stockNo,null,0.0));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return true;
    }


    public static void addSearchHistory(String input, int userId){
        if (!inputSearchInDatabase(input,userId)) {
            db.execSQL("insert into SearchHistory (history, userId) values(?,?)", new String[]{input, userId + ""});
        }
    }

    private static boolean inputSearchInDatabase(String input, int userId){
        Cursor cursor = db.rawQuery("select * from SearchHistory where userId = ? and history = ?",new String[]{
                userId+"",input
        });
        if (cursor.getCount()!=0){
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * read search history
     * @param userId
     * @return
     */
    public static boolean readFromSearchHistory(int userId){
        searchHistories.clear();
        Cursor cursor = db.rawQuery("select * from SearchHistory where userId = ?",new String[]{userId+""});
        if (cursor.moveToFirst()){
            do{
                String history = cursor.getString(cursor.getColumnIndex("history"));
                searchHistories.add(history);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return true;
    }

    public static ArrayList<String> getSearchHistories() {
        return searchHistories;
    }
}
