package com.example.epickup.data.model;

public class RestaurantModel {
    private int Id;
    private String Name;
    private String Location;

    public RestaurantModel(int id, String name, String location) {
        Id = id;
        Name = name;
        Location = location;
    }

    @Override
    public String toString() {
        return "RestaurantModel{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Location='" + Location + '\'' +
                '}';
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
