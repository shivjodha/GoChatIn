package halper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Dell on 6/14/2017.
 */

public class SQLiteHandler  extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "android_api1";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String USER_TYPE = "user_type";
    private static final String MID = "mid";
    private static final String AGENT_ID = "agent_id";
    private static final String DEPARTMENT = "dept";
    private static final String LOGGED_IN = "logged_in";
    private static final String ONLINE_STATUS ="online_status";
    private static final String Mobile_Number ="mobile_number";
    private static final String Code = "code";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + USER_TYPE + " TEXT,"
                + MID + " TEXT," + AGENT_ID +" TEXT," + DEPARTMENT +" TEXT," + LOGGED_IN +" TEXT," + Mobile_Number + " TEXT," + Code +" Text," + ONLINE_STATUS +" TEXT"+ ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String user_type, String mid , String agent_id, String dept, String logged_in, String mobile_number, String code, String online_status ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(USER_TYPE, user_type); // Email
        values.put(MID, mid); // Created At
        values.put(AGENT_ID, agent_id);
        values.put(DEPARTMENT, dept);
        values.put(LOGGED_IN, logged_in );
        values.put(Mobile_Number,mobile_number);
        values.put(Code,code);
        values.put(ONLINE_STATUS, online_status);
        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("user_type", cursor.getString(3));
            user.put("mid", cursor.getString(4));
            user.put("agent_id",cursor.getString(5));
            user.put("dept",cursor.getString(6));
            user.put("logged_in",cursor.getString(7));
            user.put("mobile_number",cursor.getString(8));
            user.put("code",cursor.getString(9));
            user.put("online_status",cursor.getString(10));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
