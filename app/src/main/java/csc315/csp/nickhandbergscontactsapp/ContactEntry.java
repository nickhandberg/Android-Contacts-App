package csc315.csp.nickhandbergscontactsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class ContactEntry extends AppCompatActivity {
    // Declares and initializes arraylist data
    ArrayList<String> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_entry);

        // Declares and initializes EditText objects using the ids of the components
        EditText nameInput = (EditText) findViewById(R.id.nameET);
        EditText phoneInput = (EditText) findViewById(R.id.phoneET);
        EditText addressInput = (EditText) findViewById(R.id.addressET);
        EditText emailInput = (EditText) findViewById(R.id.emailET);
        // Adds a PhoneNumberFormattingTextWatcher to help the user enter a phone number
        phoneInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // Declares and initializes Button objects using the ids of the components
        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        // Sets new OnClickListener on saveButton
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adds the text results from the EditTexts to the data array and calls the finish method
                data.add(nameInput.getText().toString());
                data.add(phoneInput.getText().toString());
                data.add(addressInput.getText().toString());
                data.add(emailInput.getText().toString());
                finish();
            }
        });

        // Sets new OnClickListener on cancelButton
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Calls the finish method
                finish();
            }
        });
    }

    public void finish(){
        // Creates new intent and uses putExtra to return the data array
        Intent intentData = new Intent();
        intentData.putExtra("returnArray1",data);
        // Checks if the data array is not empty
        if(!data.isEmpty()){
            // If so, sets the result code to RESULT_OK (Add code will run in MainActivity)
            setResult(RESULT_OK, intentData);
        }else{
            // Otherwise sets the result code to 0 (No code will run in MainActivity)
            setResult(0, intentData);
        }
        // Calls the finish method of the superclass
        super.finish();
    }
}