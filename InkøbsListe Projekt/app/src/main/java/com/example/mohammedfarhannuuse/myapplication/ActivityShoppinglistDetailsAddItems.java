package com.example.mohammedfarhannuuse.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class ActivityShoppinglistDetailsAddItems extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist_details_additem);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Tilføj vare til kurv");

        final ListView lsvItems = findViewById(R.id.lsvItems);

        try {
            Cursor itemsCursor = Storage.getInstance().getItemsCursor();
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    R.layout.activity_shoppinglist_details_additem_template,
                    itemsCursor,
                    new String[]{"ITEMNAME", "AMOUNT", "AMOUNTTYPE", "NORMALPRICE"},
                    new int[]{R.id.txvItemName, R.id.txvAmount, R.id.txvAmountType, R.id.txvItemPrice},
                    0);
            lsvItems.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        lsvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                AlertDialog.Builder create = new AlertDialog.Builder(view.getContext());
                create.setTitle("Tilføj vare til kurv");
                create.setCancelable(true);

                final LayoutInflater inflater = ActivityShoppinglistDetailsAddItems.this.getLayoutInflater();
                final long itemId = id;
                View mView = inflater.inflate(R.layout.dialog_additem_to_shoppinglist, null);
                create.setView(mView);

                final EditText dialogEdtAmount = mView.findViewById(R.id.edtAmount);
                create.setPositiveButton("Tilføj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int amount = Integer.parseInt(dialogEdtAmount.getText().toString());
                        Storage.getInstance().createAddedItem(getIntent().getLongExtra("id", -1), itemId, amount);
                        Toast toast = Toast.makeText(ActivityShoppinglistDetailsAddItems.this, "Du tilføjede varen i kurven", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                create.setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                create.show();
            }
        });
        lsvItems.setEmptyView(findViewById(R.id.emptyView));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shoppinglist_details_additem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListView(Storage.getInstance().getItemsCursor());
    }

    public void updateListView(Cursor cursor) {
        ListView lsvItems = findViewById(R.id.lsvItems);
        CursorAdapter adapter = (CursorAdapter) lsvItems.getAdapter();
        adapter.changeCursor(cursor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast toast;
        switch (item.getItemId()) {
            case R.id.actionSortByName:
                updateListView(Storage.getInstance().getItemsSortedByNameCursor());
                toast = Toast.makeText(this, "Du sorterede efter navn på varen", Toast.LENGTH_LONG);
                toast.show();
                return true;

            case R.id.actionSortByPrice:
                updateListView(Storage.getInstance().getItemsSortedByNormalPriceCursor());
                toast = Toast.makeText(this, "Du sorterede efter pris på varen", Toast.LENGTH_LONG);
                toast.show();
                return true;
            case R.id.actionSortByAdded:
                updateListView( Storage.getInstance().getItemsCursor());
                toast = Toast.makeText(this, "Du sorterede efter tilføjet", Toast.LENGTH_LONG);
                toast.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
