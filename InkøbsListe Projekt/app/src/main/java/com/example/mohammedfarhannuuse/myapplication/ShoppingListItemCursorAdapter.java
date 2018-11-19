package com.example.mohammedfarhannuuse.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ShoppingListItemCursorAdapter extends SimpleCursorAdapter {


    ShoppingListItemCursorAdapter(Context context, int layout, Cursor c,
                                  String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);
        final CheckBox checkBox = view.findViewById(R.id.checkBox_bought);
        checkBox.setOnCheckedChangeListener(null);
        final int id = cursor.getInt(cursor.getColumnIndex("_id"));
        boolean checked =(cursor.getInt(cursor.getColumnIndex("BOUGHT")))==1;
        if(checked){
            checkBox.setChecked(true);
        }
        else{
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Storage.getInstance().updateBought(id,isChecked);

                if(isChecked) {
                    Toast.makeText(context, "Vare købt", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
