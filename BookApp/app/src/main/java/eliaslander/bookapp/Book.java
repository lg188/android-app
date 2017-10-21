package eliaslander.bookapp;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * This class represents a book
 *
 * @author lg188
 */

class Book {
    private String title = "Undefined Title";
    private String author = "Undefined Author";
    // TODO: define how to get these images
    private Drawable image;

    /**
     * Create a book (no cover)
     *
     * @param title  Book title
     * @param author Book author
     */
    Book(String title, String author) {
        setTitle(title);
        setAuthor(author);
    }

    public String getTitle() {
        return title;
    }

    @SuppressWarnings("WeakerAccess")
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    String getAuthor() {
        return author;
    }

    @SuppressWarnings("WeakerAccess")
    void setAuthor(@NonNull String author) {
        this.author = author;
    }


}
