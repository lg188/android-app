package eliaslander.bookapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter for creating a grid of books.
 *
 * @author lg188
 */

public class BookGridAdapter extends ArrayAdapter<Book> {

    /**
     * Constructor
     * @param context
     * @param books
     */
    public BookGridAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Book book = getItem(position);
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        title.setText(book.getTitle());
        //description.setText(book.getDescription());
        return view;
    }
}
