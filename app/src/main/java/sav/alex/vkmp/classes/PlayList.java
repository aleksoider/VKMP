package sav.alex.vkmp.classes;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import sav.alex.vkmp.intefaces.IAudio;
import sav.alex.vkmp.intefaces.IPlayList;

/**
 * Created by 123 on 30.01.2016.
 */
public class PlayList implements IPlayList {

    final String LOG_T = "PlayList";
    ArrayList<Audio> audioList=null;

    int currentTrack=0;

    public PlayList(ArrayList<Audio> list){
        if(list!=null) {
            Log.d(LOG_T, "PlayList created");
            audioList = list;
        }
        else{
            Log.d(LOG_T, "PlayList not created(error null list)");
        }
    }

    public PlayList(Audio [] array){
        if(array!=null) {
            Log.d(LOG_T, "PlayList created");
            audioList = new ArrayList<>(Arrays.asList(array));
        }
        else{
            Log.d(LOG_T, "PlayList not created(error null array)");
        }
    }
    @Override
    public IAudio getCurrentTrack(){
        return audioList.get(currentTrack);
    }

    @Override
    public int getCurrentTrackNum() {
        return currentTrack;
    }

    @Override
    public String getTrackAt(int id) {
        currentTrack=id;
        return audioList.get(id).getURL();
    }

    @Override
    public String nextTrack() {
        if(currentTrack<audioList.size()-1) {
            currentTrack+=1;
            return audioList.get(currentTrack).getURL();
        }
        else{
            currentTrack=0;
            return audioList.get(currentTrack).getURL();
        }
    }

    @Override
    public String previousTrack() {
        if(currentTrack>0) {
            currentTrack -= 1;
            return audioList.get(currentTrack).getURL();
        }
        else {
            currentTrack=audioList.size()-1;
            return audioList.get(currentTrack).getURL();
        }
    }
}
