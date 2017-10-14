package eliaslander.bookapp;

import android.graphics.drawable.Drawable;

/**
 * This file represents a book
 *
 * @author lg188
 */

public class Book {
    private String title = "Undefined Title";
    private String description = "Undefined Description";
    // TODO: define how to get these images
    private Drawable image;

    /**
     * Create a book (no cover)
     *
     * @param title The title of the book
     * @param description The Description of the book
     */
    public Book(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
