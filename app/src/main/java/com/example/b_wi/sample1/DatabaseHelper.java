package com.example.b_wi.sample1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "neer.db";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    // Table Names
    private static final String USER_TABLE = "user1";
    private static final String ORDER_TABLE = "orders";


    private static final String CREATE_TABLE_ACCOUNTS = "CREATE TABLE "
            +USER_TABLE + "( UserId INTEGER PRIMARY KEY AUTOINCREMENT,password TEXT, phone TEXT)";

    private static final String CREATE_TABLE_ORDER = "create table if not exists "+ORDER_TABLE+" ( uid INTEGER, orderid INTEGER ,name TEXT,quantity INTEGER,amount INTEGER,location TEXT )";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNTS);
        db.execSQL(CREATE_TABLE_ORDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUser(User s){
        ContentValues c = new ContentValues();
        c.put("password",s.getPwd());
        c.put("phone",s.getPhone());
        SQLiteDatabase d = this.getWritableDatabase();
        d.insert("user1",null,c);

    }

    public void addOrder(order o){
        ContentValues c = new ContentValues();
        c.put("uid",o.getUid());
        c.put("orderid",o.getOid());
        c.put("name",o.getName());
        c.put("quantity",o.getNoOfCans());
        c.put("amount",o.getAmount());
        c.put("location",o.getLocation());
        SQLiteDatabase d = this.getWritableDatabase();
        d.insert("orders",null,c);
    }

    public User findHandler(String em){
        String find = "SELECT * FROM user1 WHERE phone = "+"'"+ em+"'";

        SQLiteDatabase f = this.getWritableDatabase();
        Cursor c = f.rawQuery(find,null);
        //create a User object to store the data that you are sending
        User User = new User();
        //checks if the result contains any valid row using moveToFirst func
        if(c.moveToFirst()) {
            c.moveToFirst();
            User.setUid(Integer.parseInt(c.getString(0)));
            User.setPwd(c.getString(1));
            User.setPhone(c.getString(2));
            c.close();
        }
        else {
            User=null;
        }
        return User;
    }

    public order findOrder(int oid){
        String find = "SELECT * FROM orders WHERE orderid = "+"'"+ oid+"'";

        SQLiteDatabase f = this.getWritableDatabase();
        Cursor c = f.rawQuery(find,null);
        //create a User object to store the data that you are sending
        order t = new order();
        //checks if the result contains any valid row using moveToFirst func
        if(c.moveToFirst()) {
            c.moveToFirst();
            t.setUid(Integer.parseInt(c.getString(0)));
            t.setOid(Integer.parseInt(c.getString(1)));
            c.close();
        }
        else {
            t=null;
        }
        return t;
    }

    public boolean cancelOrder(int oid){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete("orders", "orderid = " + oid, null) > 0;
    }

    public boolean updatePassword(int uid,String newPass){
        SQLiteDatabase s = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("password",newPass);
        int res=s.update("user1",c,"UserId="+uid,null);
        if(res==1)
            return true;
        else
            return false;
    }
}