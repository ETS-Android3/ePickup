package com.example.epickup;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.epickup.data.model.OrderItemModel;
import com.example.epickup.data.model.OrderModel;
import com.example.epickup.data.model.RestaurantModel;
import com.example.epickup.data.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static android.content.Context.MODE_PRIVATE;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;

    Gson gson = new Gson();
//        String asd = preferences.getString("key", String.valueOf(MODE_PRIVATE));
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("key", "value");
//        editor.commit();








    UserModel userModel;
    SharedPreferences sp;

    public DatabaseHelper(@Nullable Context context) {
        super(context, "ePDB", null, 1);
        this.context = context;
        this.sp = context.getSharedPreferences("sp", MODE_PRIVATE);
    }

//    = context.getSharedPreferences( "sp", MODE_PRIVATE);

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE USER (Id INTEGER PRIMARY KEY AUTOINCREMENT, RoleId INTEGER, RestaurantID INTEGER, Name TEXT, Email TEXT, Password TEXT, CurrentLogin INTEGER)";
        String createRoleTable = "CREATE TABLE ROLE (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT)";
        String createRestaurantTable = "CREATE TABLE RESTAURANT (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Location TEXT)";
        String createOrderTable = "CREATE TABLE ORDERS (Id INTEGER PRIMARY KEY AUTOINCREMENT, RestaurantId INTEGER, UserId INTEGER, Payment INTEGER, Status TEXT, EstimatedTime TEXT, Time TEXT, Feedback TEXT)";
        String createOrderItemTable = "CREATE TABLE ORDERITEM (Id INTEGER PRIMARY KEY AUTOINCREMENT, MenuItemId INTEGER, OrderId INTEGER, Quantity INTEGER)";
        String MenuItemTable = "CREATE TABLE MENUITEM (Id INTEGER PRIMARY KEY AUTOINCREMENT, RestaurantId INTEGER, Name TEXT)";

        String[] createTables = new String[]{createUserTable, createRoleTable, createRestaurantTable, createOrderTable, createOrderItemTable, MenuItemTable};

        for(String sql : createTables){
            db.execSQL(sql);
        }

        String insertRole = "INSERT INTO  ROLE (Name) VALUES (?)";
        db.execSQL(insertRole,new String[]{"User"});
        db.execSQL(insertRole,new String[]{"Manager"});


        // Dummy Data
        String insertMenu = "INSERT INTO  MENUITEM (Id, RestaurantId, Name) VALUES (?,?,?)";
        db.execSQL(insertMenu,new Object[]{1, 1, "item1"});
        db.execSQL(insertMenu,new Object[]{2, 1, "item2"});

        String insertOrder = "INSERT INTO  Orders (Id, RestaurantId, UserId, Payment, Status, EstimatedTime, Time, Feedback) VALUES (?,?,?,?,?,?,?,?)";
        db.execSQL(insertOrder,new Object[]{1, 1, 3, 50, "accepted", "estimatedTime1", "time1", "feedback1"});

        String insertOrderItem = "INSERT INTO  ORDERITEM (Id, MenuItemId, OrderId, Quantity) VALUES (?,?,?,?)";
        db.execSQL(insertOrderItem,new Object[]{1, 1, 1, 2});
        db.execSQL(insertOrderItem,new Object[]{2, 2, 1, 1});

        // Time calculation
//        try {
//            final long millisToAdd = 3_600_000; //one hour
//            DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//            String currentDateTime = format.format(new Date());
//            Date d = format.parse(currentDateTime);
//            d.setTime(d.getTime() + millisToAdd);
//            String oneHrAddedString = format.format(d);
//        } catch (ParseException e){
//            e.printStackTrace();
//        }

    }

    public boolean register(UserModel userModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("RoleId", userModel.getRoleId());
        cv.put("RestaurantID", userModel.getRestaurantId());
        cv.put("Name", userModel.getName());
        cv.put("Email", userModel.getEmail());
        cv.put("Password", userModel.getPassword());
        cv.put("CurrentLogin", userModel.getCurrentLogin());

        long insert = db.insert("User",null, cv);
        db.close();
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
        db.close();
        return insert;
    }


    public boolean login(String[] list){
        SQLiteDatabase db = this.getReadableDatabase();

        String findQuery = "SELECT * FROM User WHERE Email = ? AND Password = ?";
        Cursor cursor = db.rawQuery(findQuery, list);
        int count = cursor.getCount();
        if(count>0){
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            int roleId = cursor.getInt(1);
            int restaurantId = cursor.getInt(2);
            String name = cursor.getString(3);
            String email = cursor.getString(4);
            String password = cursor.getString(5);
            int currLogin = cursor.getInt(6);

            UserModel userModel = new UserModel(id, roleId, restaurantId, name, email, password, currLogin);
//            Gson gson = new Gson();

            String jsonString = gson.toJson(userModel);

//            UserModel uM = gson.fromJson(jsonString, UserModel.class);

//            SharedPreferences sp = context.getSharedPreferences("sp", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("userObject", jsonString);
            editor.commit();

//            String asd = sp.getString("userObject", String.valueOf(MODE_PRIVATE));

//            String asd = sp.getString("userObject123", String.valueOf(MODE_PRIVATE));
//            if(asd.equals("0")){
//            } else{
//            }


            SQLiteDatabase db2 = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("CurrentLogin",1);
            db2.update("User", cv, "Id = ?", new String[]{String.valueOf(id)});
            db2.close();
            return true;
        } else {
            return false;
        }
    }

    public boolean logout(boolean val) {
        try {
            String userObjectString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
            UserModel userObject = gson.fromJson(userObjectString, UserModel.class);
            int id = userObject.getId();

            SQLiteDatabase db2 = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("CurrentLogin", 0);
            db2.update("User", cv, "Id = ?", new String[]{String.valueOf(id)});
            db2.close();

            SharedPreferences.Editor editor = sp.edit();
            editor.remove("userObject");
            editor.commit();

            String asd = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
            return true;
        } catch (Exception e){
            return false;
        }
    }


    public List viewOrder() {
        List<JSONObject> retObj = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        SQLiteDatabase db = this.getReadableDatabase();
        List<OrderModel> orderList = new ArrayList<>();
        List<RestaurantModel> restaurantList = new ArrayList<>();

        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);
        String query = "SELECT * FROM Orders where UserId = ?";
        String[] args = {String.valueOf(uM.getId())};
        Cursor cursor = db.rawQuery(query, args);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                int restaurantId = cursor.getInt(1);
//                int UserId = cursor.getInt(2);
//                int payment = cursor.getInt(3);
                String status = cursor.getString(4);
                String estimatedTime = cursor.getString(5);
                String time = cursor.getString(6);
//                String feedback = cursor.getString(7);
//                OrderModel orderModel = new OrderModel(id, restaurantId, UserId, payment, status, time, feedback);
//                orderList.add(orderModel);


                String query2 = "SELECT * FROM RESTAURANT where Id = ?";
                String[] args2 = {String.valueOf(restaurantId)};
                Cursor cursor2 = db.rawQuery(query2, args2);
                String restaurantName = "";
                if(cursor2.moveToFirst()) {
//                        int restId = cursor2.getInt(0);
                        restaurantName = cursor2.getString(1);
//                        String restaurantLoc = cursor2.getString(2);
//                        RestaurantModel restaurantModel = new RestaurantModel(restId, restaurantName, restaurantLoc);
//                        restaurantList.add(restaurantModel);
                }

                String query3 = "SELECT * FROM ORDERITEM where OrderId = ?";
                String[] args3 = {String.valueOf(id)};
                Cursor cursor3 = db.rawQuery(query3, args3);

                String orderMenuString = "";
                if(cursor3.moveToFirst()) {
                    do {
                        int Id = cursor3.getInt(0);
                        int menuItemId = cursor3.getInt(1);
                        int orderId = cursor3.getInt(2);
                        int quantity = cursor3.getInt(3);

                        String query4 = "SELECT * FROM MENUITEM where Id = ?";
                        String[] args4= {String.valueOf(menuItemId)};
                        Cursor cursor4 = db.rawQuery(query4, args4);
                        String menuItemName = "";
                        if(cursor4.moveToFirst()) {
                            menuItemName = cursor4.getString(2);
                        }
                        orderMenuString = orderMenuString + quantity + "x " + menuItemName;
                        if(!cursor3.isLast()){
                            orderMenuString = orderMenuString +  ", ";
                        }
                    } while (cursor3.moveToNext());
                }

                jsonObject = new JSONObject();
                try {
                    jsonObject.put("restaurantName", restaurantName);
                    jsonObject.put("orderId", id);
                    jsonObject.put("orderMenu", orderMenuString);
                    jsonObject.put("status", status);
                    jsonObject.put("estimatedTime", estimatedTime);
                    jsonObject.put("time", time);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                retObj.add(jsonObject);

            }while(cursor.moveToNext());
        }



        return retObj;
    }

    public List receivedOrder() {
        List<JSONObject> retObj = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        SQLiteDatabase db = this.getReadableDatabase();
        List<OrderModel> orderList = new ArrayList<>();
        List<RestaurantModel> restaurantList = new ArrayList<>();

        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);
        Log.e("restId", String.valueOf(uM.getRestaurantId()));

        String query = "SELECT * FROM Orders where RestaurantId = ?";
        String[] args = {String.valueOf(uM.getRestaurantId())};
        Cursor cursor = db.rawQuery(query, args);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                int restaurantId = cursor.getInt(1);
//                int UserId = cursor.getInt(2);
//                int payment = cursor.getInt(3);
                String status = cursor.getString(4);
                String estimatedTime = cursor.getString(5);
                String time = cursor.getString(6);
//                String feedback = cursor.getString(7);
//                OrderModel orderModel = new OrderModel(id, restaurantId, UserId, payment, status, time, feedback);
//                orderList.add(orderModel);


//                String query2 = "SELECT * FROM RESTAURANT where Id = ?";
//                String[] args2 = {String.valueOf(restaurantId)};
//                Cursor cursor2 = db.rawQuery(query2, args2);
//                String restaurantName = "";
//                if(cursor2.moveToFirst()) {
////                        int restId = cursor2.getInt(0);
//                    restaurantName = cursor2.getString(1);
////                        String restaurantLoc = cursor2.getString(2);
////                        RestaurantModel restaurantModel = new RestaurantModel(restId, restaurantName, restaurantLoc);
////                        restaurantList.add(restaurantModel);
//                }
                Log.e("OrderId",String.valueOf(id));
                String query3 = "SELECT * FROM ORDERITEM where OrderId = ?";
                String[] args3 = {String.valueOf(id)};
                Cursor cursor3 = db.rawQuery(query3, args3);

                String orderMenuString = "";
                if(cursor3.moveToFirst()) {
                    do {
                        int Id = cursor3.getInt(0);
                        int menuItemId = cursor3.getInt(1);
                        int orderId = cursor3.getInt(2);
                        int quantity = cursor3.getInt(3);

                        String query4 = "SELECT * FROM MENUITEM where Id = ?";
                        String[] args4= {String.valueOf(menuItemId)};
                        Cursor cursor4 = db.rawQuery(query4, args4);
                        String menuItemName = "";
                        if(cursor4.moveToFirst()) {
                            menuItemName = cursor4.getString(2);
                        }
                        orderMenuString = orderMenuString + quantity + "x " + menuItemName;
                        if(!cursor3.isLast()){
                            orderMenuString = orderMenuString +  ", ";
                        }
                    } while (cursor3.moveToNext());
                }

                jsonObject = new JSONObject();
                try {
                    jsonObject.put("orderId", id);
                    jsonObject.put("orderMenu", orderMenuString);
                    jsonObject.put("status", status);
                    jsonObject.put("estimatedTime", estimatedTime);
                    jsonObject.put("time", time);
                    Log.e("jsonObj",String.valueOf(jsonObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                retObj.add(jsonObject);

            }while(cursor.moveToNext());
        }



        return retObj;
    }

    public boolean changeOrderStatus(int id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Status", "Prepared");
        db2.update("Orders", cv, "Id = ?", new String[]{String.valueOf(id)});
        db2.close();
        return true;
    }


    public List allItems(){
        List<JSONObject> retObj = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        SQLiteDatabase db = this.getReadableDatabase();
        List<OrderModel> orderList = new ArrayList<>();
        List<RestaurantModel> restaurantList = new ArrayList<>();

        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);
//        Log.e("restId", String.valueOf(uM.getRestaurantId()));

        String query = "SELECT * FROM MENUITEM where RestaurantId = ?";
        String[] args = {String.valueOf(uM.getRestaurantId())};
        Cursor cursor = db.rawQuery(query, args);
        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int restId = cursor.getInt(1);
                String name = cursor.getString(2);

                jsonObject = new JSONObject();
                try {
                    jsonObject.put("Id", id);
                    jsonObject.put("RestaurantId", restId);
                    jsonObject.put("Name", name);
                    retObj.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }

        return retObj;
    }


    public boolean addItem(String name){
        SQLiteDatabase db = this.getWritableDatabase();

        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);

        String query = "INSERT INTO  MENUITEM (RestaurantId,Name) VALUES (?,?)";
        db.execSQL(query,new String[]{String.valueOf(uM.getRestaurantId()),name});

        db.close();

        return true;
    }

    public boolean deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();

//        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
//        UserModel uM = gson.fromJson(jsonString, UserModel.class);

        String query = "DELETE FROM MENUITEM WHERE Id = ?";
        db.execSQL(query,new String[]{String.valueOf(id)});

        db.close();

        return true;
    }


    public boolean editItem(int Id, String Name){
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Name",Name);
        db2.update("MENUITEM", cv, "Id = ?", new String[]{String.valueOf(Id)});
        db2.close();

        return true;
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

                roleList.add(roleRec);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return "";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
