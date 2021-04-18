package com.example.epickup.data.model;

public class MenuItemModel {
    private int Id;
    private int RestuarantId;
    private String Name;

    public MenuItemModel(int id, int restuarantId, String name) {
        Id = id;
        RestuarantId = restuarantId;
        Name = name;
    }

    @Override
    public String toString() {
        return "MenuItemModel{" +
                "Id=" + Id +
                ", RestuarantId=" + RestuarantId +
                ", Name='" + Name + '\'' +
                '}';
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getRestuarantId() {
        return RestuarantId;
    }

    public void setRestuarantId(int restuarantId) {
        RestuarantId = restuarantId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
