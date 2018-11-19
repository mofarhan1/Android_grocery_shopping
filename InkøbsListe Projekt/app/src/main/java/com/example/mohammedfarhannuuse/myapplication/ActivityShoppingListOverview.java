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

public class ActivityShoppingListOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist_overview);
        DatabaseHelper.setApplicationContext(this.getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Indkøbslister");
        final ListView lsvShoppingLists = findViewById(R.id.lsvShoppingLists);
       registerForContextMenu(lsvShoppingLists);
        try {
            Cursor shoppingListOverviewCursor = Storage.getInstance().getShoppingListsCursor();
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    shoppingListOverviewCursor,
                    new String[]{"NAMESPECIFIEDBYUSER"},
                    new int[]{android.R.id.text1},
                    0);
            lsvShoppingLists.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        lsvShoppingLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                Intent intent = new Intent(ActivityShoppingListOverview.this, ActivityShoppingListDetails.class);
                intent.putExtra("id", id);
                String title =((TextView)view.findViewById(android.R.id.text1)).getText().toString();
                intent.putExtra("titel", title);
                startActivity(intent);
            }
        });

        lsvShoppingLists.setEmptyView(findViewById(R.id.emptyView));

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_shoppinglist_overview,menu);
    }

    public boolean onContextItemSelected(MenuItem item){
         AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
         Storage.getInstance().deleteShoppingList(acmi.id);
         updateListView();
         Toast.makeText(this, "You deleted this item", Toast.LENGTH_SHORT).show();

         return super.onContextItemSelected(item);
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
                Intent shopIntent = new Intent(this, ActivityShops.class);
                startActivity(shopIntent);
                return true;

            case R.id.ItemsIcon:
                Intent itemIntent = new Intent(this, ActivityItems.class);
                startActivity(itemIntent);
                return true;

            case R.id.ShoppingListIcon:
                Toast toast = Toast.makeText(this, "You are already viewing shopping lists", Toast.LENGTH_LONG);
                toast.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void btnAddNewShoppingListAction(View view) {
        AlertDialog.Builder create = new AlertDialog.Builder(this);
        create.setTitle("Opret indkøbsliste");
        create.setCancelable(true);
        final LayoutInflater inflater = this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_create_shoppinglist_template, null);
        create.setView(mView);

        final EditText dialogEditText = mView.findViewById(R.id.dialogEditText_ShoppingListName);
        create.setPositiveButton("Opret", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String nameOfShoppingList = dialogEditText.getText().toString();
                Storage.getInstance().createShoppingList(nameOfShoppingList);
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
    protected void onResume() {
        super.onResume();
        updateListView();
    }

    public void updateListView(){
        Cursor cursor = Storage.getInstance().getShoppingListsCursor();
        ListView lsvShoppingLists = findViewById(R.id.lsvShoppingLists);
        CursorAdapter adapter = (CursorAdapter) lsvShoppingLists.getAdapter();
        adapter.changeCursor(cursor);
    }
}

