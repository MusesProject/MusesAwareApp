package eu.musesproject.musesawareapp.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import eu.musesproject.musesawareapp.R;

public class AssetListAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    
    public AssetListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.asset_row, null);

        TextView name = (TextView)vi.findViewById(R.id.name);
        TextView sub_text = (TextView)vi.findViewById(R.id.sub_text);
//        TextView time = (TextView)vi.findViewById(R.id.time);
        ImageView asset_image=(ImageView)vi.findViewById(R.id.asset_image);
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        // Setting all values in listview
        name.setText(song.get(ShowActivity.KEY_NAME));
        sub_text.setText(song.get(ShowActivity.KEY_SUBTEXT));
//        time.setText(song.get(ShowActivity.KEY_TIME));
        // User normal android way to show image
        asset_image.setImageDrawable(activity.getResources().getDrawable(Integer.parseInt(song.get(ShowActivity.KEY_IMAGE_URL))));;
//        asset_image.setImageResource(activity.getApplicationContext().getResources().
//        							 getIdentifier(song.get(ShowActivity.KEY_IMAGE_URL), null, activity.getPackageName()));
        return vi;
    }
}