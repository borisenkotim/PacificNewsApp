package com.example.pnwalerts.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pnwalerts.database.DatabaseCreatorHelper;
import com.example.pnwalerts.database.EmailDBSchema;
import com.example.pnwalerts.database.ItemCursorWrapper;
import com.example.pnwalerts.database.ReadItemDBSchema;

import java.util.ArrayList;
import java.util.List;

public class DatabaseInstance {

    private static DatabaseInstance sInstance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseInstance(Context context) {
        mContext = context;
        mDatabase = new DatabaseCreatorHelper(context).getWritableDatabase();
    }

    public static DatabaseInstance getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new DatabaseInstance(context);
        }
        return sInstance;
    }

    public List<ReadItem> getReadItemList() {
        List<ReadItem> readItems = new ArrayList<>();

        ItemCursorWrapper cursor = queryPrograms(ReadItemDBSchema.ReadItemTable.NAME,
                null,null,null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                readItems.add(cursor.getReadItem());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return readItems;
    }

    //This is for matching read items for category eg. Everett, Snohomish....
    public List<ReadItem> getReadItemList(String category) {
        List<ReadItem> readItems = new ArrayList<>();

        ItemCursorWrapper cursor = queryPrograms(ReadItemDBSchema.ReadItemTable.NAME,
                ReadItemDBSchema.ReadItemTable.Cols.ITEM_CATEGORY + " = ?"
                ,new String[] {category},null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                readItems.add(cursor.getReadItem());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return readItems;
    }

    public ReadItem getItem(String hash) {
        ItemCursorWrapper cursor = queryPrograms(ReadItemDBSchema.ReadItemTable.NAME,
                ReadItemDBSchema.ReadItemTable.Cols.ITEM_HASH + " = ?"
                ,new String[] {hash},null);
        try {
            if(cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getReadItem();
        }
        finally {
            cursor.close();
        }
    }

    public String getEmail() {
        ItemCursorWrapper cursor = queryPrograms(EmailDBSchema.UserEmailTable.NAME,
                null,null,null);

        try{
            if(cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();
            return cursor.getEmail();
        }
        finally{
            cursor.close();
        }
    }

    public void addEmail(String email) {
        ContentValues values = getContentValues(email);
        mDatabase.insert(EmailDBSchema.UserEmailTable.NAME, null, values);
    }

    public void addReadItem(ReadItem newItem) {
        ContentValues values = getContentValues(newItem);
        mDatabase.insert(ReadItemDBSchema.ReadItemTable.NAME, null, values);
    }

    public void updateEmail(String email) {
        ContentValues values = getContentValues(email);

        mDatabase.update(EmailDBSchema.UserEmailTable.NAME, values,
                EmailDBSchema.UserEmailTable.Cols.USER_EMAIL + " = ?",
                new String[] {email});
    }

    public void updateItem(ReadItem oldItem) {
        ContentValues values = getContentValues(oldItem);

        mDatabase.update(ReadItemDBSchema.ReadItemTable.NAME, values,
                ReadItemDBSchema.ReadItemTable.Cols.ITEM_HASH + " = ?",
                new String[] {oldItem.getHash()});
    }

    public void deleteItem(ReadItem oldItem) {
        mDatabase.delete(ReadItemDBSchema.ReadItemTable.NAME,
                ReadItemDBSchema.ReadItemTable.Cols.ITEM_HASH + " = ?",
                new String[] {oldItem.getHash()});
    }

    private static ContentValues getContentValues(ReadItem newItem) {
        ContentValues values = new ContentValues();
        values.put(ReadItemDBSchema.ReadItemTable.Cols.ITEM_HASH, newItem.getHash());
        values.put(ReadItemDBSchema.ReadItemTable.Cols.ITEM_CATEGORY, newItem.getCategory());
        values.put(ReadItemDBSchema.ReadItemTable.Cols.ITEM_READ, newItem.wasRead());
        return values;
    }

    private static ContentValues getContentValues(String email) {
        ContentValues values = new ContentValues();
        values.put(EmailDBSchema.UserEmailTable.Cols.USER_EMAIL, email);
        return values;
    }

    private ItemCursorWrapper queryPrograms(String tableName, String whereClause, String[] whereArgs, String orderBy) {
        Cursor cursor = mDatabase.query(tableName, null,whereClause, whereArgs, null,null,orderBy);
        return new ItemCursorWrapper(cursor);
    }


}
