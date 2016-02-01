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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initMPService(ArrayList<Audio> audioList){
        if(MPService.INSTANCE!=null){
            MPService.INSTANCE.setPlayList(new PlayList(audioList));
            PlayerFragment.INSTANCE.setTrackInfo();
        }
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
                           // playBtn.setBackgroundResource(android.R.drawable.ic_media_play);
                            MPService.INSTANCE.pause();
                        } else {
                           // playBtn.setBackgroundResource(android.R.drawable.ic_media_pause);
                            MPService.INSTANCE.playAt(i);
                            PlayerFragment.INSTANCE.setTrackInfo();
                            PlayerFragment.INSTANCE.updateProgressBar();
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
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        return view;
    }
}