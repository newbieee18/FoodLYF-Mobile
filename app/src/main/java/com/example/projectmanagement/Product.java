package com.example.projectmanagement;

public class Product {

    private int product_id;
    private String product_name;
    private String product_desc;
    private double rating;
    private double product_price;
    private String product_image;

    public Product(int product_id, String product_name, String product_desc, double rating, double product_price, String product_image) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_desc = product_desc;
        this.rating = rating;
        this.product_price = product_price;
        this.product_image = product_image;
    }

    public int getId() {
        return product_id;
    }

    public String getProductName() {
        return product_name;
    }

    public String getProductDesc() {
        return product_desc;
    }

    public double getRating() {
        return rating;
    }

    public double getProductPrice() {
        return product_price;
    }

    public String getProductImage() {
        return product_image;
    }

}
