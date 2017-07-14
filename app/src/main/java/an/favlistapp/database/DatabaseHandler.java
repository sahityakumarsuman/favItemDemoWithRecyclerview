package an.favlistapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import an.favlistapp.util.UserDataUtils;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "favourite";

    // FavItem table name
    private static final String TABLE_FAV = "favlist";

    // FavItem Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "desc";
    private static final String KEY_TYPE = "offer";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_VIEW_COUNT = "view_count";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVITEM_TABLE = "CREATE TABLE " + TABLE_FAV + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT," + KEY_IMAGE_URL + " TEXT," + KEY_TYPE + " TEXT," + KEY_VIEW_COUNT + " TEXT" + ")";
        db.execSQL(CREATE_FAVITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);

        // Create tables again
        onCreate(db);
    }


    public void addFav(UserDataUtils fav) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, fav.get_title());
        values.put(KEY_DESCRIPTION, fav.get_description());
        values.put(KEY_IMAGE_URL, fav.get_imageUrl());
        values.put(KEY_TYPE, fav.get_type());
        values.put(KEY_VIEW_COUNT, fav.get_viewCount());

        // Inserting Row
        db.insert(TABLE_FAV, null, values);
        db.close(); // Closing database connection
    }

    public UserDataUtils getFavItemSingel(String title_query) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAV, new String[]{KEY_ID,
                        KEY_TITLE, KEY_DESCRIPTION, KEY_IMAGE_URL, KEY_TYPE, KEY_VIEW_COUNT}, KEY_TITLE + "=?",
                new String[]{title_query}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        UserDataUtils favItem = new UserDataUtils();
        favItem.set_title(cursor.getString(1));
        favItem.set_description(cursor.getString(2));
        favItem.set_imageUrl(cursor.getString(3));
        favItem.set_type(cursor.getString(4));
        favItem.set_viewCount(cursor.getString(5));
        favItem.set_fav(true);
        return favItem;
    }

    public List<UserDataUtils> getAllFavItem() {
        List<UserDataUtils> favList = new ArrayList<UserDataUtils>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAV;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserDataUtils listUtils = new UserDataUtils();
                listUtils.set_title(cursor.getString(1));
                listUtils.set_description(cursor.getString(2));
                listUtils.set_imageUrl(cursor.getString(3));
                listUtils.set_type(cursor.getString(4));
                listUtils.set_viewCount(cursor.getString(5));
                favList.add(listUtils);
            } while (cursor.moveToNext());
        }

        return favList;
    }


    public int getFavItemCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_FAV;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteFavItem(UserDataUtils favItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAV, KEY_TITLE + " = ?",
                new String[]{favItem.get_title()});
        db.close();
    }

    public boolean ifExists(UserDataUtils favItem) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String checkQuery = "SELECT " + KEY_TITLE + " FROM " + TABLE_FAV + " WHERE " + KEY_TITLE + "= '" + favItem.get_title() + "'";
        cursor = db.rawQuery(checkQuery, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

}
