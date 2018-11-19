package com.example.mohammedfarhannuuse.myapplication;

import android.database.Cursor;

public class Storage {
    private static Storage storage;

    private Storage() {
    }

    //Initialize testdata
    public void initContent(){

        if(!(getItemsCursor().moveToFirst()  && getShoppingListsCursor().moveToFirst() && getShopsCursor().moveToFirst())){
            DatabaseHelper.getInstance().createItem("Mikro ovn 700w", 1, "stk.", 1000);
            DatabaseHelper.getInstance().createItem("Hønsekødsuppe", 1, "stk.", 40);
            DatabaseHelper.getInstance().createItem("Banan", 1, "stk.", 2);
            DatabaseHelper.getInstance().createItem("Badehåndklæde", 1, "stk.", 249.95);
            DatabaseHelper.getInstance().createItem("Medisterpølse", 1, "stk.", 45);
            DatabaseHelper.getInstance().createItem("Havregryn", 500, "G.", 10);
            DatabaseHelper.getInstance().createItem("Arla Sødmælk", 1, "L.", 14.50);
            DatabaseHelper.getInstance().createItem("Hakket Oksekød 16-22%", 0.5, "Kg.", 55);
            DatabaseHelper.getInstance().createItem("Bananlikør", 0.70, "Cl.", 129.95);
            DatabaseHelper.getInstance().createItem("Heinz Syltede agurker", 1, "stk.", 17.50);
            DatabaseHelper.getInstance().createShop("Sinful", "Søren Nymarks Vej 1C, 8270 Højbjerg", "www.sinful.dk");
            DatabaseHelper.getInstance().createShop("Bilka", "Agerøvej 7, 8381 Tilst", "www.bilka.dk");

            DatabaseHelper.getInstance().createShoppingList("Legetøjs liste");
            DatabaseHelper.getInstance().createShoppingList("Frokost indkøb");
            DatabaseHelper.getInstance().createShoppingList("Indkøb til spa-ophold");

            DatabaseHelper.getInstance().createAddedItem(1,1,1);
            DatabaseHelper.getInstance().createAddedItem(1,4,1);
            DatabaseHelper.getInstance().createAddedItem(1,3,9);
            DatabaseHelper.getInstance().createAddedItem(1,5,2);
            DatabaseHelper.getInstance().createAddedItem(1,10,1);

            DatabaseHelper.getInstance().createItemSale(1,1,100);


            DatabaseHelper.getInstance().createAddedItem(2,6,1);
            DatabaseHelper.getInstance().createAddedItem(2,7,2);
            DatabaseHelper.getInstance().createAddedItem(2,8,1);
        }
    }

    public static Storage getInstance() {
        if (storage == null) {
            storage = new Storage();
            storage.initContent();
        }
        return storage;
    }

    //GETTER METHODS
    public Cursor getTotalPriceOfShoppingList(long id){
        return DatabaseHelper.getInstance().getTotalPriceOfShoppingList(id);
    }

    public Cursor getTotalPriceOfShoppingListWithItemSale(long id) {
        return DatabaseHelper.getInstance().getTotalPriceOfShoppingListWithItemSale(id);
    }

    public Cursor getTotalPriceSavings(long id){
        return DatabaseHelper.getInstance().getTotalPriceItemSaleSavings(id);
    }

    public Cursor getShoppingListsCursor() {
        return DatabaseHelper.getInstance().getShoppingListsCursor();
    }

    public Cursor getShopsCursor() {
        return DatabaseHelper.getInstance().getShopsCursor();
    }

    public Cursor getShoppingListDetailsCursor(long id) {
        return DatabaseHelper.getInstance().getShoppingListDetails(id);
    }

    public Cursor getShoppingListDetailsOrderedByNameCursor(long id) {
        return DatabaseHelper.getInstance().getShoppingListDetailsOrderedByNameCursor(id);
    }

    public Cursor getShoppingListDetailsOrderedByPriceCursor(long id) {
        return DatabaseHelper.getInstance().getShoppingListDetailsOrderedByPriceCursor(id);
    }

    public Cursor getItemsCursor(){
        return DatabaseHelper.getInstance().getItemsCursor();
    }

    public Cursor getItemsSortedByNameCursor(){
        return DatabaseHelper.getInstance().getItemsSortedByNameCursor();
    }

    public Cursor getItemsSortedByNormalPriceCursor() {
        return DatabaseHelper.getInstance().getItemsSortedByNormalPriceCursor();
    }

    //CREATE METHODS
    public void createShoppingList(String nameSpecifiedByUser) {
        DatabaseHelper.getInstance().createShoppingList(nameSpecifiedByUser);
    }

    public void createShop(String shopName, String shopAddress, String shopWebpage) {
        DatabaseHelper.getInstance().createShop(shopName, shopAddress, shopWebpage);
    }

    public void createItem(String itemName, String itemAmount, String itemAmountType, String itemPrice) {
        DatabaseHelper.getInstance().createItem(itemName, Double.parseDouble(itemAmount), itemAmountType, Double.parseDouble(itemPrice));
    }

    public void createAddedItem(long idShoppingList, long idItem,double amount) {
        DatabaseHelper.getInstance().createAddedItem(idShoppingList, idItem, amount);
    }

    public void createItemSale(long idShop, long idItem,double salePrice) {
        DatabaseHelper.getInstance().createItemSale(idShop, idItem, salePrice);
    }


    //PUT METHODS
    public void changeItemPrice(long id, String newPrice) {
        DatabaseHelper.getInstance().changeItemPrice(id, Double.parseDouble(newPrice));
    }

    public void updateBought(long id, boolean bought) {
            DatabaseHelper.getInstance().updateBought(id, bought?1:0);
    }

    //DELETE METHODS
    public void deleteShoppingList(long id) {
        DatabaseHelper.getInstance().deleteShoppingList(id);
    }

    public void deleteShop(long id) {
        DatabaseHelper.getInstance().deleteShop(id);
    }

    public void deleteItem(long id) {
        DatabaseHelper.getInstance().deleteItem(id);
    }

    public void deleteItemFromShoppingList(long addedItemId) {
        DatabaseHelper.getInstance().deleteItemFromShoppingList(addedItemId);
    }


    public void changeAmountOfItemOnShoppinglist(long id, double amount) {
        DatabaseHelper.getInstance().changeAmountOfItemOnShoppinglist(id, amount);
    }
}
