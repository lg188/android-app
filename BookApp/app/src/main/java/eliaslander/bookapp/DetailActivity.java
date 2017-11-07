package eliaslander.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        Book book = new Book();

        book.setTitle(intent.getStringExtra("book_title"));
        book.setAuthor(intent.getStringExtra("book_author"));
        book.setImageUrl(intent.getStringExtra("book_imageUrl"));
        // TODO: get description


        TextView title = (TextView) findViewById(R.id.detail_title);
        TextView author = (TextView) findViewById(R.id.detail_author);
        ImageView image = (ImageView) findViewById(R.id.detail_image);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        Picasso.with(getApplicationContext()).load(book.getImageUrl()).into(image);


    }
}
