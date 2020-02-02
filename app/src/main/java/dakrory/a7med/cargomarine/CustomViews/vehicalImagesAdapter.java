package dakrory.a7med.cargomarine.CustomViews;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.adprogressbarlib.AdCircleProgress;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.R;
import dakrory.a7med.cargomarine.ViewFullImage;
import dakrory.a7med.cargomarine.helpers.Constants;


public class vehicalImagesAdapter extends RecyclerView.Adapter<vehicalImagesAdapter.ViewHolder> {
    private List<vehicalsDetails.urlItem> listdata;
    private Activity activity;
    RecyclerView recyclerView;

    public vehicalImagesAdapter(RecyclerView recyclerView, List<vehicalsDetails.urlItem> listdata, Activity activity) {
        this.listdata = listdata;
        this.activity = activity;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @NonNull int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final vehicalsDetails.urlItem myImageData = listdata.get(position);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(Constants.ImageBaseUrl+listdata.get(position).getUrl()).placeholder(R.drawable.animation_loader).into(holder.imageView);
                holder.loader.setVisibility(View.GONE);
                holder.overlayView.setVisibility(View.GONE);
                holder.markView.setTextColor(activity.getResources().getColor(R.color.colorGreenSign));
            }
        });


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openFullView =new Intent(activity, ViewFullImage.class);
                openFullView.putExtra(Constants.ImageUrl_Type,myImageData.getType());
                openFullView.putExtra(Constants.SET_MODE_INTENT,Constants.MODE_VIEW);
                openFullView.putExtra(Constants.ImageUrl_INTENT,Constants.ImageBaseUrl+myImageData.getUrl());
                activity.startActivity(openFullView);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView markView;
        public TextView overlayView;
        public RelativeLayout relativeLayout;
        public AdCircleProgress loader;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.markView = (TextView) itemView.findViewById(R.id.mark);
            this.loader = (AdCircleProgress) itemView.findViewById(R.id.donut_progress);
            this.overlayView=(TextView)itemView.findViewById(R.id.backgroundWhite);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}