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
import android.widget.Toast;

public class ActivityShops extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ListView lsvShopsList = findViewById(R.id.lsvShopsList);
        registerForContextMenu(lsvShopsList);

        try {
            Cursor shopsListCursor = Storage.getInstance().getShopsCursor();
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    R.layout.template_shop_listview,
                    shopsListCursor,
                    new String[]{"SHOPNAME", "ADDRESS", "WEBPAGE"},
                    new int[]{R.id.tvShopTemplate_Name, R.id.tvShopTemplate_Address, R.id.tvShopTemplate_Webpage},
                    0);
            lsvShopsList.setAdapter(listAdapter);
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
                Toast toast = Toast.makeText(this, "You're already viewing shops", Toast.LENGTH_LONG);
                toast.show();
                return true;

            case R.id.ItemsIcon:
                finish();
                Intent itemIntent = new Intent(this, ActivityItems.class);
                startActivity(itemIntent);
                return true;

            case R.id.ShoppingListIcon:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void btnAddNewShopAction(View view) {
        AlertDialog.Builder create = new AlertDialog.Builder(this);
        create.setTitle("Opret Butik");
        create.setCancelable(true);
        final LayoutInflater inflater = this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_create_shop_template, null);
        create.setView(mView);

        final EditText dialogEditTextShopName = mView.findViewById(R.id.dialogEditText_ShopName);
        final EditText dialogEditTextShopAddress = mView.findViewById(R.id.dialogEditText_ShopAddress);
        final EditText dialogEditTextShopWebpage = mView.findViewById(R.id.dialogEditText_ShopWebpage);
        create.setPositiveButton("Opret", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String shopName = dialogEditTextShopName.getText().toString();
                String shopAddress = dialogEditTextShopAddress.getText().toString();
                String shopWebpage = dialogEditTextShopWebpage.getText().toString();

                Storage.getInstance().createShop(shopName, shopAddress, shopWebpage);
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
        getMenuInflater().inflate(R.menu.activity_shoppinglist_overview, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Storage.getInstance().deleteShop(acmi.id);
        updateListView();
        Toast.makeText(this, "Butik slettet", Toast.LENGTH_SHORT).show();

        return super.onContextItemSelected(item);
    }

    public void updateListView() {
        Cursor cursor = Storage.getInstance().getShopsCursor();
        ListView lsvShops = findViewById(R.id.lsvShopsList);
        CursorAdapter adapter = (CursorAdapter) lsvShops.getAdapter();
        adapter.changeCursor(cursor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListView();
    }
}
