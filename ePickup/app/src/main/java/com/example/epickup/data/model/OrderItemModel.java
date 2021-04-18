package com.example.epickup.data.model;

public class OrderItemModel {
    private int Id;
    private int MenuItemId;
    private int OrderId;
    private int Quantity;

    public OrderItemModel(int id, int menuItemId, int orderId, int quantity) {
        Id = id;
        MenuItemId = menuItemId;
        OrderId = orderId;
        Quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItemModel{" +
                "Id=" + Id +
                ", MenuItemId=" + MenuItemId +
                ", OrderId=" + OrderId +
                ", Quantity=" + Quantity +
                '}';
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getMenuItemId() {
        return MenuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        MenuItemId = menuItemId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
