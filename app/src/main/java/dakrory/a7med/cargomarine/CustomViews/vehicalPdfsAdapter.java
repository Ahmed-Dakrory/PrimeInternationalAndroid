package dakrory.a7med.cargomarine.CustomViews;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.adprogressbarlib.AdCircleProgress;

import java.io.File;
import java.util.List;

import dakrory.a7med.cargomarine.BuildConfig;
import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.R;
import dakrory.a7med.cargomarine.ViewFullPdf;
import dakrory.a7med.cargomarine.helpers.Constants;


public class vehicalPdfsAdapter extends RecyclerView.Adapter<vehicalPdfsAdapter.ViewHolder> {
    private List<vehicalsDetails.urlItem> listdata;
    private Activity activity;
    RecyclerView recyclerView;

    public vehicalPdfsAdapter(RecyclerView recyclerView, List<vehicalsDetails.urlItem> listdata, Activity activity) {
        this.listdata = listdata;
        this.activity = activity;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @NonNull int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_pdf, parent, false);
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

                    Uri uri = Uri.parse(myImageData.getUrl());
                    Log.v("AhmedDakrory","CodeNew: "+myImageData.getUrl());
                    //String selectedFilePath = FilePath.getPath(activity, uri);
                    final File pdfFile = new File(uri.getPath());
                    if (pdfFile.exists()) //Checking for the file is exist or not
                    {

                        Uri path = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider",pdfFile);
                        viewPdf(path,activity);
                    }else{

                        Log.v("AhmedDakrory","Non CodeNew: "+myImageData.getUrl());
                    }


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

                    Intent openFullView =new Intent(activity, ViewFullPdf.class);
                    openFullView.putExtra(Constants.ImageUrl_Type,myImageData.getType());
                    openFullView.putExtra(Constants.SET_MODE_INTENT,Constants.MODE_VIEW);
                    openFullView.putExtra(Constants.ImageUrl_INTENT,Constants.PdfBaseUrl+myImageData.getUrl()+"&Pdf=1");
                    activity.startActivity(openFullView);




                }
            });
        }

    }

    private void viewPdf(Uri file, final Activity activity) {
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(file, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("No Application Found");
            builder.setMessage("Download one from Android Market?");
            builder.setPositiveButton("Yes, Please",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
                            activity.startActivity(marketIntent);
                        }
                    });
            builder.setNegativeButton("No, Thanks", null);
            builder.create().show();
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