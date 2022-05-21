package csc315.csp.nickhandbergscontactsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Declares and initializes constant DB_NAME and VERSION
    private final static String DB_NAME = "contactsDB";
    private final static int VERSION = 1;

    public DatabaseHelper(Context context) {
        // Calls the superclass constructor passing in values
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Creates table with auto increment id, name, phone, address, and email columns
        database.execSQL("CREATE TABLE contact_table " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "phone TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "email TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // Deletes and creates new table on upgrade
        database.execSQL("DROP TABLE IF EXISTS contact_table");
        onCreate(database);
    }

    public void add(ArrayList<String> data) {
        // Creates new ContentValues object and stores key value pairs of the column name and data
        ContentValues values = new ContentValues();
        values.put("name", data.get(0));
        values.put("phone", data.get(1));
        values.put("address", data.get(2));
        values.put("email", data.get(3));

        // Gets writable database and inserts the data into the contact table
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert("contact_table", null, values);
        database.close();
    }

    public ArrayList<ArrayList<String>> getAll() {
        // Gets readable database and crates new ArrayList of type ArrayList<String>
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<ArrayList<String>> data = new ArrayList();

        //Creates cursor to query the entire table
        Cursor cursor = database.rawQuery("SELECT * FROM contact_table", null);
        if (cursor.moveToFirst()) {
            do {
                // do while loop that will group the row items into an ArrayList
                // Adds the resulting group list to the data ArrayList
                ArrayList<String> group = new ArrayList();
                group.add(cursor.getString(cursor.getColumnIndexOrThrow("id")));
                group.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                group.add(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                group.add(cursor.getString(cursor.getColumnIndexOrThrow("address")));
                group.add(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                data.add(group);
            } while (cursor.moveToNext());
        }
        // Closes the database and returns the data
        database.close();
        return data;
    }

    public void update(ArrayList<String> newValues) {
        // Creates new ContentValues object and stores key value pairs of the column name and data
        ContentValues values = new ContentValues();
        values.put("name", newValues.get(1));
        values.put("phone", newValues.get(2));
        values.put("address", newValues.get(3));
        values.put("email", newValues.get(4));

        // Gets writable database and inserts the data into the contact table where the id matches (updates row)
        SQLiteDatabase database = this.getWritableDatabase();
        database.update("contact_table", values, "id = ?",
                new String[]{newValues.get(0)});
    }

    public void remove(int id) {
        // Gets writable database and deletes row where id matches
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("contact_table", "id=?", new String[]{Integer.toString(id)});
        database.close();
    }
}

