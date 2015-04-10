package com.gabiq.simpletodolist1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class TodoActivity extends ActionBarActivity {
    ListView lvItems;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    boolean confirmDelete = false; // variable to hold confirm deletion decision

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        //items = new ArrayList<String>(); // comment since loading now
        //Load items onCreate from file
        readItems(); // line added to read from file
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        //items.add("First Item"); // comment since loading now
        //items.add("Second Item"); // comment since loading now
        setupListViewListener(); // this listener is for a long click
        setupListViewEditListener(); // added to run listener for click, works!
        // above line results in second screen opening up successfully
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
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
            Toast.makeText(this, "You selected Settings!", Toast.LENGTH_SHORT).show(); // testing, works!
            return true;
        }
        if (id == R.id.action_settings2) {
            Toast.makeText(this, "You selected Version!", Toast.LENGTH_SHORT).show(); // testing, works!
            return true;
        }
        if (id == R.id.change_Background_Color) {
            change_Background_Color();
            return true;
        }

    return super.onOptionsItemSelected(item);
    }

    private void change_Background_Color() {
        int color = Color.argb(255, 128, 128, 128);
        lvItems.setBackgroundColor(color);
    }

// Below method might be edited since adding Save items
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText(""); // clears line for another entry
        saveItems(); // write to file
    }

    public void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view, int position, long rowId) {

                verifyDelete(); // to confirm deletion
                if (confirmDelete) { // if (confirmDelete == true)
                    items.remove(position);
                    itemsAdapter.notifyDataSetChanged();
                    saveItems(); // write to file
                }
                //confirmDelete = false;
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public boolean verifyDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this); //context
        // set dialog title
        alertDialogBuilder.setTitle("Confirm Delete?");
        // set dialog message
        alertDialogBuilder
                .setMessage("Click Yes to Delete.")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    //@Override
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, then delete item
                        confirmDelete = true;
                        //dialog.cancel();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    //@Override
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, do not delete item
                        confirmDelete = false;
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show dialog
        alertDialog.show();
    return confirmDelete;
    }

    //These two methods will add the ability to load/save from files!
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
            e.printStackTrace();
        }
    }
    private void saveItems() {
        //itemsAdapter.notifyDataSetChanged();
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// Add Edit Item Capability
    public void setupListViewEditListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long rowId) {
                // first parameter is the context, second is the class of the activity to launch
                Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
                // put "extras" into the bundle for access in the second activity
                String positionText = items.get(position);
                i.putExtra("position", positionText);// pass data to launched activity
                i.putExtra("rowId", rowId);// pass data to launched activity
                // REQUEST_CODE can be any value we like, used to determine the result type later
                int REQUEST_CODE = 0; // variable to get a result back from the second activity
                //startActivity(i); // brings up the second activity
                startActivityForResult(i, REQUEST_CODE); // brings up the second activity to get a result

                //return; // return unnecessary on a void method
            }
        });
    }

    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK ) { // && requestCode == REQUEST_CODE
            // Extract name value from result extras
            String position = data.getExtras().getString("position");
            //String rowId = data.getExtras().getString("rowId");
            long rowId = data.getExtras().getLong("rowId");
            //int code = data.getExtras().getInt("code", 0);
            // Toast the name to display temporarily on screen
            //Toast.makeText(this, position, Toast.LENGTH_SHORT).show(); // testing, works!
            Toast.makeText(this, "rowId = " + rowId, Toast.LENGTH_SHORT).show(); // testing, works!
            //EditText etEditedItem = (EditText) findViewById(R.id.etNewItem);
            // android.R.layout.simple_list_item_1

            //ListView etEditedItem = (ListView) findViewById(R.id.lvItems);

            //EditText etEditedItem = (EditText) findViewById(R.id.rowId);

            //etEditedItem.setText(position); // attempt to set text on proper line, does not work

            items.set((int)rowId,position); // This edits the proper line! Had to put (int) in.

            //String position1result = items.put(position);
            saveItems(); // write to file

            //View v1 = findViewById (R.id.lvItems); // attempt to get emulator to show Edit
            //v1.invalidate(); // attempt to get emulator to show Edit
            //v1 = setContentView(R.layout.activity_todo);// attempt to get emulator to show Edit
           //v1.layout.activity_todo.invalidate();// attempt to get emulator to show Edit
            itemsAdapter.notifyDataSetChanged(); // attempt to get emulator to show Edit
            //return; // return unnecessary in a void method
        }
    }
}
