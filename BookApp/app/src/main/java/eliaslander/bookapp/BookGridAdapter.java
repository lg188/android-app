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

class BookGridAdapter extends ArrayAdapter<Book> {

    /**
     * @param books A list of books
     */
    public BookGridAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        Book book = getItem(position);
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        TextView title = view.findViewById(R.id.title);
        title.setText(book != null ? book.getTitle() : null);
        return view;
    }
}
