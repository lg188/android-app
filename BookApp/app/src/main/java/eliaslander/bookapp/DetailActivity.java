package eliaslander.bookapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {
    private int id;
    private SharedPreferences prefs;
    private Set<String> bookmarkBooks;
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


        TextView title = (TextView) findViewById(R.id.detail_title);
        TextView author = (TextView) findViewById(R.id.detail_author);
        ImageView image = (ImageView) findViewById(R.id.detail_image);
        TextView rating = (TextView) findViewById(R.id.detail_rating);
        TextView description = (TextView) findViewById(R.id.detail_description);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        rating.setText(book.getRating()+"");
        description.setText(book.getDescription());
        Picasso.with(getApplicationContext()).load(book.getImageUrl()).into(image);

        // id en fave stuff
        this.id = book.getId();
        try {
            prefs = this.getPreferences(Context.MODE_PRIVATE);
            bookmarkBooks = prefs.getStringSet("bookmarkBooks", null);
            if (bookmarkBooks == null) {
                bookmarkBooks = new HashSet<>(Arrays.asList(""));
            }
            for(String s :  bookmarkBooks){
                String msg;
                if (s.equals(this.id) ){
                    bookmarked = true;
                    updateBookmarkIcon();
                }else{
                    bookmarked = false;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Unable to store in faves", Toast.LENGTH_SHORT).show();

        }
    }
    void toggleFave(View view){
        Toast.makeText(getApplicationContext(),"Clicked fave", Toast.LENGTH_SHORT).show();
        bookmarked = !bookmarked;
        try {
            saveBookmark();
            updateBookmarkIcon();
        }catch(Exception e) {
            //Toast.makeText(getApplicationContext(), "Unable to store in faves", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    void updateBookmarkIcon(){
        ImageButton button = (ImageButton) findViewById(R.id.detail_bookmark) ;
        button.setImageResource( bookmarked ? R.drawable.bookmark_full : R.drawable.bookmark_empty);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveBookmark();
    }

    void saveBookmark(){
        if(bookmarked){
            // remove bookmark
            for(String s : bookmarkBooks){
                if(s.equals(this.id+"")){
                    bookmarkBooks.remove(s);
                }
            }
        }else{
            //add bookmark
            bookmarkBooks.add(this.id+"");
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("bookmarkBooks", bookmarkBooks);
        editor.commit();

    }
    void printBookmarks(){
        Log.v("bookmarked-books", "----<");
        for(String s : bookmarkBooks){
            Log.v("bookmarked-books", s+"");
        }
        Log.v("bookmarked-books", "---->");
    }
}
