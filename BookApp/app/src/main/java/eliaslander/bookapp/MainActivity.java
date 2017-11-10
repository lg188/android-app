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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "MainActivity";
    private ListView listView;
    private GridView gridView;
    private ArrayList<Book> books;
    private viewType viewMode = viewType.GRID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TODO add search bar
        // populate book list
        books = new ArrayList<>();
        switchToLibrary();


        // item onClick (for both grid and list)
        AdapterView.OnItemClickListener clickListener = (AdapterView<?> parent, View view, int position, long id) -> {
            Book book = books.get(position);
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
        //sharedPreferences.getStringSet()
        switchView(list ? viewType.LIST : viewType.GRID);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_library:
                switchToLibrary();
                break;
            case R.id.nav_search:
                try {
                    switchToSearch();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                break;
            default:
                switchToLibrary();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchToSearch() throws IOException, SAXException {
        books = Controller.SearchBooks("harry");
        BookListAdapter listAdapter = new BookListAdapter(this, books);
        BookGridAdapter gridAdapter = new BookGridAdapter(this, books);
        listView = (ListView) findViewById(R.id.list_view);
        gridView = (GridView) findViewById(R.id.grid_view);
        listView.setAdapter(listAdapter);
        gridView.setAdapter(gridAdapter);

    }

    public void switchToLibrary(){
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        Set<String> bookIds = sharedPreferences.getStringSet("bookmarkBooks",null);
        books = new ArrayList<>();
        if(bookIds != null){
            for (String str : bookIds) {
                try {
                    books.add(Controller.GetBookById(Integer.parseInt(str)));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        BookListAdapter listAdapter = new BookListAdapter(this, books);
        BookGridAdapter gridAdapter = new BookGridAdapter(this, books);
        listView = (ListView) findViewById(R.id.list_view);
        gridView = (GridView) findViewById(R.id.grid_view);
        listView.setAdapter(listAdapter);
        gridView.setAdapter(gridAdapter);

    }


    /**
     * reset view to {@link #viewMode}
     */
    private void resetView() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
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
        Toast.makeText(getApplicationContext(), "Switching view", Toast.LENGTH_SHORT).show();
        this.switchView(this.viewMode.toggle());
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
