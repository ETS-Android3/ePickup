package com.example.epickup.data.model;

public class UserModel {

    private int Id;
    private int RoleId;
    private int RestaurantId;
    private String Name;
    private String Email;
    private String Password;
    private int CurrentLogin;

    public UserModel(int id, int roleId, int restaurantId, String name, String email, String password, int currentLogin) {
        Id = id;
        RoleId = roleId;
        RestaurantId = restaurantId;
        Name = name;
        Email = email;
        Password = password;
        CurrentLogin = currentLogin;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "Id=" + Id +
                ", RoleId=" + RoleId +
                ", RestaurantId=" + RestaurantId +
                ", Name='" + Name + '\'' +
                ", Email='" + Email + '\'' +
                ", Password='" + Password + '\'' +
                ", CurrentLogin=" + CurrentLogin +
                '}';
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getRoleId() {
        return RoleId;
    }

    public void setRoleId(int roleId) {
        RoleId = roleId;
    }

    public int getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        RestaurantId = restaurantId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getCurrentLogin() {
        return CurrentLogin;
    }

    public void setCurrentLogin(int currentLogin) {
        CurrentLogin = currentLogin;
    }
}
