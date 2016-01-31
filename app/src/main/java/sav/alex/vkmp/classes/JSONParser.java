package sav.alex.vkmp.classes;

import android.widget.TextView;

import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import sav.alex.vkmp.R;

/**
 * Created by 123 on 31.01.2016.
 */
public class JSONParser {
    public static ArrayList<Audio> parseAudioResponse(JSONObject audio){
        ArrayList<Audio> buf = new ArrayList<>();
        try {
            JSONObject objResponse = audio.getJSONObject("response");
            JSONArray items = objResponse.getJSONArray("items");
            for(int i=0;i<items.length();i++)
                buf.add(new Audio(items.getJSONObject(i)));
        } catch (Exception e) {

        }
        return buf;
    }
}
