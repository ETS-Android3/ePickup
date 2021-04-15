package com.example.epickup;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.epickup.data.model.UserModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    UserModel userModel;
    public DatabaseHelper(@Nullable Context context) {
        super(context, "ePDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUserTable = "CREATE TABLE USER (Id INTEGER PRIMARY KEY AUTOINCREMENT, RoleId INTEGER, RestaurantID INTEGER, Name TEXT, Email TEXT, Password TEXT, CurrentLogin INTEGER)";
        String createRoleTable = "CREATE TABLE ROLE (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT)";
        String createRestaurantTable = "CREATE TABLE RESTAURANT (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Location TEXT)";

        String[] createTables = new String[]{createUserTable, createRoleTable, createRestaurantTable};

        for(String sql : createTables){
            db.execSQL(sql);
        }

        String insertRole = "INSERT INTO  ROLE (Name) VALUES (?)";
        db.execSQL(insertRole,new String[]{"User"});
        db.execSQL(insertRole,new String[]{"Manager"});

    }

    public boolean register(UserModel userModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("RoleId", userModel.getRoleId());
        cv.put("RestaurantID", userModel.getRestaurantID());
        cv.put("Name", userModel.getName());
        cv.put("Email", userModel.getEmail());
        cv.put("Password", userModel.getPassword());
        cv.put("CurrentLogin", userModel.getCurrentLogin());

        long insert = db.insert("User",null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int registerRestaurant(String[] list){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Name", list[0]);
        cv.put("Location", list[1]);

        int insert = (int) db.insert("Restaurant",null, cv);
        return insert;
    }

    public String selectAllUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<UserModel> userList = new ArrayList<>();

        String query = "SELECT * FROM User";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                int roleId = cursor.getInt(1);
                int restaurantId = cursor.getInt(2);
                String name = cursor.getString(3);
                String email = cursor.getString(4);
                String password = cursor.getString(5);
                int currLogin = cursor.getInt(6);

                UserModel userModel = new UserModel(id, roleId, restaurantId, name, email, password, currLogin);
                userList.add(userModel);
            }while(cursor.moveToNext());
        }

        Log.e("userList",userList.toString());
        cursor.close();
        db.close();
        return "";
    }

    public String selectAllRole() {
        SQLiteDatabase db = this.getReadableDatabase();
        List roleList = new ArrayList<>();

        String query = "SELECT * FROM Role";
        Cursor cursor = db.rawQuery(query, null);

        String[] roleRec;
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);

                roleRec = new String[]{String.valueOf(id), name};
                Log.e("roleList",roleRec.toString());

                roleList.add(roleRec);
            }while(cursor.moveToNext());
        }

//        Log.e("roleList",roleList.toString());
        cursor.close();
        db.close();
        return "";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
