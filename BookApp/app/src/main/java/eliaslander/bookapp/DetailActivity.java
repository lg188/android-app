package eliaslander.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;

public class DetailActivity extends AppCompatActivity {

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


    }
}
