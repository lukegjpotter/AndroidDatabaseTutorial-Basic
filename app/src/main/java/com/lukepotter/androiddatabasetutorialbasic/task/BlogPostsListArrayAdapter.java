package com.lukepotter.androiddatabasetutorialbasic.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lukepotter.androiddatabasetutorialbasic.R;
import com.lukepotter.androiddatabasetutorialbasic.model.BlogPost;

import java.util.List;

/**
 * BlogPostsListArrayAdapter.java
 *
 * @author Luke GJ Potter - lukegjpotter
 * @version 1.0
 *          <p/>
 *          Description:
 *          This is an ArrayAdapter subclass, it utilises the ViewHolder pattern to
 *          populate the row in the ListView.
 *          Date:
 *          17/Oct/2015
 */
public class BlogPostsListArrayAdapter extends ArrayAdapter<BlogPost> {

    Context context;
    int layoutResourceId;
    List<BlogPost> blogPosts;

    public BlogPostsListArrayAdapter(Context context, int layoutResourceId, List<BlogPost> blogPosts) {
        super(context, layoutResourceId, blogPosts);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.blogPosts = blogPosts;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {

        BlogPostViewHolder blogPostViewHolder;

        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(layoutResourceId, parent, false);

            blogPostViewHolder = new BlogPostViewHolder();
            blogPostViewHolder.blogPostTitle = (TextView) row.findViewById(R.id.row_blog_post_title);

            row.setTag(blogPostViewHolder);
        } else {
            blogPostViewHolder = (BlogPostViewHolder) row.getTag();
        }

        BlogPost blogPost = blogPosts.get(position);
        blogPostViewHolder.blogPostTitle.setText(blogPost.getTitle());

        return row;
    }

    static class BlogPostViewHolder {
        TextView blogPostTitle;
    }
}
