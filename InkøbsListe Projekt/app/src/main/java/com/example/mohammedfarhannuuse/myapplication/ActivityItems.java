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
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityItems extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ListView lsvItemsList = findViewById(R.id.lsvItemsList);
        registerForContextMenu(lsvItemsList);

        try {
            Cursor itemListCursor = Storage.getInstance().getItemsCursor();
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    R.layout.template_item_listview,
                    itemListCursor,
                    new String[]{"ITEMNAME", "AMOUNT", "AMOUNTTYPE" ,"NORMALPRICE"},
                    new int[]{R.id.tvItemTemplate_ItemName, R.id.tvItemTemplate_ItemAmount, R.id.tvItemTemplate_ItemAmountType, R.id.tvItemTemplate_ItemPrice},
                    0);
            lsvItemsList.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ShopsIcon:
                finish();
                Intent shopIntent = new Intent(this, ActivityShops.class);
                startActivity(shopIntent);
                return true;

            case R.id.ItemsIcon:
                Toast toast = Toast.makeText(this, "You're already viewing items", Toast.LENGTH_LONG);
                toast.show();
                return true;

            case R.id.ShoppingListIcon:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void btnAddNewItemAction(View view) {

        AlertDialog.Builder create = new AlertDialog.Builder(this);
        create.setTitle("Opret Butik");
        create.setCancelable(true);
        final LayoutInflater inflater = this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_create_item_template, null);
        create.setView(mView);

        final EditText dialogEditTextItemName = mView.findViewById(R.id.dialogEditText_ItemName);
        final EditText dialogEditTextItemPrice = mView.findViewById(R.id.dialogEditText_Itemprice);
        final EditText dialogEditTextItemAmount = mView.findViewById(R.id.dialogEditText_ItemAmount);
        final Spinner dialogSpinnerItemAmountType = mView.findViewById(R.id.dialogSpinner_ItemAmountType);
        create.setPositiveButton("Opret", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String itemName = dialogEditTextItemName.getText().toString();
                String itemAmount = dialogEditTextItemAmount.getText().toString();
                String itemAmountType = dialogSpinnerItemAmountType.getSelectedItem().toString();
                String itemPrice = dialogEditTextItemPrice.getText().toString();

                Storage.getInstance().createItem(itemName, itemAmount, itemAmountType, itemPrice);
                updateListView();
            }
        });
        create.setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        create.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_item_contextmenu,menu);
    }

    public boolean onContextItemSelected(MenuItem item){
        final AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.item_changePrice:

                AlertDialog.Builder create = new AlertDialog.Builder(this);
                create.setTitle("Ændre pris");
                create.setCancelable(true);
                final LayoutInflater inflater = this.getLayoutInflater();
                View mView = inflater.inflate(R.layout.dialog_change_price_item_template, null);
                create.setView(mView);
                final EditText dialogEditText = mView.findViewById(R.id.dialogEditText_changeItemPrice);

                create.setPositiveButton("Ændre pris", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newPrice = dialogEditText.getText().toString();
                        Storage.getInstance().changeItemPrice(acmi.id, newPrice);

                        updateListView();
                    }
                });
                create.setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                create.show();

                return true;

            case R.id.item_del:
                Storage.getInstance().deleteItem(acmi.id);
                Toast.makeText(this, "Vare slettet", Toast.LENGTH_SHORT).show();
                updateListView();

                return true;
        }

        return super.onContextItemSelected(item);
    }

    public void updateListView(){
        Cursor cursor = Storage.getInstance().getItemsCursor();
        ListView lsvItems = findViewById(R.id.lsvItemsList);
        CursorAdapter adapter = (CursorAdapter) lsvItems.getAdapter();
        adapter.changeCursor(cursor);
    }

}
