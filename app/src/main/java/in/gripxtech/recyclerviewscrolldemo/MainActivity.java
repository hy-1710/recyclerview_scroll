package in.gripxtech.recyclerviewscrolldemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import in.gripxtech.recyclerviewscrolldemo.databinding.ActivityMainBinding;
import in.gripxtech.recyclerviewscrolldemo.model.ListItem;
import in.gripxtech.recyclerviewscrolldemo.prefs.Prefs;

public class MainActivity extends AppCompatActivity {

    public static final String TAG;

    static {
        TAG = MainActivity.class.getSimpleName();
    }

    private ActivityMainBinding binding;
    private ListAdapter adapter;
    private Loader<ArrayList<ListItem>> loader;
    private Prefs prefs;
    private int scrollX, scrollY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scrollX = 0;
        scrollY = 0;
        adapter = new ListAdapter(this, new ArrayList<ListItem>());
        prefs = new Prefs(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollX += dx;
                scrollY += dy;
            }
        });
        binding.rv.setAdapter(adapter);

        loader = getSupportLoaderManager().initLoader(
                0, savedInstanceState, new LoaderManager.LoaderCallbacks<ArrayList<ListItem>>() {
                    @Override
                    public Loader<ArrayList<ListItem>> onCreateLoader(int id, Bundle args) {
                        Log.i(TAG, "onCreateLoader: ");
                        return new AsyncTaskLoader<ArrayList<ListItem>>(MainActivity.this) {
                            @Override
                            public ArrayList<ListItem> loadInBackground() {
                                return new ArrayList<ListItem>() {{
                                    for (int i = 0; i < 15; i++) {
                                        add(new ListItem(i));
                                    }
                                }};
                            }
                        };
                    }

                    @Override
                    public void onLoadFinished(Loader<ArrayList<ListItem>> loader, ArrayList<ListItem> data) {
                        Log.i(TAG, "onLoadFinished: ");
                        scrollX = 0;
                        scrollY = 0;
                        adapter = new ListAdapter(MainActivity.this, data);
                        binding.rv.setAdapter(adapter);
                        if (prefs.getScrollY() != -1) {
                            binding.rv.smoothScrollBy(prefs.getScrollX(), prefs.getScrollY());
                            prefs.setScrollX(-1);
                            prefs.setScrollY(-1);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<ArrayList<ListItem>> loader) {
                        Log.i(TAG, "onLoaderReset: ");
                    }
                }
        );
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loader.forceLoad();
    }

    public Loader<ArrayList<ListItem>> getLoader() {
        return loader;
    }

    public int getScrollX() {
        return scrollX;
    }

    public int getScrollY() {
        return scrollY;
    }
}
