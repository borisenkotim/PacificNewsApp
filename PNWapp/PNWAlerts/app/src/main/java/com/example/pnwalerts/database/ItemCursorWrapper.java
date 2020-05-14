package com.example.pnwalerts.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.pnwalerts.model.ReadItem;

public class ItemCursorWrapper extends CursorWrapper {

    public ItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ReadItem getReadItem() {
        String hash = getString(getColumnIndex(ReadItemDBSchema.ReadItemTable.Cols.ITEM_HASH));
        int wasRead = getInt(getColumnIndex(ReadItemDBSchema.ReadItemTable.Cols.ITEM_READ));
        String category = getString(getColumnIndex(ReadItemDBSchema.ReadItemTable.Cols.ITEM_CATEGORY));

        ReadItem newItem = new ReadItem();
        newItem.setHash(hash);
        //if 1 then true, else false
        newItem.setWasRead(wasRead == 1);
        newItem.setCategory(category);
        return newItem;
    }

    public String getEmail() {
        String email = getString(getColumnIndex(EmailDBSchema.UserEmailTable.Cols.USER_EMAIL));
        return email;
    }
}
