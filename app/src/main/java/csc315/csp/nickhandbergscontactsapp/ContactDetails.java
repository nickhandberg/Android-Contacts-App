package csc315.csp.nickhandbergscontactsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactDetails extends AppCompatActivity implements OnMapReadyCallback {
    // Declares needed instance variables
    ArrayList<String> inputArray;
    ArrayList<String> updateList = new ArrayList<>();
    private boolean isUpdate = true;
    private MapView mapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    LatLng latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // Declares and initializes TextView objects using the ids of the components
        TextView nameTV = findViewById(R.id.nameDetailTV);
        TextView phoneTV = findViewById(R.id.phoneDetailTV);
        TextView addressTV = findViewById(R.id.addressDetailTV);
        TextView emailTV = findViewById(R.id.emailDetailTV);

        // Declares and initializes EditText objects using the ids of the components
        EditText nameET = findViewById(R.id.nameDetailET);
        EditText phoneET = findViewById(R.id.phoneDetailET);
        EditText addressET = findViewById(R.id.addressDetailET);
        EditText emailET = findViewById(R.id.emailDetailET);
        // Adds a PhoneNumberFormattingTextWatcher to help the user enter a phone number
        phoneET.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // Declares and initializes Button objects using the ids of the components
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        Button updateButton = (Button) findViewById(R.id.updateButton);
        Button callButton = (Button) findViewById(R.id.callButton);
        Button emailButton = (Button) findViewById(R.id.emailButton);

        // Gets the extras to get the passed in data from the MainActivity
        Bundle extras = getIntent().getExtras();
        //If the extras bundle is not null, stores the passed in array into the instance variable inputArray
        // Sets the text of the TextViews using the inputArray data
        if(extras != null){
            inputArray = extras.getStringArrayList("inputArray");
            nameTV.setText(inputArray.get(1));
            phoneTV.setText(inputArray.get(2));
            addressTV.setText(inputArray.get(3));
            emailTV.setText(inputArray.get(4));
        }
        // Copies the data from the inputArray into the updateList
        updateList.addAll(inputArray);

        // Creates instance of LatLng with default lat and long of 0
        latlng = new LatLng(0,0);

        // Tries to find the address from the inputArray's address and set the correct latlng values
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(inputArray.get(3), 1);
            latlng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        // Initializes mapView object using the component id and calls the onCreate and getMapAsync methods
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        // Sets new OnClickListener on the callButton
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creates intent to open the dialer passing in the phone number
                Intent openDialer = new Intent(Intent.ACTION_DIAL);
                openDialer.setData(Uri.parse("tel:"+inputArray.get(2)));
                startActivity(openDialer);
            }
        });

        // Sets new OnClickListener on the emailButton
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creates intent to open app chooser for email and passes in the email address
                Intent openEmail = new Intent(Intent.ACTION_SENDTO);
                openEmail.setData(Uri.parse("mailto:"));
                openEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {inputArray.get(4)});
                startActivity(Intent.createChooser(openEmail, ""));
            }
        });

        // Sets new OnClickListener on the deleteButton
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sets isUpdate to false and calls the finish method
                isUpdate = false;
                finish();
            }
        });

        // Sets new OnClickListener on the updateButton
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sets the indexes of updateList to the returned text of the EditTexts
                updateList.set(1,nameET.getText().toString());
                updateList.set(2,phoneET.getText().toString());
                updateList.set(3,addressET.getText().toString());
                updateList.set(4,emailET.getText().toString());

                // For loop that will set any index that contains an empty string to the original inputArray value
                // (Does not update the data where the user did not enter anything)
                for(int i=1; i<updateList.size();i++){
                    if(updateList.get(i).equals("")){
                        updateList.set(i, inputArray.get(i));
                    }
                }
                // Sets isUpdate to true and calls the finish method
                isUpdate = true;
                finish();
            }
        });
    }

    public void finish(){
        // Creates new intent and uses putExtra to store the updateList array for return to MainActivity
        Intent intentData = new Intent();
        intentData.putExtra("returnArray2",updateList);

        // Checks if the updateList is not empty and isUpdate is true (update button pressed)
        if(!updateList.isEmpty() && isUpdate){
            // Sets result code to RESULT_OK (Update code will be run in Main Activity)
            setResult(RESULT_OK, intentData);
        }
        //Checks if isUpdate is not true (delete button pressed)
        else if(!isUpdate) {
            // Sets result code to RESULT_OK-1 (Delete code will be run in Main Activity)
            setResult(RESULT_OK-1, intentData);
        }
        else{
            // Otherwise sets result code to 0 (No code will run in MainActivity)
            setResult(0, intentData);
        }
        // calls the finish method in the superclass
        super.finish();
    }

    // Override methods for the OnMapReadyCallback interface
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if(mapViewBundle == null){
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map){
        //Sets marker and camera position of the map to the latlng of the address
        map.addMarker(new MarkerOptions().position(latlng).title("Marker"));
        map.setMinZoomPreference(14.0f);
        map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
    }

    @Override
    protected void onPause(){
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }
}