package com.rs.skills.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rs.skills.R;
import com.rs.skills.type.Skill;

/**
 * Created by waeljammal on 12/09/2014.
 */
public class SkillAdapter extends ArrayAdapter<Skill> {
    private final Context context;
    private final Skill[] values;

    public SkillAdapter(Context context, Skill[] values) {
        super(context, R.layout.activity_main_list_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_main_list_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.skillName);
        textView.setText(values[position].getTitle());

        return rowView;
    }
}
