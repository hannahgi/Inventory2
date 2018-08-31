package com.example.android.inventory2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + Contract.InventoryEntry.TABLE_NAME + " ("
                + Contract.InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + Contract.InventoryEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + Contract.InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + Contract.InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + Contract.InventoryEntry.COLUMN_SUPPLIER_PHONE + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version1, int version2) {

    }
}
