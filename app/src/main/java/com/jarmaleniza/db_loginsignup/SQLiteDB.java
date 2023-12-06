package com.jarmaleniza.db_loginsignup;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLiteDB extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "accounts_list.db";
    static final String ACCOUNTS = "account", ACCID = "acc_id", ACCUSERNAME = "acc_username", ACCPASSWORD = "acc_password", ACCTYPE = "acc_type", ACCVERIFIED = "acc_verified";
    static final String PROFILE = "profile", USERID = "user_id", USERFNAME = "user_fname", USERLNAME = "user_lname", USERGENDER = "user_gender", USERADDRESS = "user_address", USEREMAIL  = "acc_email", USERCONTACT = "acc_contact";
    static ArrayList<String> ToAdd;
    static ArrayList<Integer> ToAddCount;
    ContentValues VALUES, VALUE;
    Cursor cs;
    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ACCOUNTS + " (" + ACCID + " Integer PRIMARY KEY  AUTOINCREMENT, " + ACCUSERNAME + " TEXT, " + ACCPASSWORD + " TEXT, " + ACCTYPE + " TEXT, " + ACCVERIFIED + " TEXT)");
        db.execSQL("CREATE TABLE " + PROFILE + " (" + USERID + " Integer PRIMARY KEY AUTOINCREMENT, " + USERFNAME + " TEXT, " + USERLNAME + " TEXT, " + USERGENDER + " TEXT, " + USERADDRESS + " TEXT, " + USEREMAIL + " TEXT, " + USERCONTACT + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    public boolean AddRecords(String Fname, String Lname, String Gender, String Address, String Email, String Contact, String Username, String Password){
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        VALUE = new ContentValues();
        VALUE.put(USERFNAME, Fname);
        VALUE.put(USERLNAME, Lname);
        VALUE.put(USERGENDER, Gender);
        VALUE.put(USERADDRESS, Address);
        VALUE.put(USEREMAIL, Email);
        VALUE.put(USERCONTACT, Contact);
        db.insert(PROFILE, null, VALUE);
        VALUES = new ContentValues();
        VALUES.put(ACCUSERNAME, Username);
        VALUES.put(ACCPASSWORD, Password);
        VALUES.put(ACCTYPE, "Guest");
        VALUES.put(ACCVERIFIED, 0);
        db.insert(ACCOUNTS, null, VALUES);
        return true;
    }

    @SuppressLint("Range")
    public ArrayList<String> GetGuestList(){
        ToAdd = new ArrayList<String>();
        ToAddCount = new ArrayList<Integer>();
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
        cs = db.rawQuery("SELECT * FROM " + PROFILE, null);
        cs.moveToFirst();
        while(cs.isAfterLast()==false){
            ToAddCount.add(cs.getInt(cs.getColumnIndex(USERID)));
            ToAdd.add(cs.getString(cs.getColumnIndex(USERID)) + " " + cs.getString(cs.getColumnIndex(USERFNAME)) + " " + cs.getString(cs.getColumnIndex(USERLNAME)));
            cs.moveToNext();
        }
        return ToAdd;
    }

    public boolean recordExists(String Username){
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + ACCOUNTS + " WHERE " + ACCUSERNAME + " = ?";
        String[] selectionArgs = {Username};
        cs = db.rawQuery(query, selectionArgs);
        boolean exists = cs.moveToFirst();
        return exists;
    }

    @SuppressLint("Range")
    public String[] getUserRecordByIndex(int dataIndex) {
        android.database.sqlite.SQLiteDatabase conn = this.getReadableDatabase();
        String[] userRecord = null;

        String[] columns = {USERFNAME, USERLNAME, USERGENDER, USERADDRESS, USEREMAIL, USERCONTACT};
        String selection = USERID + " = ?";
        String[] selectionArgs = {String.valueOf(dataIndex)};
        cs = conn.query(PROFILE, columns, selection, selectionArgs, null, null, null);
        if (cs.moveToFirst()) {
            userRecord = new String[]{
                    cs.getString(cs.getColumnIndex(USERFNAME)),
                    cs.getString(cs.getColumnIndex(USERLNAME)),
                    cs.getString(cs.getColumnIndex(USERGENDER)),
                    cs.getString(cs.getColumnIndex(USERADDRESS)),
                    cs.getString(cs.getColumnIndex(USEREMAIL)),
                    cs.getString(cs.getColumnIndex(USERCONTACT))
            };
        }



        cs.close();
        conn.close();
        return userRecord;
    }

    @SuppressLint("Range")
    public String[] getAccountRecordByIndex(int dataIndex) {
        android.database.sqlite.SQLiteDatabase conn = this.getReadableDatabase();
        String[] accRecord = null;

        String[] columns = {ACCUSERNAME, ACCPASSWORD, ACCVERIFIED};
        String selection = ACCID + " = ?";
        String[] selectionArgs = {String.valueOf(dataIndex)};
        cs = conn.query(ACCOUNTS, columns, selection, selectionArgs, null, null, null);
        if (cs.moveToFirst()) {
            accRecord = new String[]{
                    cs.getString(cs.getColumnIndex(ACCUSERNAME)),
                    cs.getString(cs.getColumnIndex(ACCPASSWORD)),
                    cs.getString(cs.getColumnIndex(ACCVERIFIED))
            };
        }
        cs.close();
        conn.close();
        return accRecord;
    }

    @SuppressLint("Range")
    public int getUserId(String Username, String Password){
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
        Integer id = 0;

        String[] columns = {ACCID, ACCUSERNAME, ACCPASSWORD};
        String selection = ACCUSERNAME + " = ?" + " AND " + ACCPASSWORD + " = ?";
        String[] selectionArgs = {Username, Password};
        cs = db.query(ACCOUNTS, columns, selection, selectionArgs, null, null, null);

        if(cs.moveToFirst()){
            id = cs.getInt(cs.getColumnIndex(ACCID));
        }
        return id;
    }
    @SuppressLint("Range")
    public int checkLogin(String Username, String Password){
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
        Integer state = 0;

        String[] columns = {ACCUSERNAME, ACCPASSWORD, ACCVERIFIED};
        String selection = ACCUSERNAME + " = ?" + " AND " + ACCPASSWORD + " = ?";
        String[] selectionArgs = {Username, Password};
        cs = db.query(ACCOUNTS, columns, selection, selectionArgs, null, null, null);

        if(cs.moveToFirst()){
            String useracc = cs.getString(cs.getColumnIndex(ACCUSERNAME));
            String userpass = cs.getString(cs.getColumnIndex(ACCPASSWORD));
            Integer status = cs.getInt(cs.getColumnIndex(ACCVERIFIED));

            if(status == 1){
                if (Username.equals(useracc) && Password.equals(userpass)){
                    state = 1;
                }
                else if(Username.equals(useracc) || Password.equals(userpass)){
                    state = 3;
                }
            }
            else{
                state = 2;
            }
        }

        return state;
    }

    @SuppressLint("Range")
    public boolean checkAdmin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {ACCUSERNAME, ACCPASSWORD, ACCTYPE};
        String selection = ACCUSERNAME + " = ? AND " + ACCPASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cs = db.query(ACCOUNTS, columns, selection, selectionArgs, null, null, null);

        boolean isAdmin = false;

        if (cs != null && cs.moveToFirst()) {
            String accountType = cs.getString(cs.getColumnIndex(ACCTYPE));
            isAdmin = "Admin".equals(accountType);
        }

        if (cs != null) {
            cs.close();
        }

        db.close();

        return isAdmin;
    }

    @SuppressLint("Range")
    public boolean checkAdminStatus(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {ACCID, ACCUSERNAME, ACCPASSWORD, ACCTYPE};
        String selection = ACCID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        cs = db.query(ACCOUNTS, columns, selection, selectionArgs, null, null, null);

        boolean isAdmin = false;

        if (cs != null && cs.moveToFirst()) {
            String accountType = cs.getString(cs.getColumnIndex(ACCTYPE));
            isAdmin = "Admin".equals(accountType);
        }

        if (cs != null) {
            cs.close();
        }

        db.close();

        return isAdmin;
    }



    public boolean updateRecord(int index, String Fname, String Lname, String Gender, String Address, String Email, String Contact, String Password, String Status) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        VALUES = new ContentValues();
        VALUES.put(USERFNAME, Fname);
        VALUES.put(USERLNAME, Lname);
        VALUES.put(USERGENDER, Gender);
        VALUES.put(USERADDRESS, Address);
        VALUES.put(USEREMAIL, Email);
        VALUES.put(USERCONTACT, Contact);
        VALUE = new ContentValues();
        VALUE.put(ACCPASSWORD, Password);
        VALUE.put(ACCVERIFIED, Status);

        String selection = USERID + " = ?";
        String[] selectionArgs = {String.valueOf(index)};
        String selected = ACCID + " = ?";

        int rowsAffected = db.update(PROFILE, VALUES, selection, selectionArgs);
        db.update(ACCOUNTS, VALUE, selected, selectionArgs);

        db.close();
        return rowsAffected > 0;
    }

    public boolean createAdmin(){
        SQLiteDatabase db = this.getReadableDatabase();
        cs = db.rawQuery("SELECT * FROM " + ACCOUNTS + " WHERE " + ACCTYPE + " = 'Admin'", null);
        Long rowc = (long) -1;

        if(!cs.moveToFirst()){
            VALUE = new ContentValues();
            VALUE.put(USERFNAME, "UserAdmin");
            VALUE.put(USERLNAME, "Admin");
            VALUE.put(USERGENDER, "Male");
            VALUE.put(USERADDRESS, "Admin");
            VALUE.put(USEREMAIL, "admin@gmail.com");
            VALUE.put(USERCONTACT, "09123456789");
            db.insert(PROFILE, null, VALUE);
            VALUES = new ContentValues();
            VALUES.put(ACCUSERNAME, "admin");
            VALUES.put(ACCPASSWORD, "admin");
            VALUES.put(ACCTYPE, "Admin");
            VALUES.put(ACCVERIFIED, 1);


            rowc = db.insert(ACCOUNTS, null, VALUES);
        }

        if (rowc == -1) {
            Log.e("insert admin", "failed");
            return false;

        }
        Log.i("insert admin", "success");
        return true;
    }
}
