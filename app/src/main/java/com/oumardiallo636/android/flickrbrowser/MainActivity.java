package com.oumardiallo636.android.flickrbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements GetFlickrJsonData.OnDataAvailable,
                                                    RecyclerItemClickListener.OnRecyclerClickListener{

    private static final String TAG = "MainActivity";
    private FlickerRecyclerViewAdapter mFlickerRecyclerViewAdapter;

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //activateToolbar is defined in BaseActivity
        activateToolbar(false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFlickerRecyclerViewAdapter = new FlickerRecyclerViewAdapter(this, new ArrayList<Photo>());
        recyclerView.setAdapter(mFlickerRecyclerViewAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,recyclerView,this));

//        GetRawData getRawData = new GetRawData(this);
//        getRawData.execute("https://api.flickr.com/services/feeds/photos_public.gne?tags=android,O&tagmode=any&format=json&nojsoncallback=1");

        Log.d(TAG, "onCreate: Ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String researchQuery = sharedPreferences.getString(FLICKR_QUERY,"");

        if (researchQuery.length() > 0){
            GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData(
                    this,
                    "https://api.flickr.com/services/feeds/photos_public.gne",
                    "en-us",
                    true);
            getFlickrJsonData.execute(researchQuery);
        }
        //        getFlickrJsonData.executeSameThread("android, nougat");
        Log.d(TAG, "onResume: Ends");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_search, menu);
//
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
//        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//        return true;
//    }

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

        if (id == R.id.action_search){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
        Log.d(TAG, "onOptionsItemSelected() returned: returned" );
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if(status == DownloadStatus.OK){
            mFlickerRecyclerViewAdapter.loadNewData(data);
        }else {
            Log.d(TAG, "onDownloadComplete: status = "+status);    
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: stars");
//        Toast.makeText(getApplicationContext(),"normal tap at position "+ position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, mFlickerRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
        //Toast.makeText(MainActivity.this, "long tap at position "+position, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, mFlickerRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }
}
