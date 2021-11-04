package dakrory.a7med.cargomarine.CustomViews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.R;

public class CustomAdapterForPersonModel extends ArrayAdapter<vehicalsDetails.modelIdAndName> {


    LayoutInflater flater;
    public CustomAdapterForPersonModel(Activity context, int resouceId, int textviewId, List<vehicalsDetails.modelIdAndName> list) {
        super(context,resouceId,textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getDropDownView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = flater.inflate(R.layout.listitems_names_layout,parent, false);
        }
        vehicalsDetails.modelIdAndName rowItem = getItem(position);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(rowItem.getName());
        return convertView;
    }





}
