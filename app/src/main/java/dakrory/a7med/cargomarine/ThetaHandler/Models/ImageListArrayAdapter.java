package dakrory.a7med.cargomarine.ThetaHandler.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import dakrory.a7med.cargomarine.R;

import java.util.List;

public class ImageListArrayAdapter extends ArrayAdapter<imageDetails> {

    private List<imageDetails> rows;
    private LayoutInflater inflater;

    public ImageListArrayAdapter(Context context, int resourceIdOfListLayout, List<imageDetails> rows) {
        super(context, resourceIdOfListLayout, rows);
        this.rows = rows;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(dakrory.a7med.cargomarine.R.layout.listlayout_object, null);
        }

        imageDetails row = rows.get(position);

        ImageView thumbnail = (ImageView) view.findViewById(R.id.object_thumbnail);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(row.url, options);
        thumbnail.setImageBitmap(bitmap);


        TextView title = (TextView) view.findViewById(R.id.list_item_title);
        title.setText(row.getFileName());

        TextView description = (TextView) view.findViewById(R.id.list_item_description);
        description.setText(row.size+" Mb");

        return view;
    }

    @Override
    public int getCount() {
        Log.v("AhmedDakrory",String.valueOf(this.rows.size()));
        return this.rows.size();
    }
}
