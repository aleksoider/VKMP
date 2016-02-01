package sav.alex.vkmp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import sav.alex.vkmp.R;
import sav.alex.vkmp.classes.Utilites;
import sav.alex.vkmp.intefaces.IAudio;
import sav.alex.vkmp.services.MPService;


public class PlayerFragment extends Fragment {

    View view;

    private Handler mHandler = new Handler();
    public static PlayerFragment INSTANCE;
    public static Button playBtn;
    Button nextBtn;
    Button prevBtn;
    Button dwnBtn;

    TextView songDurationLabel;
    TextView artist;
    TextView trackName;
    TextView trackNum;
    SeekBar songProgressBar;

    public PlayerFragment() {
        // Required empty public constructor
    }

    public void setTrackInfo(){
        if(MPService.INSTANCE!=null) {
            IAudio audio = MPService.INSTANCE.getCurrentTrack();
            artist.setText(audio.getArtist());
            trackName.setText(audio.getTitle());
            trackNum.setText((MPService.INSTANCE.currentTrackNum() + 1) + "/" + MPService.INSTANCE.getTrackCount());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_player, container, false);
        INSTANCE=this;
        playBtn = (Button) view.findViewById(R.id.playBtn);
        nextBtn = (Button) view.findViewById(R.id.nextBtn);
        prevBtn = (Button) view.findViewById(R.id.prevBtn);
        //dwnBtn = (Button) view.findViewById(R.id.dwnBtn);
        songDurationLabel = (TextView) view.findViewById(R.id.duration);
        artist = (TextView) view.findViewById(R.id.author);
        trackName = (TextView) view.findViewById(R.id.song);
        trackNum = (TextView) view.findViewById(R.id.songCount);
        songProgressBar = (SeekBar) view.findViewById(R.id.seekBar);
        songProgressBar.setProgressDrawable(getResources()
                .getDrawable(R.drawable.progress_bar));
        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
            }

            /**
             * When user starts moving the progress handler
             * */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // remove message Handler from updating progress bar
                mHandler.removeCallbacks(mUpdateUITask);
            }

            /**
             * When user stops moving the progress hanlder
             * */

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateUITask);
                if (MPService.INSTANCE != null) {
                    int totalDuration = MPService.INSTANCE.getDuration();
                    int currentPosition = Utilites.progressToTimer(seekBar.getProgress(), totalDuration);

                    // forward or backward to certain seconds
                    MPService.INSTANCE.seekTo(currentPosition);

                    // update timer progress again
                    updateProgressBar();
                }
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MPService.INSTANCE != null) {
                    if (MPService.INSTANCE.isPlaying()) {
                        playBtn.setBackgroundResource(android.R.drawable.ic_media_play);
                        MPService.INSTANCE.pause();
                    } else {
                        playBtn.setBackgroundResource(android.R.drawable.ic_media_pause);
                        //MPService.INSTANCE.playAt(MPService.INSTANCE.currentTrackNum());
                        MPService.INSTANCE.play();
                        setTrackInfo();
                        updateProgressBar();
                    }
                    ;
                }
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(MPService.INSTANCE != null){
                    MPService.INSTANCE.prev();
                    setTrackInfo();
                    updateProgressBar();
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(MPService.INSTANCE != null){
                    MPService.INSTANCE.next();
                    setTrackInfo();
                    updateProgressBar();
                }
            }
        });
        updateProgressBar();

        return view;
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateUITask, 100);
        setTrackInfo();
    }

    private Runnable mUpdateUITask = new Runnable() {
        public void run() {
            if(MPService.INSTANCE != null){
                setTrackInfo();
                long totalDuration = MPService.INSTANCE.getDuration();
                long currentDuration = MPService.INSTANCE.getCurrentPosition();
                songDurationLabel.setText(""+Utilites.milliSecondsToTimer(currentDuration));
                // Updating progress bar
                int progress = (int)(Utilites.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                songProgressBar.setProgress(progress);
                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            }
        }
    };

}
