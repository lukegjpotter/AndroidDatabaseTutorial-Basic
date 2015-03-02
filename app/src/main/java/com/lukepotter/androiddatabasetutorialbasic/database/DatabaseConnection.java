package com.lukepotter.androiddatabasetutorialbasic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lukepotter.androiddatabasetutorialbasic.model.BlogPost;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseConnection.java
 *
 * @author Luke GJ Potter - lukegjpotter
 * @version 1.0
 *          <p/>
 *          Description:
 *          This is the class that handles the creation of the database, upgrades of the database,
 *          and the various queries.
 *          Date:
 *          02/Mar/2015
 */
public class DatabaseConnection extends SQLiteOpenHelper {

    // Database Information.
    private static final String DATABASE_NAME = "BlogPost";
    private static final int DATABASE_VERSION = 1;
    // Table Name.
    private static final String TABLE_BLOGPOSTS = "blogposts";
    // Blog Post Table Columns, these map to the public fields on the BlogPost class.
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    // Singleton Instance.
    private static DatabaseConnection ourInstance;

    /**
     * Simple Constructor for Database Connection
     */
    private DatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Allows retrieval of the Singleton Instance,
     * in the app we'll call DatabaseConnection.getInstance(this) to get the instance.
     */
    public static DatabaseConnection getInstance(Context context) {
        // Initialise the instance if it's null.
        if (ourInstance == null) ourInstance = new DatabaseConnection(context);

        return ourInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        // SQL Query for creating the table.
        final String CREATE_BLOGPOSTS_TABLE = "CREATE TABLE " + TABLE_BLOGPOSTS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TITLE + " TEXT)";
        database.execSQL(CREATE_BLOGPOSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        // Drop the older table, if there's a newer table, and create the newer table.
        if (newVersion > oldVersion) {
            // SQL Query for Deleting (dropping) the table.
            final String DROP_BLOGPOSTS_TABLE = "DROP TABLE IF EXISTS " + TABLE_BLOGPOSTS;
            database.execSQL(DROP_BLOGPOSTS_TABLE);

            onCreate(database);
        }
    }

    public void addBlogPost(BlogPost blogPost) {

        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, blogPost.getId());
        contentValues.put(KEY_TITLE, blogPost.getTitle());

        database.insert(TABLE_BLOGPOSTS, null, contentValues);
        database.close();
    }

    public List<BlogPost> getAllBlogPosts() {

        List<BlogPost> blogPosts = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_BLOGPOSTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                blogPosts.add(new BlogPost(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return blogPosts;
    }

    public BlogPost getBlogPost(int id) {

        BlogPost blogPost = null;

        String selection = KEY_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        String limit = String.valueOf(1);

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(
                TABLE_BLOGPOSTS, null, selection, selectionArgs, null, null, null, limit);

        if (cursor.moveToFirst()) {
            blogPost = new BlogPost(
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))),
                    cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
        }

        cursor.close();
        database.close();

        return blogPost;
    }

    public void updateBlogPost(BlogPost updatedBlogPost) {

        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, updatedBlogPost.getId());
        contentValues.put(KEY_TITLE, updatedBlogPost.getTitle());

        String whereClause = KEY_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(updatedBlogPost.getId())};

        database.update(TABLE_BLOGPOSTS, contentValues, whereClause, whereArgs);

        database.close();
    }

    public void deleteBlogPost(BlogPost blogPostToDelete) {

        String whereClause = KEY_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(blogPostToDelete.getId())};

        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_BLOGPOSTS, whereClause, whereArgs);

        database.close();
    }

    public int getBlogPostsCount() {

        String[] columns = new String[]{KEY_ID};

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_BLOGPOSTS, columns, null, null, null, null, null);
        int numberOfBlogPosts = cursor.getCount();

        cursor.close();
        database.close();

        return numberOfBlogPosts;
    }
}
