package com.example.mohammedfarhannuuse.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Database";
    private static final int DB_VERSION = 1;

    private static DatabaseHelper dbHelper = null;
    private static Context applicationContext;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DatabaseHelper getInstance() {
        if (dbHelper == null) {
            if (applicationContext == null) {
                throw new NullPointerException("Person SQLite - Missing getApplicationContext() context!");
            } else {
                dbHelper = new DatabaseHelper(applicationContext);
            }
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    public void initContent() {


    }

    public static void setApplicationContext(Context context) {
        applicationContext = context;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE SHOPPINGLIST(_id INTEGER PRIMARY KEY AUTOINCREMENT, NAMESPECIFIEDBYUSER TEXT, TOTALPRICE REAL);");
            db.execSQL("CREATE TABLE ADDEDITEM(_id INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_ID INTEGER, SHOPPINGLIST_ID INTEGER, AMOUNT REAL, TOTALPRICE REAL, BOUGHT INTEGER);");
            db.execSQL("CREATE TABLE ITEM(_id INTEGER PRIMARY KEY AUTOINCREMENT, ITEMNAME TEXT, AMOUNT REAL, AMOUNTTYPE TEXT, NORMALPRICE REAL);");
            db.execSQL("CREATE TABLE SHOP(_id INTEGER PRIMARY KEY AUTOINCREMENT, SHOPNAME TEXT, ADDRESS TEXT, WEBPAGE TEXT);");
            db.execSQL("CREATE TABLE ITEMSALE(_id INTEGER PRIMARY KEY AUTOINCREMENT, SHOP_ID INTEGER, ITEM_ID INTEGER, SALEPRICE REAL);");
           // initContent();
        }
    }

    //GET METHODS
    public Cursor getShoppingListDetails(long id) {
        return dbHelper.getReadableDatabase().rawQuery("Select AI._id, I.ItemName, I.NormalPrice, ItmS.SalePrice, I.AMOUNT, AI.AMOUNT AS AMOUNT2, I.AMOUNTTYPE, AI.BOUGHT From ADDEDITEM AI" +
                " Inner join ITEM I on I._id=AI.ITEM_ID " +
                " Left join ITEMSALE ItmS on Itms.Item_Id=I._id" +
                " Left join SHOP on shop._id=ItmS.Shop_Id" +
                " Where AI.SHOPPINGLIST_ID=?", new String[]{id + ""}, null);
    }

    public Cursor getShoppingListDetailsOrderedByNameCursor(long id) {
        return dbHelper.getReadableDatabase().rawQuery("Select AI._id, I.ItemName, I.NormalPrice, ItmS.SalePrice, I.AMOUNT, AI.AMOUNT AS AMOUNT2, I.AMOUNTTYPE, AI.BOUGHT From ADDEDITEM AI" +
                " Inner join ITEM I on I._id=AI.ITEM_ID " +
                " Left join ITEMSALE ItmS on Itms.Item_Id=I._id" +
                " Left join SHOP on shop._id=ItmS.Shop_Id" +
                " Where AI.SHOPPINGLIST_ID=?"+
                " Order by I.ItemName", new String[]{id + ""}, null);
    }

    public Cursor getShoppingListDetailsOrderedByPriceCursor(long id) {
        return dbHelper.getReadableDatabase().rawQuery("Select AI._id, I.ItemName, I.NormalPrice, ItmS.SalePrice, I.AMOUNT, AI.AMOUNT AS AMOUNT2, I.AMOUNTTYPE, AI.BOUGHT From ADDEDITEM AI" +
                " Inner join ITEM I on I._id=AI.ITEM_ID " +
                " Left join ITEMSALE ItmS on Itms.Item_Id=I._id" +
                " Left join SHOP on shop._id=ItmS.Shop_Id" +
                " Where AI.SHOPPINGLIST_ID=?"+
                " Order by I.NormalPrice", new String[]{id + ""}, null);
    }


    //GET METHODS
    public Cursor getTotalPriceOfShoppingList(long id) {
        return dbHelper.getReadableDatabase().rawQuery("Select sum(I.NormalPrice*AI.AMOUNT) From ADDEDITEM AI" +
                " Inner join ITEM I on I._id=AI.ITEM_ID " +
                " Where AI.SHOPPINGLIST_ID=?", new String[]{id + ""}, null);
    }

    public Cursor getTotalPriceOfShoppingListWithItemSale(long id) {
        return dbHelper.getReadableDatabase().rawQuery("Select sum(ItmS.SalePrice*AI.AMOUNT) From ADDEDITEM AI" +
                " Inner join ITEM I on I._id=AI.ITEM_ID " +
                " Inner join ITEMSALE ItmS on Itms.Item_Id=I._id" +
                " Inner join SHOP on shop._id=ItmS.Shop_Id" +
                " Where AI.SHOPPINGLIST_ID=?", new String[]{id + ""}, null);
    }

    public Cursor getTotalPriceItemSaleSavings2(long id) {
        return dbHelper.getReadableDatabase().rawQuery("Select sum(ItmS.SalePrice*AI.AMOUNT) From ADDEDITEM AI" +
                " Inner join ITEM I on I._id=AI.ITEM_ID " +
                " Inner join ITEMSALE ItmS on Itms.Item_Id=I._id" +
                " Where AI.SHOPPINGLIST_ID=?" , new String[]{id + ""}, null);
    }

    public Cursor getTotalPriceItemSaleSavings(long id) {
        return dbHelper.getReadableDatabase().rawQuery("Select sum((I.NORMALPRICE-ItmS.SalePrice)*AI.AMOUNT) From ADDEDITEM AI" +
                " Inner join ITEM I on I._id=AI.ITEM_ID " +
                " Inner join ITEMSALE ItmS on Itms.Item_Id=I._id" +
                " Where AI.SHOPPINGLIST_ID=?" , new String[]{id + ""}, null);
    }


    public Cursor getShoppingListsCursor() {
        return dbHelper.getReadableDatabase().query("SHOPPINGLIST", new String[]{"_id", "NAMESPECIFIEDBYUSER", "TOTALPRICE"}, null, null, null, null, null);
    }

    public Cursor getItemsCursor() {
        return dbHelper.getReadableDatabase().query("ITEM", new String[]{"_id", "ITEMNAME", "AMOUNT", "AMOUNTTYPE", "NORMALPRICE"}, null, null, null, null, null);
    }

    public Cursor getItemsSortedByNameCursor() {
        return dbHelper.getReadableDatabase().query("ITEM", new String[]{"_id", "ITEMNAME", "AMOUNT", "AMOUNTTYPE", "NORMALPRICE"}, null, null, null, null, "ITEMNAME"+" ASC");
    }
    public Cursor getItemsSortedByNormalPriceCursor() {
        return dbHelper.getReadableDatabase().query("ITEM", new String[]{"_id", "ITEMNAME", "AMOUNT", "AMOUNTTYPE", "NORMALPRICE"}, null, null, null, null, "NORMALPRICE"+" ASC");
    }

    public Cursor getShopsCursor() {
        return dbHelper.getReadableDatabase().query("SHOP", new String[]{"_id", "SHOPNAME", "ADDRESS", "WEBPAGE"}, null, null, null, null, null);
    }


    //CREATE METHODS
    public void createShoppingList(String nameSpecifiedByUser) {
        ContentValues cvShoppingList = new ContentValues();
        cvShoppingList.put("NAMESPECIFIEDBYUSER", nameSpecifiedByUser);
        cvShoppingList.put("TOTALPRICE", 0);
        long insertedId = dbHelper.getWritableDatabase().insert("SHOPPINGLIST", null, cvShoppingList);
    }

    public void createItem(String itemName, double amount, String amountType, double normalPrice) {
        ContentValues cvItem = new ContentValues();
        cvItem.put("ITEMNAME", itemName);
        cvItem.put("AMOUNT", amount);
        cvItem.put("AMOUNTTYPE", amountType);
        cvItem.put("NORMALPRICE", normalPrice);
        long insertedId = dbHelper.getWritableDatabase().insert("ITEM", null, cvItem);
    }

    public void createShop(String shopName, String shopAddress, String webpage) {
        ContentValues cvShop = new ContentValues();
        cvShop.put("SHOPNAME", shopName);
        cvShop.put("ADDRESS", shopAddress);
        cvShop.put("WEBPAGE", webpage);
        long insertedId = dbHelper.getWritableDatabase().insert("SHOP", null, cvShop);
    }

    public void createAddedItem(long idShoppingList, long idItem, double amount) {
        ContentValues cvAddedItem = new ContentValues();
        cvAddedItem.put("SHOPPINGLIST_ID", idShoppingList);
        cvAddedItem.put("ITEM_ID", idItem);
        cvAddedItem.put("AMOUNT", amount);
        cvAddedItem.put("BOUGHT", 0);
        long insertedId = dbHelper.getWritableDatabase().insert("ADDEDITEM", null, cvAddedItem);
    }

    public void createItemSale(long idShop, long idItem, double salePrice) {
        ContentValues cvItemSale = new ContentValues();
        cvItemSale.put("SHOP_ID", idShop);
        cvItemSale.put("ITEM_ID", idItem);
        cvItemSale.put("SALEPRICE", salePrice);
        long insertedId = dbHelper.getWritableDatabase().insert("ITEMSALE", null, cvItemSale);
    }


    //PUT METHODS
    public void changeItemPrice(long id, double newPrice) {
        ContentValues priceUpdate = new ContentValues();
        priceUpdate.put("NORMALPRICE", newPrice);
        dbHelper.getWritableDatabase().update("ITEM", priceUpdate, "_id=" + id, null);
    }

    public void changeAmountOfItemOnShoppinglist(long id, double amount) {
        ContentValues amountUpdate = new ContentValues();
        amountUpdate.put("AMOUNT", amount);
        dbHelper.getWritableDatabase().update("ADDEDITEM", amountUpdate, "_id=" + id, null);
    }

    public void updateBought(long id, int bought) {
        ContentValues boughtUpdate = new ContentValues();
        boughtUpdate.put("BOUGHT", bought);
        dbHelper.getWritableDatabase().update("ADDEDITEM", boughtUpdate, "_id=" + id, null);
    }


    //DELETE METHODS
    public void deleteShoppingList(long id) {
        int shopcount = dbHelper.getWritableDatabase().delete("SHOPPINGLIST", "_id = " + id, null);
        int addeditemcount = dbHelper.getWritableDatabase().delete("ADDEDITEM", "SHOPPINGLIST_ID = " + id, null);
    }

    public void deleteShop(long id) {
        int shopcount = dbHelper.getWritableDatabase().delete("SHOP", "_id = " + id, null);
        int shopcount2 = dbHelper.getWritableDatabase().delete("ITEMSALE", "SHOP_ID = " + id, null);
    }

    public void deleteItem(long id) {
        int itemCount = dbHelper.getWritableDatabase().delete("ITEM", "_id = " + id, null);
        int itemCount2 = dbHelper.getWritableDatabase().delete("ADDEDITEM", "ITEM_ID = " + id, null);
        int shopcount3 = dbHelper.getWritableDatabase().delete("ITEMSALE", "ITEM_ID = " + id, null);
    }

    public void deleteItemFromShoppingList(long id) {
        int itemCount = dbHelper.getWritableDatabase().delete("ADDEDITEM", "_id = " + id, null);
    }


}
