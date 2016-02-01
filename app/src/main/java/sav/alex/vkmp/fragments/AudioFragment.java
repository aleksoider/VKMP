package sav.alex.vkmp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;

import sav.alex.vkmp.R;
import sav.alex.vkmp.activities.MainActivity;
import sav.alex.vkmp.adapters.PlayListAdapter;
import sav.alex.vkmp.classes.Audio;
import sav.alex.vkmp.classes.JSONParser;
import sav.alex.vkmp.classes.PlayList;
import sav.alex.vkmp.classes.Utilites;
import sav.alex.vkmp.intefaces.IAudio;
import sav.alex.vkmp.services.MPService;

public class AudioFragment extends Fragment {
    final String LOG_T = "AudioFragment";
    PlayListAdapter adapter;
    View view;

    private Handler mHandler = new Handler();

    public static Button playBtn;
    Button nextBtn;
    Button prevBtn;
    Button dwnBtn;

    TextView songDurationLabel;
    TextView artist;
    TextView trackName;
    TextView trackNum;
    SeekBar songProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initMPService(ArrayList<Audio> audioList){
        if(MPService.INSTANCE!=null){
            MPService.INSTANCE.setPlayList(new PlayList(audioList));
            IAudio audio = MPService.INSTANCE.getCurrentTrack();
            artist.setText(audio.getArtist());
            trackName.setText(audio.getTitle());
            trackNum.setText("1/" + adapter.getCount());
        }
    }
    private void setTrackInfo(){
        IAudio audio = MPService.INSTANCE.getCurrentTrack();
        artist.setText(audio.getArtist());
        trackName.setText(audio.getTitle());
        trackNum.setText((MPService.INSTANCE.currentTrackNum()+1)+"/"+adapter.getCount());
    }
    public void getAudioList(){
        MainActivity activity = (MainActivity) getActivity();
        ArrayList<Audio> audioList= activity.getAudioList();
        if(audioList!=null) {
            adapter = new PlayListAdapter(getContext(), audioList);
            ListView listView = (ListView) getView().findViewById(R.id.listView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    if(MPService.INSTANCE != null) {
                        if (MPService.INSTANCE.isPlaying() &&
                                i == MPService.INSTANCE.currentTrackNum()) {
                            playBtn.setBackgroundResource(android.R.drawable.ic_media_play);
                            MPService.INSTANCE.pause();
                        } else {
                            playBtn.setBackgroundResource(android.R.drawable.ic_media_pause);
                            MPService.INSTANCE.playAt(i);
                            setTrackInfo();
                            updateProgressBar();
                        }
                    }
                }
            });
            initMPService(audioList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_audio, container, false);
        playBtn = (Button) view.findViewById(R.id.playBtn);
        nextBtn = (Button) view.findViewById(R.id.nextBtn);
        prevBtn = (Button) view.findViewById(R.id.prevBtn);
        //dwnBtn = (Button) view.findViewById(R.id.dwnBtn);
        getAudioList();
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
                if(MPService.INSTANCE != null){
                	if(MPService.INSTANCE.isPlaying()){
                        playBtn.setBackgroundResource(android.R.drawable.ic_media_play);
                        MPService.INSTANCE.pause();
                	}else{
                        playBtn.setBackgroundResource(android.R.drawable.ic_media_pause);
                        //MPService.INSTANCE.playAt(MPService.INSTANCE.currentTrackNum());
                        MPService.INSTANCE.play();
                        setTrackInfo();
                        updateProgressBar();
                	};
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

        //updateProgressBar();

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