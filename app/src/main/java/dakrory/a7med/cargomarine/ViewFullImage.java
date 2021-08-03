package dakrory.a7med.cargomarine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.helpers.Constants;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ViewFullImage extends Activity {
    private ZoomageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_image);
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(Constants.ImageUrl_INTENT);
        int imageUrlType = intent.getIntExtra(Constants.ImageUrl_Type,0);

        imageView = (ZoomageView) findViewById(R.id.imageViewFull);

        if(imageUrlType== vehicalsDetails.TYPE_FILE){
            Log.v("AhmedDakrory","FileData: "+imageUrl);
            File f = new File(imageUrl);
            Picasso.get().load(f).into(imageView);
        }else{

            Picasso.get().load(imageUrl).placeholder(R.drawable.animation_loader).into(imageView);
        }

    }
}