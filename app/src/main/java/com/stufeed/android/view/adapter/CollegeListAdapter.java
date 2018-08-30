package com.stufeed.android.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.stufeed.android.R;
import com.stufeed.android.api.response.GetAllCollegeResponse;

import java.util.ArrayList;

public class CollegeListAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ListFilter listFilter = new ListFilter();
    private ArrayList<GetAllCollegeResponse.College> colleges;
    private ArrayList<GetAllCollegeResponse.College> dataListAllItems;

    public CollegeListAdapter(Context context, ArrayList<GetAllCollegeResponse.College> colleges) {
        this.context = context;
        this.colleges = colleges;
    }

    @Override
    public int getCount() {
        return colleges.size();
    }

    @Override
    public Object getItem(int position) {
        return colleges.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dropdown, parent, false);
        }

        TextView textAutoComplete = view.findViewById(R.id.textAutoComplete);
        TextView count = view.findViewById(R.id.count);

        GetAllCollegeResponse.College college = colleges.get(position);
        textAutoComplete.setText(college.getCollegeName());
        count.setText("Follower : " + college.getFollowerCount());

        return view;
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }


    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<GetAllCollegeResponse.College>(colleges);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<GetAllCollegeResponse.College> matchValues = new ArrayList<GetAllCollegeResponse.College>();

                for (GetAllCollegeResponse.College dataItem : dataListAllItems) {
                    if (dataItem.getCollegeName().toLowerCase().contains(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                colleges = (ArrayList<GetAllCollegeResponse.College>) results.values;
            } else {
                colleges = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
