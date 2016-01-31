package sav.alex.vkmp.classes;

import org.json.JSONObject;

import sav.alex.vkmp.intefaces.IAudio;

/**
 * Created by 123 on 31.01.2016.
 */
public class Audio implements IAudio{

    String artist;
    String title;
    int duration;
    String url;

    public Audio(JSONObject audioItem){
        try {
            artist = audioItem.getString("artist");
            title = audioItem.getString("title");
            duration = Integer.parseInt(audioItem.getString("duration"));
            url = audioItem.getString("url");
        } catch (Exception e) {

        }
    }
    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }
    public int getDuration(){
        return duration;
    }
    public String getURL(){
        return url;
    }
}
