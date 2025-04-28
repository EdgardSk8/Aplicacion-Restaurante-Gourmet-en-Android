package com.example.tarea5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RestaurantDbHelper extends SQLiteOpenHelper {

    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "Restaurante.db";

    /* ------------------------- CREACION DE LA BASE DE DATOS ------------------------- */

    private static final String SQL_CREATE_DISH_ENTRIES =
            "CREATE TABLE " + RestaurantContract.DishEntry.TABLE_NAME +
                    " (" + RestaurantContract.DishEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RestaurantContract.DishEntry.COLUMN_NAME_DISH + " TEXT, " +
                    RestaurantContract.DishEntry.COLUMN_DESCRIPTION_DISH + " TEXT, " +
                    RestaurantContract.DishEntry.COLUMN_PRICE_PRICE + " TEXT, " +
                    RestaurantContract.DishEntry.COLUMN_IMAGE_DISH + " TEXT)";


    private static final String SQL_DELETE_ENTRIES_DISH = "DROP TABLE IF EXISTS " + RestaurantContract.DishEntry.TABLE_NAME;
    /* --------------------------------------------------------------------------------- */

    public RestaurantDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DISH_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_DISH);
        onCreate(db);
    }
}