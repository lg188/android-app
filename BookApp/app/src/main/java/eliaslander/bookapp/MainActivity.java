package eliaslander.bookapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "MainActivity";
    private ListView listView;
    private GridView gridView;

    private viewType viewMode = viewType.GRID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // populate book list
        ArrayList<Book> books = new ArrayList<>();
        for (int i = 0; i <= 50; i++) {
            books.add(new Book("Harry potter " + i, "J. K. Rowling"));
        }

        // assign adapters to views
        BookListAdapter listAdapter = new BookListAdapter(this, books);
        BookGridAdapter gridAdapter = new BookGridAdapter(this, books);
        listView = (ListView) findViewById(R.id.list_view);
        gridView = (GridView) findViewById(R.id.grid_view);
        listView.setAdapter(listAdapter);
        gridView.setAdapter(gridAdapter);

        // item onClick (for both grid and list)
        AdapterView.OnItemClickListener clickListener = (parent, view, position, id) -> {
            Toast.makeText(getApplicationContext(), "Item clicked", Toast.LENGTH_SHORT).show();
        };
        listView.setOnItemClickListener(clickListener);
        gridView.setOnItemClickListener(clickListener);
        resetView();

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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * reset view to {@link #viewMode}
     */
    private void resetView() {
        String dbg = "";
        switch (viewMode) {
            case GRID:
                gridView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                dbg += "grid";
                break;
            case LIST:
                gridView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                dbg += "list";
                break;
            default:
                gridView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);
                dbg += "error";
        }
        Log.d(TAG, dbg);
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
