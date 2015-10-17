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
        setId(-1);
    }

    /**
     * Construcor with only the title.
     * @param title The Title of the BlogPost
     */
    public BlogPost(String title) {
        setId(-1);
        setTitle(title);
    }

    /**
     * Full Constructor
     * @param id The ID number of the BlogPost in the Database
     * @param title The Title of the BlogPost
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

    /**
     * Determines if this Instance is equal to the argument.
     * It makes the comparison on the case insensitive titles of the BlogPost Objects.
     *
     * @param o Any Object.
     * @return True, if o is the same as this Instance.
     * False, if o is not the same as this Instance.
     */
    @Override
    public boolean equals(Object o) {

        return o instanceof BlogPost && this.title.equalsIgnoreCase(((BlogPost) o).getTitle());
    }
}
