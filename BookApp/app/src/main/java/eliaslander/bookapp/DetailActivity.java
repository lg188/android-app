package eliaslander.bookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {
    private int id;
    private SharedPreferences prefs;
    private ArrayList<String> bookmarkBooks;
    private Set<String> bookmarkBooksSet;
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
        try {
            prefs = this.getSharedPreferences("data", MODE_PRIVATE);
            loadBookmarks();
            if (bookmarkBooks != null) {
                for (String s : bookmarkBooks) {
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

    void toggleBookmark(View view) {
        try {
            loadBookmarks();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Clicked fave", Toast.LENGTH_SHORT).show();
        bookmarked = !bookmarked;
        try {
            updateBookmarkIcon();
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "Unable to store in faves", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    void updateBookmarkIcon() {
        //TODO: werkt nog niet
        ImageButton button = findViewById(R.id.detail_bookmark);
        button.setImageResource(bookmarked ? R.drawable.bookmark_full : R.drawable.bookmark_empty);
    }

    @Override
    protected void onDestroy() {
        saveBookmark();
        super.onDestroy();
    }

    void loadBookmarks() {
        bookmarkBooksSet = prefs.getStringSet("bookmarks", null);
        if (bookmarkBooks != null) {
            bookmarkBooks = new ArrayList<>(bookmarkBooksSet);
        } else {
            bookmarkBooks = new ArrayList<>();
        }
    }

    void saveBookmark() {
        if (!bookmarked) {
            // remove bookmark
            for (String s : bookmarkBooks) {
                if (s.equals(this.id + "")) {
                    bookmarkBooks.remove(s);
                }
            }
        } else {
            //add bookmark
            bookmarkBooks.add(this.id + "");
        }


        SharedPreferences.Editor editor = prefs.edit();

        editor.putStringSet("bookmarks", new HashSet<>(bookmarkBooks));
        editor.commit();
    }
}
