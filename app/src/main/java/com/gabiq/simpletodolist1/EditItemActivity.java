package com.gabiq.simpletodolist1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EditItemActivity extends ActionBarActivity {

    long rowId = 0; // placed here so visible in onCreate and onEditItem

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        // ActivityTwo.java (subactivity) can access any extras passed in
        String position = getIntent().getStringExtra("position");
        //String rowId = getIntent().getStringExtra("rowId");
        rowId = getIntent().getLongExtra("rowId", 0);
        Toast.makeText(this, "rowID = " + rowId, Toast.LENGTH_SHORT).show(); // for testing
        EditText etEditItem = (EditText) findViewById(R.id.editView);
        etEditItem.setText(position); // sets edit box to selected text
        // Now put curser at end of text
        int positionOfCursor = position.length();
        etEditItem.setSelection(positionOfCursor); // puts cursor at end of text
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Send data back to first activity from second activity
    public void onEditItem(View v) { // hitting the Save button here
    //    EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
    //    String itemText = etNewItem.getText().toString();
    //    itemsAdapter.add(itemText);
    //    etNewItem.setText(""); // clears line for another entry
    //    saveItems(); // write to file
        EditText etEditItem = (EditText) findViewById(R.id.editView);
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("position", etEditItem.getText().toString());
        //data.putExtra("rowId", etEditItem.getText().toString());
        data.putExtra("rowId", rowId); // ints work too
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

}
