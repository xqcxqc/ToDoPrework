package com.example.todoprework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editItem;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //grab references to the views in the layout
        editItem = findViewById(R.id.editItem);
        btnSave = findViewById(R.id.btnSave);

        //give the title of this activity
        getSupportActionBar().setTitle("Edit Item");

        //use the data that we passed into our intent in the edit activity

        //getStringExtra --> get text of the item and pass the key in mainactivity
        // editItem.setText() --> pre-populate the orginal edit text
        editItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        //add the clickListenent on the button.
        //when the user is done editing, they click the save button. And we want
        //to return back to the main activity. This is done by using intent
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create an intent whcih will contain the results
                Intent intent = new Intent();

                //pass the data(result)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT,editItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION,getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                //set the result of the intent
                setResult(RESULT_OK,intent);

                //finish the activity --> close the screen and go back
                finish();
            }
        });

    }
}