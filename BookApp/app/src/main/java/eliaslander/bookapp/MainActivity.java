package eliaslander.bookapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @SuppressWarnings("FieldCanBeLocal")
    private ListView listView;
    private GridView gridView;
    private SearchView searchView;
    private BookGridAdapter gridAdapter;
    private BookListAdapter listAdapter;
    private ArrayList<Book> books_shown, books_search, books_bookmarked;
    private viewType viewMode = viewType.GRID;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Menu navigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = findViewById(R.id.list_view);
        gridView = findViewById(R.id.grid_view);
        searchView = findViewById(R.id.search);
        searchView.setLayoutParams(new Toolbar.LayoutParams(Gravity.RIGHT));
        navigationMenu = navigationView.getMenu();

        loadBookmarks();
        books_shown = books_bookmarked;
        reloadAdapters();
        prefs = getSharedPreferences("data", MODE_PRIVATE);
        editor = prefs.edit();
        if (prefs.getBoolean("searchEnabled", false)) {
            switchToSearch(prefs.getString("searchQuery", ""));
        } else {
            switchToLibrary();
        }

        // item onClick (for both grid and list)
        AdapterView.OnItemClickListener clickListener = (AdapterView<?> parent, View view, int position, long id) -> {
            Book book = books_shown.get(position);
            Toast.makeText(getApplicationContext(), book.getId() + " clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("book_id", book.getId()+"");
            intent.putExtra("book_title", book.getTitle());
            intent.putExtra("book_author", book.getAuthor());
            intent.putExtra("book_imageUrl", book.getImageUrl());
            // TODO: Remove this when it's safe
            try {
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        listView.setOnItemClickListener(clickListener);
        gridView.setOnItemClickListener(clickListener);

        boolean list  = sharedPreferences.getBoolean("listView",false);
        switchView(list ? viewType.LIST : viewType.GRID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                editor.putString("searchQuery", query);
                editor.commit();
                try {
                    books_search = Controller.SearchBooks(query);
                    books_shown = books_search;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                reloadAdapters();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_library:
                switchToLibrary();
                break;
            case R.id.nav_search:
                switchToSearch();
                break;
            default:
                switchToLibrary();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchToSearch() {
        switchToSearch("");
    }

    public void switchToSearch(String searchQuery) {
        editor.putBoolean("searchEnabled", true);
        editor.commit();
        navigationMenu.getItem(1).setChecked(true); //TODO: hardcoded
        searchView.setVisibility(View.VISIBLE);
        if (books_search == null) {
            books_search = new ArrayList<>();
        }
        if (!searchQuery.isEmpty()) {
            searchView.setIconified(false);
            searchView.setQuery(searchQuery, true);
        } else {
            editor.putString("searchQuery", "");
        }
        books_shown = books_search;
        reloadAdapters();
    }


    private void loadBookmarks() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        Set<String> bookIds = sharedPreferences.getStringSet("bookmarks", null);

        findViewById(R.id.search).setVisibility(View.INVISIBLE);

        books_bookmarked = new ArrayList<>();
        if(bookIds != null){
            for (String str : bookIds) {
                try {
                    books_bookmarked.add(Controller.GetBookById(Integer.parseInt(str)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.v("library", "library is empty");
        }
    }


    public void switchToLibrary() {
        navigationMenu.getItem(0).setChecked(true); //TODO: hardcoded
        editor.putBoolean("searchEnabled", false);
        editor.commit();
        loadBookmarks();
        books_shown = books_bookmarked;
        reloadAdapters();
    }

    /**
     * reset view to {@link #viewMode}
     */
    private void resetView() {
        SharedPreferences sharedPref = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (viewMode) {
            case GRID:
                gridView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                editor.putBoolean("listView",false) ;
                break;
            case LIST:
                editor.putBoolean("listView",true) ;
                gridView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                break;
            default:
                editor.putBoolean("listView",false) ;
                gridView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);
        }
        editor.commit();
    }

    /**
     * Set the preferred {@link #viewMode} and update the main activity.
     *
     * @param mode the preferred viewMode
     */
    private void switchView(viewType mode) {
        this.viewMode = mode;
        this.resetView();
    }

    public void switchView(MenuItem item) {
        this.switchView(this.viewMode.toggle());
    }

    private void reloadAdapters() {
        try {
            listAdapter = new BookListAdapter(this, books_shown);
            gridAdapter = new BookGridAdapter(this, books_shown);
            listView.setAdapter(listAdapter);
            gridView.setAdapter(gridAdapter);
        } catch (NullPointerException e) {
            Log.e("reloadAdapters", "books_shown is null");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum viewType {
        GRID, LIST;
        viewType toggle() {
            if (this.equals(GRID))
                return LIST;
            else
                return GRID;
        }
    }
}
