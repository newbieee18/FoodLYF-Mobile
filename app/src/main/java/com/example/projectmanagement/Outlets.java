package com.example.projectmanagement;

public class Outlets {

    private int id;
    private String phone;
    private String store_name;
    private String branch_name;
    private String latitude;
    private String longitude;

    public Outlets (int id, String phone, String store_name, String branch_name, String latitude, String longitude){
        this.id = id;
        this.phone = phone;
        this.store_name = store_name;
        this.branch_name = branch_name;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public Integer getID() { return id; }
    public String getPhone() { return phone; }
    public String getStoreName() { return store_name; }
    public String getBranchName() { return branch_name; }
    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }


}
