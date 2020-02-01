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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.adprogressbarlib.AdCircleProgress;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dakrory.a7med.cargomarine.Models.vehicalsData;
import dakrory.a7med.cargomarine.Models.vehicalsData.vehicalItem;
import dakrory.a7med.cargomarine.R;
import dakrory.a7med.cargomarine.helpers.Constants;
import dakrory.a7med.cargomarine.vehicalDetails;
import dakrory.a7med.cargomarine.vehicalView;

public class vehicalsAdapter extends RecyclerView.Adapter<vehicalsAdapter.ViewHolder> implements Filterable {

    private List<vehicalItem> vehicalsData;
    private List<vehicalItem> vehicalsDataFull;

    Activity activity;
    public vehicalsAdapter(List<vehicalItem> vehicalsData,List<vehicalItem> vehicalsDataFull,Activity activity) {
        this.vehicalsData = vehicalsData;
        this.vehicalsDataFull=vehicalsDataFull;
        this.activity=activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_vehical, parent, false);
        vehicalsAdapter.ViewHolder viewHolder = new vehicalsAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final vehicalItem vItem = vehicalsData.get(position);
        Picasso.get().load(Constants.ImageBaseUrl + vItem.getUrl()).placeholder(R.mipmap.car).into(holder.carImage);
        holder.uuid.setText(String.valueOf("Vin: " + vItem.getUuid()));
        holder.makeModel.setText(String.valueOf(vItem.getMake() + " " + vItem.getModel() + " " + vItem.getYear()));

        holder.lastUpdate.setText("Last update: " + String.valueOf(vItem.getLastUpdateCar().toString()));
        holder.shipperName.setText("Shipper: " + String.valueOf(vItem.getFirstName() + " " + vItem.getLastName()));
        if (vItem.getReleaseOption() == 1) {
            Picasso.get().load(R.mipmap.no).into(holder.stateImage);
        } else {
            Picasso.get().load(R.mipmap.ok).into(holder.stateImage);
        }

        holder.carItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("AhmedDakrory","Car Selected: "+vItem.getId());
                Intent openCarDetails=new Intent(activity,vehicalView.class);
                openCarDetails.putExtra(Constants.CarIdData,vItem.getId());
                activity.startActivity(openCarDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicalsData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<vehicalItem> filterData = new ArrayList<>();
            if (constraint.length() == 0 || constraint == null) {
                filterData.addAll(vehicalsDataFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (vehicalItem item : vehicalsDataFull) {
                    if (item.getLastName().toLowerCase().trim().contains(filterPattern)
                            || item.getFirstName().toLowerCase().trim().contains(filterPattern)
                            || item.getYear().toLowerCase().trim().contains(filterPattern)
                            || item.getModel().toLowerCase().trim().contains(filterPattern)
                            || item.getMake().toLowerCase().trim().contains(filterPattern)
                            || item.getUuid().toLowerCase().trim().contains(filterPattern)) {
                        filterData.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterData;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            vehicalsData.clear();
            vehicalsData.addAll((List)results.values);
            Log.v("AhmedDakrory3","Size: "+vehicalsData.size());
            Log.v("AhmedDakrory3","SizeFull: "+vehicalsDataFull.size());
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView carImage;
        public ImageView stateImage;
        public TextView uuid;
        public TextView makeModel;
        public TextView shipperName;
        public TextView lastUpdate;
        public LinearLayout carItem;
        public ViewHolder(View itemView) {
            super(itemView);
            this.carImage = (ImageView) itemView.findViewById(R.id.carImage);
            this.stateImage = (ImageView) itemView.findViewById(R.id.stateImage);
            this.makeModel = (TextView) itemView.findViewById(R.id.makeModel);
            this.uuid = (TextView) itemView.findViewById(R.id.vehicalUUID);
            this.carItem = (LinearLayout) itemView.findViewById(R.id.caritem);
            this.shipperName = (TextView) itemView.findViewById(R.id.ShipperName);
            this.lastUpdate = (TextView) itemView.findViewById(R.id.lastUpdate);

        }
    }
}
