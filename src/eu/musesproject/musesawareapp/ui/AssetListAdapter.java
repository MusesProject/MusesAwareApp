package eu.musesproject.musesawareapp.ui;

/*
 * #%L
 * MUSES Client
 * %%
 * Copyright (C) 2013 - 2014 Sweden Connectivity
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
        ImageView asset_image=(ImageView)vi.findViewById(R.id.asset_image);
        
        HashMap<String, String> item = new HashMap<String, String>();
        item = data.get(position);
        
        // Setting all values in listview
        name.setText(item.get(MainActivity.KEY_NAME));
        sub_text.setText(item.get(MainActivity.KEY_SUBTEXT));
        // User normal android way to show image
        asset_image.setImageDrawable(activity.getResources().getDrawable(Integer.parseInt(item.get(MainActivity.KEY_IMAGE_URL))));;
        return vi;
    }
}