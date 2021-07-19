package com.example.epickup;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.Nullable;

import com.example.epickup.data.model.OrderItemModel;
import com.example.epickup.data.model.OrderModel;
import com.example.epickup.data.model.RestaurantModel;
import com.example.epickup.data.model.UserModel;

import org.json.JSONArray;
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
        String createOrderTable = "CREATE TABLE ORDERS (Id INTEGER PRIMARY KEY AUTOINCREMENT, RestaurantId INTEGER, UserId INTEGER, Payment TEXT, Status TEXT, EstimatedTime TEXT, Time TEXT, FeedbackRating INTEGER, FeedbackMessage TEXT)";
        String createOrderItemTable = "CREATE TABLE ORDERITEM (Id INTEGER PRIMARY KEY AUTOINCREMENT, MenuItemId INTEGER, OrderId INTEGER, Quantity INTEGER)";
        String MenuItemTable = "CREATE TABLE MENUITEM (Id INTEGER PRIMARY KEY AUTOINCREMENT, RestaurantId INTEGER, Name TEXT, Cost TEXT)";

        String[] createTables = new String[]{createUserTable, createRoleTable, createRestaurantTable, createOrderTable, createOrderItemTable, MenuItemTable};

        for (String sql : createTables) {
            db.execSQL(sql);
        }

        String insertRole = "INSERT INTO  ROLE (Name) VALUES (?)";
        db.execSQL(insertRole, new String[]{"User"});
        db.execSQL(insertRole, new String[]{"Manager"});


        // Dummy Data
        /*String insertMenu = "INSERT INTO  MENUITEM (Id, RestaurantId, Name, Cost) VALUES (?,?,?,?)";
        db.execSQL(insertMenu, new Object[]{1, 1, "item1", "5"});
        db.execSQL(insertMenu, new Object[]{2, 1, "item2", "4"});

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateTime = format.format(new Date());
        String insertOrder = "INSERT INTO  Orders (Id, RestaurantId, UserId, Payment, Status, EstimatedTime, Time, FeedbackRating, FeedbackMessage) VALUES (?,?,?,?,?,?,?,?,?)";
        db.execSQL(insertOrder, new Object[]{1, 1, 2, "50", "accepted", currentDateTime, "", -1, ""});

        String insertOrderItem = "INSERT INTO  ORDERITEM (Id, MenuItemId, OrderId, Quantity) VALUES (?,?,?,?)";
        db.execSQL(insertOrderItem, new Object[]{1, 1, 1, 2});
        db.execSQL(insertOrderItem, new Object[]{2, 2, 1, 1});*/

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

    public static boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.length() > 0 && email.matches(emailPattern)){
            return true;
        } else {
            return false;
        }
    }

    public boolean register(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("RoleId", userModel.getRoleId());
        cv.put("RestaurantID", userModel.getRestaurantId());
        cv.put("Name", userModel.getName());
        cv.put("Email", userModel.getEmail());
        cv.put("Password", userModel.getPassword());
        cv.put("CurrentLogin", userModel.getCurrentLogin());

        long insert = db.insert("User", null, cv);
        db.close();
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int registerRestaurant(String[] list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Name", list[0]);
        cv.put("Location", list[1]);

        int insert = (int) db.insert("Restaurant", null, cv);
        db.close();
        return insert;
    }


    public boolean login(String[] list) {
        SQLiteDatabase db = this.getReadableDatabase();

        String findQuery = "SELECT * FROM User WHERE Email = ? AND Password = ?";
        Cursor cursor = db.rawQuery(findQuery, list);
        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            int roleId = cursor.getInt(1);
            int restaurantId = cursor.getInt(2);
            String name = cursor.getString(3);
            String email = cursor.getString(4);
            String password = cursor.getString(5);
            int currLogin = cursor.getInt(6);

            UserModel userModel = new UserModel(id, roleId, restaurantId, name, email, password, currLogin);
            String jsonString = gson.toJson(userModel);
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
            cv.put("CurrentLogin", 1);
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
        } catch (Exception e) {
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

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int restaurantId = cursor.getInt(1);
//                int UserId = cursor.getInt(2);
                String payment = cursor.getString(3);
                String status = cursor.getString(4);
                String estimatedTime = cursor.getString(5);
                String time = cursor.getString(6);
                int feedbackRating = cursor.getInt(7);


                String query2 = "SELECT * FROM RESTAURANT where Id = ?";
                String[] args2 = {String.valueOf(restaurantId)};
                Cursor cursor2 = db.rawQuery(query2, args2);
                String restaurantName = "";
                if (cursor2.moveToFirst()) {
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
                if (cursor3.moveToFirst()) {
                    do {
                        int Id = cursor3.getInt(0);
                        int menuItemId = cursor3.getInt(1);
                        int orderId = cursor3.getInt(2);
                        int quantity = cursor3.getInt(3);

                        String query4 = "SELECT * FROM MENUITEM where Id = ?";
                        String[] args4 = {String.valueOf(menuItemId)};
                        Cursor cursor4 = db.rawQuery(query4, args4);
                        String menuItemName = "";
                        if (cursor4.moveToFirst()) {
                            menuItemName = cursor4.getString(2);
                        }
                        orderMenuString = orderMenuString + quantity + "x " + menuItemName;
                        if (!cursor3.isLast()) {
                            orderMenuString = orderMenuString + ", ";
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
                    jsonObject.put("feedbackRating", feedbackRating);
                    jsonObject.put("payment", payment);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                retObj.add(jsonObject);

            } while (cursor.moveToNext());
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

        String query = "SELECT * FROM Orders where RestaurantId = ?";
        String[] args = {String.valueOf(uM.getRestaurantId())};
        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int restaurantId = cursor.getInt(1);
//                int UserId = cursor.getInt(2);
//                int payment = cursor.getInt(3);
                String payment = cursor.getString(3);
                String status = cursor.getString(4);
                String estimatedTime = cursor.getString(5);
                String time = cursor.getString(6);
                String feedbackRating = cursor.getString(7);
                String feedbackMessage = cursor.getString(8);


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
                String query3 = "SELECT * FROM ORDERITEM where OrderId = ?";
                String[] args3 = {String.valueOf(id)};
                Cursor cursor3 = db.rawQuery(query3, args3);

                String orderMenuString = "";
                if (cursor3.moveToFirst()) {
                    do {
                        int Id = cursor3.getInt(0);
                        int menuItemId = cursor3.getInt(1);
                        int orderId = cursor3.getInt(2);
                        int quantity = cursor3.getInt(3);

                        String query4 = "SELECT * FROM MENUITEM where Id = ?";
                        String[] args4 = {String.valueOf(menuItemId)};
                        Cursor cursor4 = db.rawQuery(query4, args4);
                        String menuItemName = "";
                        if (cursor4.moveToFirst()) {
                            menuItemName = cursor4.getString(2);
                        }
                        orderMenuString = orderMenuString + quantity + "x " + menuItemName;
                        if (!cursor3.isLast()) {
                            orderMenuString = orderMenuString + ", ";
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
                    jsonObject.put("feedbackRating", feedbackRating);
                    jsonObject.put("feedbackMessage", feedbackMessage);
                    jsonObject.put("payment", payment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                retObj.add(jsonObject);

            } while (cursor.moveToNext());
        }


        return retObj;
    }

    public boolean changeOrderStatus(int id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            final long millisToAdd = 3_600_000; //one hour
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = format.format(new Date());
            Date d = format.parse(currentDateTime);
            d.setTime(d.getTime() + millisToAdd);
            String oneHrAddedString = format.format(d);

            cv.put("Status", "Prepared");
            cv.put("Time", oneHrAddedString);
            db2.update("Orders", cv, "Id = ?", new String[]{String.valueOf(id)});
            db2.close();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List viewAllItems(int Id) {
        List<JSONObject> retObj = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        SQLiteDatabase db = this.getReadableDatabase();
        List<OrderModel> orderList = new ArrayList<>();
        List<RestaurantModel> restaurantList = new ArrayList<>();

        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);

        String query = "SELECT * FROM MENUITEM where RestaurantId = ?";
        String[] args = {String.valueOf(Id)};
        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int restId = cursor.getInt(1);
                String name = cursor.getString(2);
                String cost = cursor.getString(3);

                jsonObject = new JSONObject();
                try {
                    jsonObject.put("Id", id);
                    jsonObject.put("RestaurantId", restId);
                    jsonObject.put("Name", name);
                    jsonObject.put("Cost", cost);
                    jsonObject.put("Quantity", 0);
                    retObj.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }

        return retObj;
    }


    public List allItems() {
        List<JSONObject> retObj = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        SQLiteDatabase db = this.getReadableDatabase();
        List<OrderModel> orderList = new ArrayList<>();
        List<RestaurantModel> restaurantList = new ArrayList<>();

        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);

        String query = "SELECT * FROM MENUITEM where RestaurantId = ?";
        String[] args = {String.valueOf(uM.getRestaurantId())};
        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int restId = cursor.getInt(1);
                String name = cursor.getString(2);
                String cost = cursor.getString(3);

                jsonObject = new JSONObject();
                try {
                    jsonObject.put("Id", id);
                    jsonObject.put("RestaurantId", restId);
                    jsonObject.put("Name", name);
                    jsonObject.put("Cost", cost);
                    retObj.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }

        return retObj;
    }


    public boolean addItem(String name, String cost) {
        SQLiteDatabase db = this.getWritableDatabase();

        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);

        String query = "INSERT INTO  MENUITEM (RestaurantId,Name,Cost) VALUES (?,?,?)";
        db.execSQL(query, new String[]{String.valueOf(uM.getRestaurantId()), name, cost});

        db.close();

        return true;
    }

    public boolean deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

//        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
//        UserModel uM = gson.fromJson(jsonString, UserModel.class);

        String query = "DELETE FROM MENUITEM WHERE Id = ?";
        db.execSQL(query, new String[]{String.valueOf(id)});

        db.close();

        return true;
    }


    public boolean editItem(int Id, String Name, String Cost) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Name", Name);
        cv.put("Cost", Cost);
        db2.update("MENUITEM", cv, "Id = ?", new String[]{String.valueOf(Id)});
        db2.close();

        return true;
    }


    public JSONObject getProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);

        JSONObject retObj = new JSONObject();
        String Name = "";
        String Email = "";
        String Password = "";
        String RestaurantName = "";
        String RestaurantLocation = "";


        String query = "SELECT * FROM USER WHERE Id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(uM.getId())});
        if (cursor.moveToFirst()) {
            Name = cursor.getString(3);
            Email = cursor.getString(4);
            Password = cursor.getString(5);
        }

        String query2 = "SELECT * FROM RESTAURANT WHERE Id = ?";
        Cursor cursor2 = db.rawQuery(query2, new String[]{String.valueOf(uM.getRestaurantId())});
        if (cursor2.moveToFirst()) {
            RestaurantName = cursor2.getString(1);
            RestaurantLocation = cursor2.getString(2);
        }

        try {
            retObj.put("Name", Name);
            retObj.put("Email", Email);
            retObj.put("Password", Password);
            retObj.put("RestaurantName", RestaurantName);
            retObj.put("RestaurantLocation", RestaurantLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retObj;

    }

    public Boolean updateuserdata(String profileName, String profilePassword, String profileRestaurantName, String profileRestaurantLocation) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
            UserModel uM = gson.fromJson(jsonString, UserModel.class);

//            String query = "UPDATE USER SET Name = ? AND Password = ? WHERE Id = ?";
//            db.execSQL(query, new String[]{profileName, profilePassword, String.valueOf(uM.getId())});
//
//            String query2 = "UPDATE Restaurant SET Name = ? AND Location = ? WHERE Id = ?";
//            db.execSQL(query2, new String[]{profileRestaurantName, profileRestaurantLocation, String.valueOf(uM.getRestaurantId())});

            ContentValues contentValues = new ContentValues();
            contentValues.put("Name", profileName);
            contentValues.put("Password", profilePassword);
            long result = db.update("USER", contentValues, "Id = ?", new String[]{String.valueOf(uM.getId())});

            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("Name", profileRestaurantName);
            contentValues2.put("Location", profileRestaurantLocation);
            long result2 = db.update("RESTAURANT", contentValues2, "Id = ?", new String[]{String.valueOf(uM.getRestaurantId())});

            db.close();

            uM.setName(profileName);
            uM.setPassword(profilePassword);
            String jsonString2 = gson.toJson(uM);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("userObject", jsonString2);
            editor.commit();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //order id i have added .pass it to activity
    public Boolean feedbackData(int feedbackRating, String feedbackMessage, String orderID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FeedbackRating", feedbackRating);
        contentValues.put("FeedbackMessage", feedbackMessage);

        long result = DB.update("ORDERS", contentValues, "Id = ?", new String[]{orderID});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public List searchRestaurantList(String searchString, String searchParam) {
    List<JSONObject> retObj = new ArrayList<>();
    JSONObject jsonObject = new JSONObject();

    SQLiteDatabase db = this.getReadableDatabase();

    String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
    UserModel uM = gson.fromJson(jsonString, UserModel.class);

    String query = "SELECT * FROM RESTAURANT WHERE " + searchParam + " like ?";
    String[] args = {"%" + searchString + "%"};
    Cursor cursor = db.rawQuery(query, args);
    if(cursor.moveToFirst())
    {
        do {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String location = cursor.getString(2);

            jsonObject = new JSONObject();
            try {
                jsonObject.put("Id", id);
                jsonObject.put("Name", name);
                jsonObject.put("Location", location);
                retObj.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } while (cursor.moveToNext());
    }
        return retObj;
}



    public boolean placeOrder(JSONObject orderObj){
        SQLiteDatabase db = this.getWritableDatabase();

        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);



        ContentValues cv = new ContentValues();
        try {

//                final long millisToAdd = 3_600_000; //one hour
            final long millisToAdd = Long.parseLong(String.valueOf(orderObj.get("EstimatedTime"))) * 60000;
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = format.format(new Date());
            Date d = format.parse(currentDateTime);
            d.setTime(d.getTime() + millisToAdd);
            String estimatedTimeString = format.format(d);


            cv.put("RestaurantId", String.valueOf(orderObj.get("RestaurantId")));
            cv.put("UserId", String.valueOf(orderObj.get("UserId")));
            cv.put("Payment", String.valueOf(orderObj.get("Payment")));
            cv.put("Status", "Accepted");
            cv.put("EstimatedTime", estimatedTimeString);
            cv.put("Time", "");
            cv.put("FeedbackRating", "-1");
            cv.put("FeedbackMessage", "");


        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        long insert = db.insert("ORDERS", null, cv);
        db.close();

        if(insert!=-1){
            insertOrderItems(insert,orderObj);
            return true;
        } else {
            return false;
        }
    }

    public void insertOrderItems(long orderId, JSONObject orderObj){
        SQLiteDatabase db = this.getWritableDatabase();

        String jsonString = sp.getString("userObject", String.valueOf(MODE_PRIVATE));
        UserModel uM = gson.fromJson(jsonString, UserModel.class);
        try {
            JSONArray orderItemList = new JSONArray (String.valueOf(orderObj.get("orderItem")));

            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                for (int i=0;i<orderItemList.length();i++) {
                    JSONObject tempObj = new JSONObject(String.valueOf(orderItemList.get(i)));
                    values.put("MenuItemId", String.valueOf(tempObj.get("Id")));
                    values.put("OrderId", String.valueOf(orderId));
                    values.put("Quantity", String.valueOf(tempObj.get("Quantity")));
                    db.insert("ORDERITEM", null, values);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
