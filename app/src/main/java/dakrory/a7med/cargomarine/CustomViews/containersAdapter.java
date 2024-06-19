package dakrory.a7med.cargomarine.CustomViews;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dakrory.a7med.cargomarine.Models.containersDataAllList.containerItemOfAllList;
import dakrory.a7med.cargomarine.R;
import dakrory.a7med.cargomarine.helpers.Constants;
import dakrory.a7med.cargomarine.containerView;

public class containersAdapter extends RecyclerView.Adapter<containersAdapter.ViewHolder> implements Filterable {

    private List<containerItemOfAllList> containersData;
    private List<containerItemOfAllList> containersDataFull;

    Activity activity;
    public containersAdapter(List<containerItemOfAllList> containersData, List<containerItemOfAllList> containersDataFull, Activity activity) {
        this.containersData = containersData;
        this.containersDataFull=containersDataFull;
        this.activity=activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_container, parent, false);
        containersAdapter.ViewHolder viewHolder = new containersAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final containerItemOfAllList vItem = containersData.get(position);
        Picasso.get().load(Constants.ImageBaseUrl + vItem.getMainUrl()).placeholder(R.mipmap.img).into(holder.containerImage);
        Log.v("IMAGE_AHMED_DAKRORY",String.valueOf(vItem.getMainUrl()));
        holder.container_number.setText(String.valueOf(vItem.getContainer_number()));
        try{

            holder.date_of_container.setText("Date: " + String.valueOf(vItem.getDatetime().toString()));
        }catch (Error e){

        }catch (Exception exception){

        }


        holder.containerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("AhmedDakrory","Container Selected: "+vItem.getId());
                Intent openContainerDetails=new Intent(activity,containerView.class);
                openContainerDetails.putExtra(Constants.ContainerIdData,vItem.getId());
                activity.startActivity(openContainerDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return containersData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<containerItemOfAllList> filterData = new ArrayList<>();
            if (constraint.length() == 0 || constraint == null) {
                filterData.addAll(containersDataFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (containerItemOfAllList item : containersDataFull) {

                    if (item.getContainer_number().toLowerCase().trim().contains(filterPattern)
                            || item.getDescription_of_container().toLowerCase().trim().contains(filterPattern)) {

                        filterData.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterData;
            Log.v("AhmedDakrory","Data: "+((ArrayList) filterResults.values).size());
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if(results.values!=null){
                containersData.clear();
                containersData.addAll((List)results.values);
                //Log.v("AhmedDakrory3","Size: "+containersData.size());
                //Log.v("AhmedDakrory3","SizeFull: "+containersDataFull.size());
            }

            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView containerImage;
        public TextView container_number;
        public TextView date_of_container;
        public LinearLayout containerItem;
        public ViewHolder(View itemView) {
            super(itemView);
            this.containerImage = (ImageView) itemView.findViewById(R.id.containerImage2);
            this.container_number = (TextView) itemView.findViewById(R.id.container_number);
            this.containerItem = (LinearLayout) itemView.findViewById(R.id.containeritem);
            this.date_of_container = (TextView) itemView.findViewById(R.id.date_of_container);

        }
    }
}
