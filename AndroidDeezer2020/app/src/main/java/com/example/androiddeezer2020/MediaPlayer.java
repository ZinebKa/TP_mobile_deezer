package com.example.androiddeezer2020;

import com.example.androiddeezer2020.service.data.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaPlayer {

    static public String PLAY = "play";
    static public String STOP = "stop";


    static private MediaPlayer INSTANCE = new MediaPlayer();
    public android.media.MediaPlayer mediaPlayer;
    private List<Track> tracks;

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    private String trackName;

    private MediaPlayer() {
        mediaPlayer = new android.media.MediaPlayer();
        tracks = new ArrayList<>();
    }

    static public MediaPlayer getInstance() {
        return INSTANCE;
    }

    void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public void playTrack(int index) throws IOException {
        this.stopTrack();
        mediaPlayer.setDataSource(tracks.get(index).getPreview());
        mediaPlayer.prepare();
        mediaPlayer.start();
        setTrackName(tracks.get(index).getTitle());
    }

    public void stopTrack() {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }




}
