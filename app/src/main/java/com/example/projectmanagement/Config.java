package com.example.projectmanagement;

public class Config {

    //Customer Account
    public static final String DATA_URL = "http://192.168.254.109/fadSystem/customerAccount.php?phone=";
    public static final String FULLNAME = "fullname";
    public static final String EMAIL = "email";
    public static final String ADDRESS = "address";
    public static final String JSON_ARRAY = "result";
    public static final String ID = "id";
    public static final String IMG = "img";
    public static final String PHONE = "phone";

    //Rider Account
    public static final String DATA_URL1 = "http://192.168.254.109/fadSystem/riderAccount.php?phone=";
    public static final String FULLNAME1 = "fullname";
    public static final String EMAIL1 = "email";
    public static final String GENDER1 = "gender";
    public static final String BIRTHDATE = "birthdate";
    public static final String JSON_ARRAYY = "result";
    public static final String ID1 = "id";
    public static final String PHONE1 = "phone";


    //Store
    public static final String STORE_URL = "http://192.168.254.109/fadSystem/store.php";
    public static final String STORE_ID = "id";
    public static final String STORE_PHONE = "phone";
    public static final String CONTACT_PERSON= "contact_person";
    public static final String STORE_EMAIL = "email";
    public static final String STORE_NAME = "store_name";
    public static final String STORE_IMAGE= "store_image";
    public static final String JSON_ARRAY1 = "store";


    //Products
    public static final String PRODUCT_URL = "http://192.168.254.109/fadSystem/products.php?store_name=";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_DESC = "product_description";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_IMAGE = "product_image";
    public static final String RATINGS = "ratings";
    public static final String AVAILABLE = "available";
    public static final String JSON_ARRAY2 = "products";

    //Users
    public static final String USERS_URL = "http://192.168.254.109/fadSystem/users.php";
    public static final String USER_FULLNAME = "fullname";
    public static final String USER_POSITION = "position";
    public static final String USER_PASSWORD = "password";
    public static final String USER_CNUMBER = "contact";
    public static final String USER_GENDER = "gender";
    public static final String USER_AGE = "age";
    public static final String USER_EMAIL = "email";
    public static final String USER_ADDRESS = "address";
    public static final String JSON_ARRAY3 = "users";
    public static final String USER_ID = "id";
    public static final String USER_USERNAME = "username";

    //FoodProducts
    public static final String PRODUCT_URL1 = "http://192.168.254.109/fadSystem/foodProducts.php?phone=";
    public static final String PRODUCT_ID1 = "product_id";
    public static final String PRODUCT_NAME1 = "product_name";
    public static final String PRODUCT_DESC1 = "product_description";
    public static final String PRODUCT_PRICE1 = "product_price";
    public static final String PRODUCT_IMAGE1 = "product_image";
    public static final String RATINGS1 = "ratings";
    public static final String JSON_ARRAY4 = "foodProducts";

    //EditProduct
    public static final String EDIT_PRODUCT_URL = "http://192.168.254.109/fadSystem/editProduct.php?edit=";
    public static final String EDIT_PRODUCT_ID = "product_id";
    public static final String EDIT_PRODUCT_NAME = "product_name";
    public static final String EDIT_PRODUCT_DESC = "product_description";
    public static final String EDIT_PRODUCT_PRICE = "product_price";
    public static final String EDIT_PRODUCT_IMAGE = "product_image";
    public static final String EDIT_RATINGS = "ratings";
    public static final String EDIT_PRODUCT_CATEGORY = "category";
    public static final String JSON_ARRAY5 = "editFoodProducts";

    //Outlets
    public static final String OUTLETS_URL = "http://192.168.254.109/fadSystem/outlets.php?phone=";
    public static final String OUTLET_ID = "outlet_id";
    public static final String OUTLET_PHONE = "outlet_phone";
    public static final String OUTLET_SNAME = "outlet_sname";
    public static final String OUTLET_BNAME = "outlet_bname";
    public static final String OUTLET_LATITUDE = "outlet_latitude";
    public static final String OUTLET_LONGITUDE= "outlet_longitude";
    public static final String JSON_ARRAY6 = "outlets";

    //EditOutlet
    public static final String EDIT_OUTLET_URL = "http://192.168.254.109/fadSystem/editOutlet.php?outletID=";
    public static final String EDIT_OUTLET_PHONE = "outlet_phone";
    public static final String EDIT_OUTLET_BNAME = "outlet_bname";
    public static final String EDIT_OUTLET_LATITUDE = "outlet_latitude";
    public static final String EDIT_OUTLET_LONGITUDE= "outlet_longitude";
    public static final String JSON_ARRAY7 = "editOutlet";

    //Cart
    public static final String CART_URL = "http://192.168.254.109/fadSystem/cart.php?phone=";
    public static final String CART_PRODUCT_NAME = "product_name";
    public static final String CART_PRODUCT_ID = "cart_id";
    public static final String CART_PRODUCT_QUANTITY = "quantity";
    public static final String CART_PRODUCT_PRICE = "price";
    public static final String CART_PRODUCT_IMAGE = "product_image";
    public static final String CART_PRODUCT_SUBTOTAL = "subtotal";
    public static final String TOTAL = "total";
    public static final String JSON_ARRAY8 = "cart";

    //AvailableOrders
    public static final String ORDER_URL = "http://192.168.254.109/fadSystem/orders.php?phone=";
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_CUSTOMER_PHONE = "customer_phone";
    public static final String ORDER_CUSTOMER_NAME = "customer_name";
    public static final String ORDER_CUSTOMER_ADDRESS = "customer_address";
    public static final String ORDER_PRODUCT_NAME = "product_name";
    public static final String ORDER_QUANTITY = "quantity";
    public static final String ORDER_SUBTOTAL = "subtotal";
    public static final String ORDER_CUSTOMER_LATITUDE = "latitude";
    public static final String ORDER_CUSTOMER_LONGITUDE = "longitude";
    public static final String ORDER_STORE_NAME = "store_name";
    public static final String ORDER_TOTAL = "total";
    public static final String ORDER_DISTANCE = "distance";
    public static final String JSON_ARRAY9 = "orders";

    //OrderList
    public static final String ORDER_URL1 = "http://192.168.254.109/fadSystem/orderList.php?phone=";
    public static final String ORDER_ID1 = "order_id";
    public static final String ORDER_CUSTOMER_PHONE1 = "customer_phone";
    public static final String ORDER_CUSTOMER_NAME1 = "customer_name";
    public static final String ORDER_CUSTOMER_ADDRESS1 = "customer_address";
    public static final String ORDER_PRODUCT_NAME1 = "product_name";
    public static final String ORDER_QUANTITY1 = "quantity";
    public static final String ORDER_SUBTOTAL1 = "subtotal";
    public static final String ORDER_TOTAL1 = "total";
    public static final String ORDER_CUSTOMER_LATITUDE1 = "latitude";
    public static final String ORDER_CUSTOMER_LONGITUDE1 = "longitude";
    public static final String ORDER_STORE_NAME1 = "store_name";
    public static final String ORDER_SUGGESTION = "suggestion";
    public static final String ORDER_STATUS = "status";
    public static final String ORDER_RIDER_PHONE = "rider_phone";
    public static final String ORDER_RIDER_LATITUDE = "rider_latitude";
    public static final String ORDER_RIDER_LONGITUDE = "rider_longitude";
    public static final String JSON_ARRAY91 = "orderList";

    //JobListAdapter
    public static final String ORDER_URL2 = "http://192.168.254.109/fadSystem/jobList.php?phone=";
    public static final String ORDER_ID2 = "order_id";
    public static final String ORDER_CUSTOMER_PHONE2 = "customer_phone";
    public static final String ORDER_CUSTOMER_NAME2 = "customer_name";
    public static final String ORDER_PRODUCT_NAME2 = "product_name";
    public static final String ORDER_QUANTITY2 = "quantity";
    public static final String ORDER_SUBTOTAL2 = "subtotal";
    public static final String ORDER_CUSTOMER_LATITUDE2 = "latitude";
    public static final String ORDER_CUSTOMER_LONGITUDE2 = "longitude";
    public static final String ORDER_STORE_NAME2 = "store_name";
    public static final String ORDER_BRANCH_NAME2 = "branch_name";
    public static final String ORDER_BRANCH_LATITUDE2 = "branch_latitude";
    public static final String ORDER_BRANCH_LONGITUDE2 = "branch_longitude";
    public static final String ORDER_SUGGESTION2 = "suggestion";
    public static final String ORDER_TOTAL2 = "total";
    public static final String ORDER_DISTANCE2 = "distance";
    public static final String JOB_LIST = "jobs";

    //OutletAccount
    public static final String OUTLETS_URL1 = "http://192.168.254.109/fadSystem/outletAccount.php?phone=";
    public static final String OUTLET_ID1 = "outlet_id";
    public static final String OUTLET_PHONE1 = "outlet_phone";
    public static final String OUTLET_SNAME1 = "outlet_sname";
    public static final String OUTLET_BNAME1 = "outlet_bname";
    public static final String OUTLET_LATITUDE1 = "outlet_latitude";
    public static final String OUTLET_LONGITUDE1= "outlet_longitude";
    public static final String JSON_ARRAY10 = "outletAccount";

}
