package com.example.epickup.data.model;

public class OrderModel {

    private int Id;
    private int RestaurantID;
    private int UserId;
    private int Payment;
    private String Status;
    private String EstimatedTime;
    private String Time;
    private String Feedback;

    public OrderModel(int id, int restaurantID, int userId, int payment, String status, String estimatedTime, String time, String feedback) {
        Id = id;
        RestaurantID = restaurantID;
        UserId = userId;
        Payment = payment;
        Status = status;
        EstimatedTime = estimatedTime;
        Time = time;
        Feedback = feedback;
    }

    @Override
    public String toString() {
        return "OrderModel{" +
                "Id=" + Id +
                ", RestaurantID=" + RestaurantID +
                ", UserId=" + UserId +
                ", Payment='" + Payment +
                ", Status='" + Status + '\'' +
                ", EstimatedTime='" + EstimatedTime + '\'' +
                ", Time='" + Time + '\'' +
                ", Feedback='" + Feedback + '\'' +
                '}';
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getRestaurantID() {
        return RestaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        RestaurantID = restaurantID;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getPayment() {
        return Payment;
    }

    public void setPayment(int payment) {
        Payment = payment;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEstimatedTime() {
        return EstimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        EstimatedTime = estimatedTime;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }
}
