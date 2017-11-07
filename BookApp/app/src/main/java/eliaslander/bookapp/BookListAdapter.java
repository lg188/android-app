package eliaslander.bookapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter for creating a list of books
 *
 * @author lg188
 */

class BookListAdapter extends ArrayAdapter<Book> {

    /**
     * @param books A list of books
     */
    public BookListAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        Book book = getItem(position);
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        TextView title = view.findViewById(R.id.title);
        TextView author = view.findViewById(R.id.author);
        ImageView image = view.findViewById(R.id.image);
        title.setText(book != null ? book.getTitle() : null);
        author.setText(book != null ? book.getAuthor() : null);
        Picasso.with(getContext()).load(book.getImageUrl()).into(image);
        return view;
    }
}
