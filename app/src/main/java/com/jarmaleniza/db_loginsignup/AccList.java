package com.jarmaleniza.db_loginsignup;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AccList extends ListActivity {
    SQLiteDB Conn;
    ArrayList<String> ItemList;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Conn = new SQLiteDB(this);
        ItemList = Conn.GetGuestList();
        if(ItemList.size() > 0){
            setListAdapter(new ArrayAdapter<String>(AccList.this, android.R.layout.simple_list_item_1, ItemList));
        }
        else{
            Toast.makeText(this, "No Record/s Found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String selectedItem = ItemList.get(position);
        String[] parts = selectedItem.split(" ");
        if (parts.length >= 1) {
            int recordId = Integer.parseInt(parts[0]);
            Intent intent = new Intent(AccList.this, EditRecord.class);
            intent.putExtra("selectedData", recordId);
            intent.putExtra("account", 1);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AccList.this, MainActivity.class);
        startActivity(intent);
    }
}
