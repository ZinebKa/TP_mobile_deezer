package com.example.androiddeezer2020;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.androiddeezer2020.adapter.TrackAdapter;
import com.example.androiddeezer2020.service.DeezerService;
import com.example.androiddeezer2020.service.data.DataTrack;
import com.example.androiddeezer2020.service.data.Track;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListTrackActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public static final MediaPlayer DEZEER_MEDIA_PLAYER = MediaPlayer.getInstance();
    private static final String TAG = "ListTrackActivity";
    List<Track> tracks;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;
    private SeekBar songProgressBar;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_track);
        Toolbar toolbar = findViewById(R.id.toolbar_track);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progress_circular_track);
        hideProgress();



        recyclerView = (RecyclerView) findViewById(R.id.track_recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final String track = getIntent().getStringExtra("album");
        final String albumName = getIntent().getStringExtra("albumName");
        listTrackFill(track);
        toolbar.setTitle(albumName);
    }

    private void listTrackFill(String query) {
        Snackbar.make(findViewById(android.R.id.content),
                "Search " + query,
                Snackbar.LENGTH_SHORT).show();
        showProgress();

        Response.Listener<DataTrack> rep = new Response.Listener<DataTrack>() {
            @Override
            public void onResponse(DataTrack response) {
                Log.d(TAG, "searchTrack Found " + response.getNbTracks() + " track");
                tracks = response.getTracks().getData();
                DEZEER_MEDIA_PLAYER.setTracks(tracks);
                TrackAdapter mAdapter = new TrackAdapter(tracks);
                recyclerView.setAdapter(mAdapter);
                hideProgress();
            }
        };
        final Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "searchTrack onErrorResponse: " + error.getMessage());
                hideProgress();
            }
        };

        DeezerService.searchTrack(ListTrackActivity.this, query, rep, error);
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = DEZEER_MEDIA_PLAYER.mediaPlayer.getDuration();
            long currentDuration = DEZEER_MEDIA_PLAYER.mediaPlayer.getCurrentPosition();

            long percentProgress;
            try {
                percentProgress =currentDuration*100/ totalDuration ;
            }catch(ArithmeticException r){
                percentProgress =0;
            }
            // Updating progress bar
            int progress = (int)(percentProgress);
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            mHandler.postDelayed(this, 100);
            // Running this thread after 100 milliseconds

        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
