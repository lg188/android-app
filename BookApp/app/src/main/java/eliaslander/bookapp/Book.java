package eliaslander.bookapp;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * This class represents a book
 *
 * @author lg188
 */

class Book {
    private int id;
    private String title = "Undefined Title";
    private String author = "Undefined Author";
    private String imageUrl = "";
    private String description = "";
    private List<Book> similarBooks;
    private float rating;


    /**
     * Create a book (no cover)
     *
     * @param title  Book title
     * @param author Book author
     */
    Book(int id, String title, String author) {
        setId(id);
        setTitle(title);
        setAuthor(author);
    }

    public Book() {
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


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Book> getSimilarBooks() {
        return similarBooks;
    }

    public void setSimilarBooks(List<Book> similarBooks) {
        this.similarBooks = similarBooks;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
