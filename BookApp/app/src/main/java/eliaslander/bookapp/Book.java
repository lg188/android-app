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
    private String description = "Undefined Description";
    // TODO: define how to get these images
    private Drawable image;

    /**
     * Create a book (no cover)
     *
     * @param title       Book title
     * @param description Book description
     */
    Book(String title, String description) {
        setTitle(title);
        setDescription(description);
    }

    public String getTitle() {
        return title;
    }

    @SuppressWarnings("WeakerAccess")
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    String getDescription() {
        return description;
    }

    @SuppressWarnings("WeakerAccess")
    void setDescription(@NonNull String description) {
        this.description = description;
    }


}
