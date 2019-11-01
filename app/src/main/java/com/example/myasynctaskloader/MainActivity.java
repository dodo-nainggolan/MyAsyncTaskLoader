package com.example.myasynctaskloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<WeatherItems>>{

    RecyclerView recyclerView;
    WeatherAdapter adapter;
    EditText edtCity;
    Button btnSearch;

    static final String EXTRAS_CITY = "EXTRAS_CITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new WeatherAdapter();
        adapter.notifyDataSetChanged();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        edtCity = findViewById(R.id.editCity);
        btnSearch = findViewById(R.id.btnCity);

        btnSearch.setOnClickListener(myListener);
        String city = edtCity.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_CITY, city);

        getLoaderManager().initLoader(0, bundle, this);
    }

    @Override
    public Loader<ArrayList<WeatherItems>> onCreateLoader(int id, Bundle args) {
        String cities = "";
        if (args != null) {
            cities = args.getString(EXTRAS_CITY);
        }
        return new MyAsyncTaskLoader(this, cities);
    }

    @Override
    public void onLoadFinished(android.content.Loader<ArrayList<WeatherItems>> loader, ArrayList<WeatherItems> data) {
        adapter.setData(data);
        Log.e("TEST adapter count", "onLoadFinished: " + adapter.getItemCount() );
        Log.e("TEST item size", "onLoadFinished: " + data.size() );
    }

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<WeatherItems>> loader) {
        adapter.setData(null);
    }


    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String city = edtCity.getText().toString();
            Log.e("test ", city );

            if (TextUtils.isEmpty(city)) return;
            Bundle bundle = new Bundle();
            bundle.putString(EXTRAS_CITY, city);

            getLoaderManager().restartLoader(0, bundle, MainActivity.this);
        }
    };
}
