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
    // The ID of the next row number, used for Insertions.
    private static int NEXT_ROW_ID_NUMBER;

    /**
     * Simple Constructor for Database Connection
     */
    private DatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setupNextRowIdNumber();
    }

    /**
     * Allows retrieval of the Singleton Instance,
     * in the app we'll call DatabaseConnection.getInstance(getApplicationContext()) to get the
     * instance.
     */
    public static synchronized DatabaseConnection getInstance(Context context) {
        // Initialise the instance if it's null.
        if (ourInstance == null) ourInstance = new DatabaseConnection(context);

        return ourInstance;
    }

    /**
     * Used by the Android System to Create the Database Initially.
     *
     * @param database The Database Instance from Android System.
     */
    @Override
    public void onCreate(SQLiteDatabase database) {

        // SQL Query for creating the table.
        final String CREATE_BLOGPOSTS_TABLE = "CREATE TABLE " + TABLE_BLOGPOSTS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TITLE + " TEXT)";
        database.execSQL(CREATE_BLOGPOSTS_TABLE);
    }

    /**
     * Used by the Android System to Upgrade the database version.
     *
     * @param database   The Database Instance from Android System.
     * @param oldVersion The Old version of the Database.
     * @param newVersion The New version of the Database.
     */
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

    /**
     * Adds a new BlogPost to the Database.
     * It performs a check to see if the BlogPost is unique.
     *
     * @param blogPost The BlogPost to add.
     */
    public synchronized void addBlogPost(BlogPost blogPost) {

        // Abandon if new BlogPost is a duplicate.
        if (isBlogPostInDatabase(blogPost)) return;

        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, NEXT_ROW_ID_NUMBER);
        contentValues.put(KEY_TITLE, blogPost.getTitle());

        database.insert(TABLE_BLOGPOSTS, null, contentValues);
        NEXT_ROW_ID_NUMBER++;
        database.close();
    }

    /**
     * Gets all the BlogPost objects that are in the Database.
     *
     * @return All the BlogPosts in the Database.
     */
    public synchronized List<BlogPost> getAllBlogPosts() {

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

    /**
     * Locates a BlogPost using it's Id.
     * Useful for ListView's OnClickListener, when dealing with positions that correspond to the
     * indices in the database, e.g. FragmentListView in a DrawerPattern.
     *
     * @param id The Id of the BlogPost to search the Database for.
     * @return The BlogPost corresponding to the Id
     */
    public synchronized BlogPost getBlogPost(int id) {

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

    /**
     * Updates the BlogPost in the Database. Uses the BlogPost's Id to do the lookup.
     *
     * @param updatedBlogPost The BlogPost to update.
     */
    public synchronized void updateBlogPost(BlogPost updatedBlogPost) {

        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, updatedBlogPost.getId());
        contentValues.put(KEY_TITLE, updatedBlogPost.getTitle());

        String whereClause = KEY_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(updatedBlogPost.getId())};

        database.update(TABLE_BLOGPOSTS, contentValues, whereClause, whereArgs);

        database.close();
    }

    /**
     * Deletes a BlogPost from the Database.
     * It checks if the BlogPost exists in the Database before proceeding.
     *
     * @param blogPostToDelete The BlogPost to delete.
     */
    public synchronized void deleteBlogPost(BlogPost blogPostToDelete) {

        // If the Id is not set, discover the ID;
        if (blogPostToDelete.getId() == -1) {
            int blogPostId = findBlogPostRowId(blogPostToDelete);

            if (blogPostId == -1) return; // Exit deletion process, as BlogPost is not in Database.
            else blogPostToDelete.setId(blogPostId); // Continue with deletion of existing BlogPost.
        }

        String whereClause = KEY_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(blogPostToDelete.getId())};

        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_BLOGPOSTS, whereClause, whereArgs);

        database.close();
    }

    /**
     * Gets the number of BlogPosts in the database.
     * Useful for Array sizing.
     *
     * @return The number of BlogPosts in the database.
     */
    public synchronized int getBlogPostsCount() {

        String[] columns = new String[]{KEY_ID};

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_BLOGPOSTS, columns, null, null, null, null, null);
        int numberOfBlogPosts = cursor.getCount();

        cursor.close();
        database.close();

        return numberOfBlogPosts;
    }

    /**
     * Searches the database for the BlogPost.
     * This Search uses the BlogPost's equals() method.
     *
     * @param blogPost The BlogPost to search for.
     * @return True, if the BlogPost is in the database.
     * False, if the BlogPost is not in the database.
     */
    public synchronized boolean isBlogPostInDatabase(BlogPost blogPost) {

        List<BlogPost> blogPosts = getAllBlogPosts();

        for (BlogPost blogPostInList : blogPosts) {
            if (blogPostInList.equals(blogPost))
                return true;
        }

        return false;
    }

    /**
     * Finds the Row Id of the BlogPost in the database.
     * This Search uses the BlogPost's equals() method.
     *
     * @param blogPost The BlogPost to search for.
     * @return The Row Id of the BlogPost, if it exists.
     * -1, if the BlogPost does not exist.
     */
    public synchronized int findBlogPostRowId(BlogPost blogPost) {

        List<BlogPost> blogPosts = getAllBlogPosts();

        for (BlogPost blogPostInList : blogPosts) {
            if (blogPostInList.equals(blogPost))
                return blogPostInList.getId();
        }

        return -1;
    }

    /**
     * Sets up the value for the next Row ID.
     * This is required for Insertion.
     * This is used at creation of the DatabaseConnection Instance.
     */
    private synchronized void setupNextRowIdNumber() {

        List<BlogPost> blogPosts = getAllBlogPosts();

        if (blogPosts.size() == 0)
            NEXT_ROW_ID_NUMBER = 0;
        else
            NEXT_ROW_ID_NUMBER = 1 + blogPosts.get(blogPosts.size() - 1).getId();
    }
}
