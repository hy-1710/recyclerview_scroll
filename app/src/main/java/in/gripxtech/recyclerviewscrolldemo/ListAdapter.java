package in.gripxtech.recyclerviewscrolldemo;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.gripxtech.recyclerviewscrolldemo.databinding.ItemViewListBinding;
import in.gripxtech.recyclerviewscrolldemo.model.ListItem;
import in.gripxtech.recyclerviewscrolldemo.model.ListViewHolder;
import in.gripxtech.recyclerviewscrolldemo.prefs.Prefs;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    public static final String TAG;

    static {
        TAG = "ListAdapter";
    }

    private MainActivity activity;
    private ArrayList<ListItem> items;
    private Prefs prefs;

    public ListAdapter(MainActivity activity, ArrayList<ListItem> items) {
        this.activity = activity;
        this.items = items;
        this.prefs = new Prefs(activity);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemViewListBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.item_view_list,
                parent,
                false
        );
        return new ListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        ListItem item = items.get(position);
        holder.getBinding().tvSubhead.setText(item.getPosition() + " " + activity.getString(R.string.app_name));
        holder.getBinding().bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setScrollX(activity.getScrollX());
                prefs.setScrollY(activity.getScrollY());
                activity.getLoader().forceLoad();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<ListItem> getItems() {
        return items;
    }
}
