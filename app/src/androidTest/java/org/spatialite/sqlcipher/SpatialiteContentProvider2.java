package org.spatialite.sqlcipher;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.spatialite.database.SQLiteDatabase;
import org.spatialite.database.SQLiteQueryBuilder;

import java.io.File;

public class SpatialiteContentProvider2 extends ContentProvider {

    public static String DATABASE_NAME = "interprocess_test.db";
    public static final Uri CONTENT_URI =
            Uri.parse("content://org.spatialite.testsuite.provider2");

    private SQLiteDatabase database;

    static {
        System.loadLibrary("android_spatialite");
    }

    public SpatialiteContentProvider2() {

    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        File databasePath = getContext().getDatabasePath(DATABASE_NAME);
        database = SQLiteDatabase.openOrCreateDatabase(databasePath, null);

        createDatabaseWithData(database);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("t1");
        return builder.query(database, new String[]{"a", "b"}, null, null, null, null, null);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    private void createDatabaseWithData(SQLiteDatabase database) {
        database.execSQL("create table if not exists t1(a varchar, b blob);");
        ContentValues values = new ContentValues();
        values.put("a", "one for the money");
        values.put("b", "two for the show".getBytes());
        database.insert("t1", null, values);
    }
}
