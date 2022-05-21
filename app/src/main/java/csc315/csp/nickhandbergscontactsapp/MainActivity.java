package csc315.csp.nickhandbergscontactsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Declares needed instance variables
    ArrayList<String> listViewItems = new ArrayList<String>();
    ArrayList<ArrayList<String>> itemGroups;
    ArrayAdapter<String> adapter;
    private ListView myListView;
    private DatabaseHelper dbHelper = null;
    private int listPosition;


    @Override   // On create method which overrides the method in AppCompatActivity class
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creates new DatabaseHelper instance passing in the context and stores all the items in itemGroups
        dbHelper = new DatabaseHelper(this);
        itemGroups = dbHelper.getAll();

        // Checks if the itemGroups is not empty, if so adds the name and number to the list view array
        if(!itemGroups.isEmpty()){
            for(int i=0; i<itemGroups.size(); i++){
                listViewItems.add(itemGroups.get(i).get(1)+"         "+itemGroups.get(i).get(2));
            }
        }

        // Creates intents for the ContactEntry and ContactDetail activities
        Intent contactEntry = new Intent(this, ContactEntry.class);
        Intent contactDetails = new Intent(this, ContactDetails.class);

        // Links the list view with the id of the element and creates/sets the array adapter
        myListView = findViewById(R.id.contactsLV);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,
                listViewItems);
        myListView.setAdapter(adapter);

        // sets an OnItemClickListener to the list view
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Sets the list position to the index of the clicked item (used later for deletion)
                listPosition = i;
                // Uses putExtra to pass the selected groups array to the activity
                // Starts the activity Contact Details for result
                contactDetails.putExtra("inputArray", dbHelper.getAll().get(i));
                startActivityForResult(contactDetails, 2);
            }
        });

        // Creates the FAB object finding by id and sets a OnClickListener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addContactFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts the activity ContactEntry for result
                startActivityForResult(contactEntry, 1);
            }
        });
    }

    // Defines the onActivityResult method
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Declares ArrayList for the returned array
        ArrayList<String> returnArray;
        // Checks if the request code matches the code that was passed in for the FAB onClick method call (1)
        if(requestCode == 1 && resultCode == RESULT_OK){
            if(data.hasExtra("returnArray1")){
                // Stores the returned array into returnArray
                returnArray = data.getExtras().getStringArrayList("returnArray1");
                // Adds the name and number to the list view array, adds the array to the database
                // ... and notifies the adapter that the data set has changed
                listViewItems.add(returnArray.get(0)+"         "+returnArray.get(1));
                dbHelper.add(returnArray);
                adapter.notifyDataSetChanged();
            }
        }
        // Checks if the request code matches the code passed in for the list view onItemClick method call (2)
        // As well as if the result code is RESULT_OK (update)
        if(requestCode == 2 && resultCode == RESULT_OK){
            if(data.hasExtra("returnArray2")){
                // Stores the returned array into returnArray
                returnArray = data.getExtras().getStringArrayList("returnArray2");
                // Sets the name and number to the list view array, updates the array in the database
                // ... and notifies the adapter that the data set has changed
                listViewItems.set(listPosition, returnArray.get(1)+"         "+returnArray.get(2));
                dbHelper.update(returnArray);
                adapter.notifyDataSetChanged();
            }
        }
        // Checks if the request code matches the code passed in for the list view onItemClick method call (2)
        // As well as if the result code is RESULT_OK-1 (delete)
        if(requestCode == 2 && resultCode == RESULT_OK-1){
            if(data.hasExtra("returnArray2")){
                // Stores the returned array into returnArray
                returnArray = data.getExtras().getStringArrayList("returnArray2");
                // Removes the name and number in the list view array, removes the array in the database
                // ... and notifies the adapter that the data set has changed
                listViewItems.remove(listPosition);
                dbHelper.remove(Integer.parseInt(returnArray.get(0)));
                adapter.notifyDataSetChanged();
            }
        }
    }
}