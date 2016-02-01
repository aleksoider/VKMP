package sav.alex.vkmp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.media.MediaPlayer;
import android.util.Log;
import java.io.IOException;
import android.os.PowerManager;
import android.media.AudioManager;

import sav.alex.vkmp.classes.PlayList;
import sav.alex.vkmp.intefaces.IAudio;
import sav.alex.vkmp.intefaces.IPlayList;

public class MPService extends Service implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{

    final String LOG_T = "MPService";
    public static MPService INSTANCE;
    MediaPlayer player = new MediaPlayer();
    IPlayList playlist;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        setProps();
        Log.d(LOG_T, "Player created");
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setProps(){
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setPlayList(IPlayList pl){
        this.playlist=pl;
    }

    public IAudio getCurrentTrack(){
        return playlist.getCurrentTrack();
    }

    public int currentTrackNum(){
        return playlist.getCurrentTrackNum();
    }

    public void play() {
        if(player.isPlaying()){
            player.pause();
        } else {
            player.start();
        }
    }

    public void playAt(int id){
        player.reset();
        try{
            if(playlist != null){
                player.setDataSource(this.playlist.getTrackAt(id));
            } else {
                return;
            }
            player.prepare();
            player.start();
        } catch (IOException e){
            Log.e(LOG_T, "Player IOException");
        }
    }

    public void next(){
        player.reset();
        try{
            if(playlist != null){
                player.setDataSource(this.playlist.nextTrack());
            } else {
                return;
            }
            player.prepare();
            player.start();
        } catch (IOException e){
            Log.e(LOG_T, "Player IOException");
        }
    }

    public void prev(){
        player.reset();
        try{
            if(playlist != null){
                player.setDataSource(this.playlist.previousTrack());
            } else {
                return;
            }
            player.prepare();
            player.start();
        } catch (IOException e){
            Log.e(LOG_T, "Player IOException");
        }
    }

    public void pause(){
        if(player.isPlaying()){
            player.pause();
        }
    }

    public void stop(){
        player.stop();
    }

    public int getDuration(){
        try{
            return player.getDuration();
        } catch (IllegalStateException e){
            return 0;
        }
    }

    public int getCurrentPosition(){
        try{
            return player.getCurrentPosition();
        } catch (IllegalStateException e){
            return 0;
        }
    }

    public void seekTo(int position){
        if(player!= null ){
            player.seekTo(position);
        }
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(playlist != null){
            mediaPlayer.reset();
            next();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    public void onDestroy() {
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();
        Log.d(LOG_T, "Player destroyed");
    }
}
