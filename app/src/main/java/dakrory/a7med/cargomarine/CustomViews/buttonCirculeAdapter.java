package dakrory.a7med.cargomarine.CustomViews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dakrory.a7med.cargomarine.Models.buttonDataCircle;
import dakrory.a7med.cargomarine.Models.vehicalsDataAllList.vehicalItemOfAllList;
import dakrory.a7med.cargomarine.R;
import dakrory.a7med.cargomarine.damageActivity;
import dakrory.a7med.cargomarine.helpers.Constants;
import dakrory.a7med.cargomarine.vehicalView;

public class buttonCirculeAdapter extends RecyclerView.Adapter<buttonCirculeAdapter.ViewHolder>  {

    private List<buttonDataCircle> buttonsData;

    Activity activity;
    public buttonCirculeAdapter(List<buttonDataCircle> buttonsData, Activity activity) {
        this.buttonsData = buttonsData;
        this.activity=activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.button_circle, parent, false);
        buttonCirculeAdapter.ViewHolder viewHolder = new buttonCirculeAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final buttonDataCircle vItem = buttonsData.get(position);
        holder.button.setText(String.valueOf(vItem.getName()));
        holder.textView.setText(String.valueOf(vItem.getDescription()));


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(damageActivity.lastSelectedButton!=null){

                    damageActivity.lastSelectedButton.setBackground(activity.getResources().getDrawable(R.drawable.designbutton));
                    damageActivity.lastSelectedButton.setTextColor(activity.getResources().getColor(R.color.whitesmall));
                }
                damageActivity.lastSelectedButton = holder.button;

                damageActivity.drawableImage = vItem.getDrawableImage();
                damageActivity.drawableImageType =vItem.getType();
                holder.button.setBackground(activity.getResources().getDrawable(R.drawable.designbutton_selected));
                holder.button.setTextColor(activity.getResources().getColor(R.color.mdtp_transparent_black));
            }
        });


    }

    @Override
    public int getItemCount() {
        return buttonsData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button button;
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.button = (Button) itemView.findViewById(R.id.buttonOfDamage);
            this.textView = (TextView) itemView.findViewById(R.id.textViewOfDamage);

        }
    }
}
