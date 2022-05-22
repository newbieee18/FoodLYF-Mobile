package com.example.projectmanagement;

public class CartList {

    private int cart_id;
    private int quantity;
    private String product_name;
    private double product_price;
    private double subtotal;
    private String product_image;

    public CartList(int cart_id, String product_name, double product_price, double subtotal, String product_image, int quantity) {
        this.cart_id = cart_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
        this.product_image = product_image;
        this.subtotal = subtotal;
    }

    public int getId() {
        return cart_id;
    }

    public String getProductName() {
        return product_name;
    }

    public double getProductPrice() {
        return product_price;
    }

    public String getProductImage() {
        return product_image;
    }

    public int getQuantity() { return quantity; }

    public double getProductSubtotal() { return subtotal; }

}
