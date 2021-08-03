package dakrory.a7med.cargomarine.CustomViews;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.adprogressbarlib.AdCircleProgress;
import dakrory.a7med.cargomarine.R;
import dakrory.a7med.cargomarine.ViewFullImage;
import dakrory.a7med.cargomarine.helpers.Constants;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.ViewHolder>{
    private List<MyImageData> listdata;
    private Activity activity;
     RecyclerView recyclerView;
    public MyImageAdapter(RecyclerView recyclerView,List<MyImageData> listdata, Activity activity) {
        this.listdata = listdata;
        this.activity = activity;
        this.recyclerView=recyclerView;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,@NonNull int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final MyImageData myImageData = listdata.get(position);
        myImageData.callBackViewChanger.setViewToPercentage(holder.loader,holder.overlayView,holder.markView);
if(listdata.get(position).getType()==MyImageData.TYPE_FILE) {

    if (listdata.get(position).getImage() != null) {
        File f = new File(listdata.get(position).getImage());
        Picasso.get().load(f).into(holder.imageView);


    }

}else{
    activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Picasso.get().load(listdata.get(position).getImage()).placeholder(R.drawable.animation_loader).into(holder.imageView);
            holder.loader.setVisibility(View.GONE);
            holder.overlayView.setVisibility(View.GONE);
            holder.markView.setTextColor(activity.getResources().getColor(R.color.colorGreenSign));
        }
    });


}

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openFullView =new Intent(activity, ViewFullImage.class);
                openFullView.putExtra(Constants.ImageUrl_Type,myImageData.getType());
                openFullView.putExtra(Constants.ImageUrl_INTENT,myImageData.getImage());
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