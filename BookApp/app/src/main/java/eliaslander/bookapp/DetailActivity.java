package eliaslander.bookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {
    private int id;
    private SharedPreferences prefs;
    private ArrayList<String> bookmarkedBooks;
    private Set<String> bookmarkedBookSet;
    private boolean bookmarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        Book book = null;
        try {
            book = Controller.GetBookById(Integer.parseInt(intent.getStringExtra("book_id")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        TextView title = findViewById(R.id.detail_title);
        TextView author = findViewById(R.id.detail_author);
        ImageView image = findViewById(R.id.detail_image);
        TextView rating = findViewById(R.id.detail_rating);
        TextView description = findViewById(R.id.detail_description);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        rating.setText(book.getRating() + "");
        description.setText(book.getDescription());
        Picasso.with(getApplicationContext()).load(book.getImageUrl()).into(image);

        // id en fave stuff
        this.id = book.getId();
        getBookMarked();
    }

    void toggleBookmark(View view) {
        try {
            loadBookmarks();
            bookmarked = !bookmarked;
            updateBookmarkIcon();
            saveBookmark();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateBookmarkIcon() {
        //TODO: werkt nog niet
        ImageButton button = findViewById(R.id.detail_bookmark);
        button.setImageResource(bookmarked ? R.drawable.bookmark_full : R.drawable.bookmark_empty);
    }

    void loadBookmarks() {
        try {
            prefs = getSharedPreferences("data", MODE_PRIVATE);
            bookmarkedBookSet = prefs.getStringSet("bookmarks", null);
            bookmarkedBooks = new ArrayList<>();
            for (String s : bookmarkedBookSet) {
                bookmarkedBooks.add(s);
                if (s.equals(this.id))
                    bookmarked = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void saveBookmark() {
        if (!bookmarked) {
            for (String s : bookmarkedBooks) {
                if (s.equals(this.id + "")) {
                    bookmarkedBooks.remove(s);
                }
            }
        } else {
            bookmarkedBooks.add(this.id + "");
        }


        SharedPreferences.Editor editor = prefs.edit();
        HashSet<String> temp = new HashSet(bookmarkedBooks);

        editor.putStringSet("bookmarks", temp);
        editor.commit();
    }

    void getBookMarked() {
        loadBookmarks();
        try {
            if (bookmarkedBooks != null) {
                for (String s : bookmarkedBooks) {
                    if (s.equals(this.id + "")) {
                        bookmarked = true;
                        updateBookmarkIcon();
                    } else {
                        bookmarked = false;
                    }
                }
            } else {
                bookmarked = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Unable to store in faves", Toast.LENGTH_SHORT).show();

        }

    }
}
