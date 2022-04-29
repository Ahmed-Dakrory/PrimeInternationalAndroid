package dakrory.a7med.cargomarine.CustomViews;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adprogressbarlib.AdCircleProgress;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import dakrory.a7med.cargomarine.BuildConfig;
import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.R;
import dakrory.a7med.cargomarine.ThetaHandler.PanoramaView;
import dakrory.a7med.cargomarine.allImagesActivity;
import dakrory.a7med.cargomarine.helpers.Constants;


public class vehical3DImagesAdapter extends RecyclerView.Adapter<vehical3DImagesAdapter.ViewHolder> {
    private List<vehicalsDetails.urlItem> listdata;
    private Activity activity;
    RecyclerView recyclerView;

    private static final int REQUEST_NETWORK_STATE = 1;
    private static String[] ACCESS_NETWORK_STATE = {Manifest.permission.INTERNET};

    public vehical3DImagesAdapter(RecyclerView recyclerView, List<vehicalsDetails.urlItem> listdata, Activity activity) {
        this.listdata = listdata;
        this.activity = activity;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @NonNull int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_3d_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final vehicalsDetails.urlItem myImageData = listdata.get(position);


        if(listdata.get(position).getType()==vehicalsDetails.TYPE_FILE) {
            myImageData.getCallBackViewChanger().setViewToPercentage(holder.loader,holder.overlayView,holder.markView);

            if (listdata.get(position).getUrl() != null) {
               holder.nameFile.setText(listdata.get(position).getUrl());


            }

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.nameFile.setText(myImageData.getUrl());

                    Log.v("AhmedDakrory","CodeNew: "+myImageData.getUrl());
                    //String selectedFilePath = FilePath.getPath(activity, uri);

                    Toast.makeText(activity,"Cannot Show until Reopen",Toast.LENGTH_SHORT).show();
//                        view3D(Uri.parse(myImageData.getUrl()),activity,vehicalsDetails.TYPE_FILE);



                }
            });

        }else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    holder.nameFile.setText(listdata.get(position).getUrl());
                    //Picasso.get().load(Constants.ImageBaseUrl + listdata.get(position).getUrl()).placeholder(R.drawable.animation_loader).into(holder.imageView);
                    holder.loader.setVisibility(View.GONE);
                    holder.overlayView.setVisibility(View.GONE);
                    holder.markView.setTextColor(activity.getResources().getColor(R.color.colorGreenSign));
                }
            });

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //action after load
                    Log.v("AhmedDakrory","Code: "+myImageData.getType());


                    view3D(Uri.parse(Constants.Base3DUrl+myImageData.getUrl()),activity,vehicalsDetails.TYPE_Server);
//                    Intent openFullView =new Intent(activity, ViewFullPdf.class);
//                    openFullView.putExtra(Constants.ImageUrl_Type,myImageData.getType());
//                    openFullView.putExtra(Constants.SET_MODE_INTENT,Constants.MODE_VIEW);
//                    openFullView.putExtra(Constants.ImageUrl_INTENT,Constants.Base3DUrl+myImageData.getUrl()+"&Pdf=1");
//                    activity.startActivity(openFullView);




                }
            });
        }

    }

    private void view3D(Uri file, final Activity activity,int type) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    ACCESS_NETWORK_STATE,
                    REQUEST_NETWORK_STATE
            );
        }else {

            Log.v("AhmedDakrory", "URI:" + String.valueOf(file.toString()));
            Intent i = new Intent(activity, PanoramaView.class);
            i.putExtra("pan3D", file.toString());
            i.putExtra("pan3DType", type);
            activity.startActivity(i);
        }
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView markView;
        public TextView nameFile;
        public TextView overlayView;
        public RelativeLayout relativeLayout;
        public AdCircleProgress loader;
        public ViewHolder(View itemView) {
            super(itemView);
            this.markView = (TextView) itemView.findViewById(R.id.mark);
            this.nameFile = (TextView) itemView.findViewById(R.id.nameFile);
            this.loader = (AdCircleProgress) itemView.findViewById(R.id.donut_progress);
            this.overlayView=(TextView)itemView.findViewById(R.id.backgroundWhite);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}