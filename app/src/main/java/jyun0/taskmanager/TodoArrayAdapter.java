package jyun0.taskmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jyun0.taskmanager.R;

import java.util.ArrayList;

public class TodoArrayAdapter extends ArrayAdapter<Item> {
    private Context mContext;
    private int mResource;

    public TodoArrayAdapter(Context context, int resource,ArrayList<Item> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        String title = getItem(position).getTitle();
        String date = getItem(position).getDate();

        Item item = new Item(date, title);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView contents = convertView.findViewById(R.id.contents);
        TextView Date = convertView.findViewById(R.id.date);
        TextView time = convertView.findViewById(R.id.time);

        contents.setText(title);
        Date.setText(date);

        return convertView;
    }
}
