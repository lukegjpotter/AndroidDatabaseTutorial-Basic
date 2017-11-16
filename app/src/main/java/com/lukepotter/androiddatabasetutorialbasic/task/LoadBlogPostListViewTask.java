package com.lukepotter.androiddatabasetutorialbasic.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.lukepotter.androiddatabasetutorialbasic.database.DatabaseConnection;
import com.lukepotter.androiddatabasetutorialbasic.model.BlogPost;

import java.util.List;


/**
 * LoadBlogPostListViewTask.java
 *
 * @author Luke GJ Potter - lukegjpotter
 * @version 1.0
 *          <p/>
 *          Description:
 *          This is an AsyncTask to populate the ListView using the BlogPostsListArrayAdapter.
 *          Date:
 *          17/Oct/2015
 */
public class LoadBlogPostListViewTask extends AsyncTask<Void, Void, Void> {

    List<BlogPost> blogPosts;
    DatabaseConnection databaseConnection;
    Context context;
    ListView blogPostsListView;

    public LoadBlogPostListViewTask(Context applicationContext, ListView blogPostsListView) {

        this.context = applicationContext;
        this.blogPostsListView = blogPostsListView;
    }

    @Override
    protected Void doInBackground(Void... params) {

        databaseConnection = DatabaseConnection.getInstance(context);
        blogPosts = databaseConnection.getAllBlogPosts();

        return null;
    }

    @Override
    protected void onPostExecute(Void arg0) {

        BlogPostsListArrayAdapter blogPostsListArrayAdapter = new BlogPostsListArrayAdapter(context, R.layout.view_blog_post_list, blogPosts);
        blogPostsListView.setAdapter(blogPostsListArrayAdapter);
    }
}