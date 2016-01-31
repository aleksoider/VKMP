package sav.alex.vkmp.intefaces;

/**
 * Created by AlexSav on 28.01.2016.
 */
public interface IPlayList {
    public String getTrackAt(int id);
    public String nextTrack();
    public String previousTrack();
    public IAudio getCurrentTrack();
    public int getCurrentTrackNum();
}
