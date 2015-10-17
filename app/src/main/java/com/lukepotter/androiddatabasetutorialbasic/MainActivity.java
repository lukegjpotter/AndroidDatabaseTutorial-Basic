package com.lukepotter.androiddatabasetutorialbasic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lukepotter.androiddatabasetutorialbasic.database.DatabaseConnection;
import com.lukepotter.androiddatabasetutorialbasic.model.BlogPost;
import com.lukepotter.androiddatabasetutorialbasic.task.LoadBlogPostListViewTask;

public class MainActivity extends Activity {

    private EditText editText;
    private Button addButton, deleteButton;
    private ListView blogPostsListView;
    /**
     * The OnClickListener to add a BlogPost to the database.
     * It Spawns a thread that inserts the new unique BlogPost into the database.
     * It finally refreshes the ListView.
     */
    private OnClickListener addBlogPostOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Thread addNewBlogPostThread = new Thread() {

                @Override
                public void run() {
                    BlogPost blogPostToAdd = new BlogPost(editText.getText().toString().trim());
                    DatabaseConnection databaseConnection = DatabaseConnection.getInstance(getApplicationContext());
                    databaseConnection.addBlogPost(blogPostToAdd);

                    refreshBlogPostsListView();
                }
            };

            addNewBlogPostThread.start();
        }
    };
    /**
     * The OnClickListener to remove a BlogPost from the database.
     * It Spawns a new Thread to delete the BlogPost from the database.
     * Finally it refreshes the ListView.
     */
    private OnClickListener deleteBlogPostOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Thread deleteBlogPostThread = new Thread() {

                @Override
                public void run() {
                    BlogPost blogPostToDelete = new BlogPost(editText.getText().toString().trim());
                    DatabaseConnection databaseConnection = DatabaseConnection.getInstance(getApplicationContext());
                    databaseConnection.deleteBlogPost(blogPostToDelete);

                    refreshBlogPostsListView();
                }
            };

            deleteBlogPostThread.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUiResources();
    }

    private void setupUiResources() {

        editText = (EditText) findViewById(R.id.editText);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(addBlogPostOnClickListener);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(deleteBlogPostOnClickListener);

        blogPostsListView = (ListView) findViewById(R.id.blogPostListView);
        refreshBlogPostsListView();
    }

    /**
     * Refreshes the ListView using an AsyncTask.
     * This reduces the load on the UI Thread.
     */
    private void refreshBlogPostsListView() {

        LoadBlogPostListViewTask loadBlogPostListViewTask = new LoadBlogPostListViewTask(getApplicationContext(), blogPostsListView);
        loadBlogPostListViewTask.execute();
    }
}
