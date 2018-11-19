package com.example.mohammedfarhannuuse.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityShoppingListDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("titel"));
        final long shoppinglistid = getIntent().getLongExtra("id",-1);
        final ListView lsvShoppingList = findViewById(R.id.lsvItemsOnShoppingList);
        registerForContextMenu(lsvShoppingList);

        try {
            Cursor shoppingListOverviewCursor = Storage.getInstance().getShoppingListDetailsCursor(getIntent().getLongExtra("id",-1));
            SimpleCursorAdapter listAdapter = new ShoppingListItemCursorAdapter(this,
                    R.layout.activity_shoppinglist_details_template,
                    shoppingListOverviewCursor,
                    new String[]{"ITEMNAME","NORMALPRICE","AMOUNT2"},
                    new int[]{R.id.txvItemName,R.id.txvItemPrice,R.id.txvAmount3},
                    0);
            lsvShoppingList.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        lsvShoppingList.setEmptyView(findViewById(R.id.emptyView));
        updatePrice();
    }

    public double updateTotalPrice(){
        Cursor c = Storage.getInstance().getTotalPriceOfShoppingList(getIntent().getLongExtra("id",-1));
        double amount;
        if(c.moveToFirst())
            amount = c.getInt(0);
        else
            amount = -1;
        c.close();
        TextView txvTotalPrice = (TextView)findViewById(R.id.tvTotalPriceShoppingListDetails);
        txvTotalPrice.setText(amount+"");
        return amount;
    }

    public double updateTotalPriceWithItemSale(){
        Cursor c = Storage.getInstance().getTotalPriceSavings(getIntent().getLongExtra("id",-1));
        double amount;
        if(c.moveToFirst())
            amount = c.getInt(0);
        else
            amount = -1;
        c.close();
        TextView txvTotalPrice = (TextView)findViewById(R.id.tvSaleSavings);
        txvTotalPrice.setText(amount+"");
        return amount;
    }

    public void updatePrice(){
        double amount = updateTotalPrice()-updateTotalPriceWithItemSale();

        TextView txvTotalPrice = (TextView)findViewById(R.id.tvSalePriceDetails);
        txvTotalPrice.setText(amount+"");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shoppinglist, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast toast;
        switch (item.getItemId()) {
            case R.id.ShopsIcon:
                Intent shopIntent = new Intent(this, ActivityShops.class);
                startActivity(shopIntent);
                return true;

            case R.id.ItemsIcon:
                Intent itemIntent = new Intent(this, ActivityItems.class);
                startActivity(itemIntent);
                return true;

            case R.id.ShoppingListIcon:
                toast = Toast.makeText(this, "You are already viewing shopping lists", Toast.LENGTH_LONG);
                toast.show();
                return true;
            case R.id.actionSortByName:
                updateListView(Storage.getInstance().getShoppingListDetailsOrderedByNameCursor(getIntent().getLongExtra("id",-1)));
                toast = Toast.makeText(this, "Du sorterede efter navn på varen", Toast.LENGTH_LONG);
                toast.show();
                return true;

            case R.id.actionSortByPrice:
                updateListView(Storage.getInstance().getShoppingListDetailsOrderedByPriceCursor(getIntent().getLongExtra("id",-1)));
                toast = Toast.makeText(this, "Du sorterede efter pris på varen", Toast.LENGTH_LONG);
                toast.show();
                return true;
            case R.id.actionSortByAdded:
                updateListView(Storage.getInstance().getShoppingListDetailsCursor(getIntent().getLongExtra("id",-1)));
                toast = Toast.makeText(this, "Du sorterede efter tilføjet", Toast.LENGTH_LONG);
                toast.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void btnAddNewItemToList(View view){
        Intent intent = new Intent(ActivityShoppingListDetails.this, ActivityShoppinglistDetailsAddItems.class);
        intent.putExtra("id", getIntent().getLongExtra("id",-1));
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateListView(Storage.getInstance().getShoppingListDetailsCursor(getIntent().getLongExtra("id",-1)));
    }

    public void updateListView(Cursor cursor) {
        ListView lsvItemsOnShoppingList = findViewById(R.id.lsvItemsOnShoppingList);
        CursorAdapter adapter = (CursorAdapter) lsvItemsOnShoppingList.getAdapter();
        adapter.changeCursor(cursor);
        updatePrice();
        updateTotalPriceWithItemSale();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_shoppinglist_details_contextmenu,menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.changeAmount:

                AlertDialog.Builder create = new AlertDialog.Builder(this);
                create.setTitle("Ændre antal");
                create.setCancelable(true);
                final LayoutInflater inflater = this.getLayoutInflater();
                View mView = inflater.inflate(R.layout.dialog_change_amount_of_item_on_shoppinglist, null);
                create.setView(mView);
                final EditText dialogEditText = mView.findViewById(R.id.dialogEditText_changeAmount);

                create.setPositiveButton("Bekræft antal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int newAmount = Integer.parseInt(dialogEditText.getText().toString());
                        Storage.getInstance().changeAmountOfItemOnShoppinglist(acmi.id, newAmount);
                        updateListView(Storage.getInstance().getShoppingListDetailsCursor(getIntent().getLongExtra("id",-1)));
                    }
                });
                create.setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                create.show();

                return true;

            case R.id.item_delete:
                Storage.getInstance().deleteItemFromShoppingList(acmi.id);
                Toast.makeText(this, "Vare slettet", Toast.LENGTH_SHORT).show();
                updateListView(Storage.getInstance().getShoppingListDetailsCursor(getIntent().getLongExtra("id",-1)));
                return true;
        }


        return super.onContextItemSelected(item);
    }
}

