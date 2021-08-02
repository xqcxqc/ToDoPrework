package com.example.todoprework;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText edItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd); // Add button
        edItem = findViewById(R.id.edItem); //the plain text bar
        rvItems = findViewById(R.id.rvItems); // recycler view

        loadItems(); // load local file if any

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item in the selected position from the model
                items.remove(position);
                // Notify the adapter that an item is remove
                itemsAdapter.notifyItemRemoved(position);
                //show the text to notify user the remove
                Toast.makeText(getApplicationContext(),"Item was removed", Toast.LENGTH_SHORT).show();
                //save the change into a file
                saveItem();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //place to open up and edit an activity

                //one method to check we are able to get inside this method
                Log.d("MainActivity", "Single click at position"+position);

                //create the new activity
                //do this by using intent which is like a request to the Android system.
                //in our case, it's a request simply to open up another activity defined in our application
                Intent i = new Intent(MainActivity.this,EditActivity.class);

                //pass the activity
                //pass the data along with the intent. we can access this data in the EditActivity
                //so we need to pass in the actual contents of the todo item and the postion
                i.putExtra(KEY_ITEM_TEXT,items.get(position)); //this wil look into our model list, then getting the relevant position
                i.putExtra(KEY_ITEM_POSITION,position);

                //display the activity
                startActivityForResult(i,EDIT_TEXT_CODE);


            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener,onClickListener);

        //build recycler view based on the itemsAdapter
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        //add the clickListener on the add button. This is done by
        //notifying the adapter every time the user click the button
        //and take the corresponding action
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //grab the inputed text into string
                String todoItem = edItem.getText().toString();
                //Add item to the model
                items.add(todoItem);
                //Notify adapter that an item is inserted at the end
                itemsAdapter.notifyItemInserted(items.size()-1);
                //reset the plain text bar to be empty
                edItem.setText("");
                //show the text to notify user the remove
                Toast.makeText(getApplicationContext(),"Item was added", Toast.LENGTH_SHORT).show();
                //save the change into a file
                saveItem();
            }
        });

    }

    //handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            //retreive the updae text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //Update the model at the right position with new item text
            items.set(position, itemText);
            //notify the adapter
            itemsAdapter.notifyItemChanged(position);
            //persist the changes
            saveItem();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT);
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }


    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }

    //This function will load items by reading every line of the data file
    private  void loadItems(){
        try {
            //load the file into a list
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e){
            Log.e("MainActivity","Error reading items",e);
            items = new ArrayList<>(); //if error happen, give a enmpty list
        }
    }

    //This function saves items by writing them into the data file
    private void saveItem(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        }
        catch (IOException e){
            Log.e("MainActivity","Error writing items",e);
        }
    }


}