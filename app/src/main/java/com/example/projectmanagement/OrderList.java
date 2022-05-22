package com.example.projectmanagement;

public class OrderList {

    private int order_id;
    private String customer_phone;
    private String customer_name;
    private String customer_address;
    private String product_name;
    private int quantity;
    private double subtotal;
    private String latitude;
    private String longitude;
    private String store_name;
    private String branch_name;
    private String branch_latitude;
    private String branch_longitude;
    private double distance;
    private double total;

    public OrderList(int order_id, String customer_phone, String customer_name, String customer_address, String product_name, int quantity, int subtotal, String latitude, String longitude, String store_name, String branch_name,
                     String branch_latitude, String branch_longitude, double total, double distance) {
        this.order_id = order_id;
        this.customer_phone = customer_phone;
        this.customer_name = customer_name;
        this.customer_address = customer_address;
        this.product_name = product_name;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.latitude = latitude;
        this.longitude = longitude;
        this.store_name = store_name;
        this.branch_name = branch_name;
        this.branch_latitude = branch_latitude;
        this.branch_longitude = branch_longitude;
        this.total = total;
        this.distance = distance;

    }

    public int getOrderID() { return order_id; }

    public String getCustomerPhone() {
        return customer_phone;
    }

    public String getCustomerName() { return customer_name; }

    public String getCustomerAddress() { return customer_address; }

    public String getProductName() { return product_name; }

    public int getProductQuantity() { return quantity; }

    public double getSubtotal() { return subtotal; }

    public String getLatitude() { return latitude; }

    public String getLongitude() { return longitude; }

    public String getStoreName() { return store_name; }

    public String getBranchName() { return branch_name; }

    public String getBranchLatitude() { return branch_latitude; }

    public String getBranchLongitude() { return branch_longitude; }

    public double getTotal() { return total; }

    public double getDistance() { return distance; }
}
