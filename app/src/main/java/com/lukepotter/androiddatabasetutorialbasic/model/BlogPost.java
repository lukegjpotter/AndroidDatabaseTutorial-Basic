package com.lukepotter.androiddatabasetutorialbasic.model;

/**
 * BlogPost.java
 *
 * @author Luke GJ Potter - lukegjpotter
 * @version 1.0
 *          <p/>
 *          Description:
 *          This is a POJO for a Blog Post.
 *          Date:
 *          02/Mar/2015
 */
public class BlogPost {

    private int id;
    private String title;

    /**
     * Empty Constructor
     */
    public BlogPost() {
    }

    /**
     * Full Constructor
     */
    public BlogPost(int id, String title) {

        setId(id);
        setTitle(title);
    }

    /**
     * Getters and Setters
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
