package com.example.pnwalerts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseCreatorHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "readListBase.db";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String FOREIGN_KEY = "FOREIGN KEY";
    private static final String REFERENCES = " REFERENCES ";

    private static final String READ_TABLE = CREATE_TABLE + ReadItemDBSchema.ReadItemTable.NAME +
            "(" + ReadItemDBSchema.ReadItemTable.Cols.ITEM_HASH + PRIMARY_KEY + ", " +
            ReadItemDBSchema.ReadItemTable.Cols.ITEM_CATEGORY + "," +
            ReadItemDBSchema.ReadItemTable.Cols.ITEM_READ + " INTEGER DEFAULT 0);";

    public static final String EMAIL_TABLE = CREATE_TABLE + EmailDBSchema.UserEmailTable.NAME +
            "(" + EmailDBSchema.UserEmailTable.Cols.USER_EMAIL + PRIMARY_KEY + ");";

    public DatabaseCreatorHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(READ_TABLE);
        sqLiteDatabase.execSQL(EMAIL_TABLE);
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
    }
}
