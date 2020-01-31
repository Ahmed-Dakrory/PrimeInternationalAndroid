package dakrory.a7med.cargomarine;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adprogressbarlib.AdCircleProgress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import dakrory.a7med.cargomarine.CustomViews.CallBackViewChanger;
import dakrory.a7med.cargomarine.CustomViews.MyImageAdapter;
import dakrory.a7med.cargomarine.CustomViews.MyImageData;
import dakrory.a7med.cargomarine.helpers.Api;
import dakrory.a7med.cargomarine.helpers.FileUploader;
import dakrory.a7med.cargomarine.helpers.MyResponse;
import dakrory.a7med.cargomarine.layoutManagers.ZoomCenterCardLayoutManager;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonView;

    RecyclerView recyclerView;


    List<MyImageData> myImageData = new ArrayList<>();
    MyImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EasyImage.configuration(this)
                .setImagesFolderName("My app images") // images folder name, default is "EasyImage"
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(false); // if you want to use internal memory for storying images - default




        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonView = (Button) findViewById(R.id.buttonViewImage);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MyImageAdapter(recyclerView,myImageData,this);
        ZoomCenterCardLayoutManager layoutManager=new ZoomCenterCardLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonView.setOnClickListener(this);


        //intializing scan object
        qrScan = new IntentIntegrator(this);


        getAllImagesToAdapter();

    }

    private void getAllImagesToAdapter() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        Api api = retrofit.create(Api.class);

        Log.v("AhmedDakrory:","Start Upload3");
        //creating a call and calling the upload image method
        Call<JsonObject> call = api.getAllImages();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject post = (JsonObject) response.body();
                if(post!=null){
                    JsonArray array =post.getAsJsonArray("images");
                    Log.v("AhmedDakrory","Size"+array.size());
                    for(int i=0;i<array.size();i++){
                        String id =  array.get(i).getAsJsonObject().get("id").getAsString();
                        String desc =  array.get(i).getAsJsonObject().get("desc").getAsString();
                        String url =  array.get(i).getAsJsonObject().get("image").getAsString();
                        myImageData.add(new MyImageData(desc, url, url, MyImageData.TYPE_Server, new CallBackViewChanger() {
                            @Override
                            public void setViewToPercentage(AdCircleProgress loader, TextView overlayView, TextView markView) {
                                Log.v("AhmedDakrory","New!!!!!");
                            }
                        }));
                        adapter.notifyDataSetChanged();
                        Log.v("AhmedDakrory",url);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                String vin = getVinNumber( result.getContents());
                Toast.makeText(this, vin, Toast.LENGTH_LONG).show();

                    Log.v("AhmedDakrory", vin); // Prints scan results


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode!=100) {

                EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {

                    @Override
                    public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                        //Some error handling
                        e.printStackTrace();
                    }

                    @Override
                    public void onImagesPicked(@NonNull final List<File> list, EasyImage.ImageSource imageSource, int i) {
                        if(list.get(0)!=null) {
                            File file = list.get(0);
                          uploadFileAndAddToAdapter(file);

                        }
                    }



                    @Override
                    public void onCanceled(EasyImage.ImageSource source, int type) {
                        //Cancel handling, you might wanna remove taken photo if it was canceled
                        if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                            File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                            if (photoFile != null) photoFile.delete();
                        }
                    }


                });

            }

        }



    }

    private void uploadFileAndAddToAdapter(final File file) {

        myImageData.add(new MyImageData(file.getPath(), file.getPath(), file.getPath(), MyImageData.TYPE_FILE, new CallBackViewChanger() {
            @Override
            public void setViewToPercentage(final AdCircleProgress loader,final TextView overlayView,final TextView markView) {
                new FileUploader().uploadFile(file.getPath(), "My Image", MainActivity.this, new FileUploader.FileUploaderCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(MainActivity.this, "Some error occurred...", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinish(Response<MyResponse> response) {
                        Log.v("AhmedDakrory:", "Finish");
                        loader.setVisibility(View.GONE);
                        overlayView.setVisibility(View.GONE);
                        markView.setTextColor(MainActivity.this.getResources().getColor(R.color.colorGreenSign));

                        Toast.makeText(MainActivity.this, "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgressUpdate(int currentpercent, int totalpercent) {

                        loader.setProgress(Float.parseFloat(String.valueOf(currentpercent)));
                        Log.v("AhmedDakrory:", String.valueOf(currentpercent) + " / " + String.valueOf(totalpercent));
                    }
                });
            }
        }));
        adapter.notifyItemInserted(adapter.getItemCount()-1);
        recyclerView.scrollToPosition(adapter.getItemCount()-1);
  /*
        recyclerView.setScrollX(adapter.getItemCount()-1);
        View v =  recyclerView.getChildAt(adapter.getItemCount()-1);
        if(v==null){
            Log.v("AhmedDakrory","Null Ya man");
        }
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        final TextView markView = (TextView) v.findViewById(R.id.mark);
        final TextView overlayView=(TextView)v.findViewById(R.id.backgroundWhite);
        RelativeLayout relativeLayout= (RelativeLayout)v.findViewById(R.id.relativeLayout);
        final AdCircleProgress loader= (AdCircleProgress) v.findViewById(R.id.donut_progress);
        Log.v("AhmedDakrory","Ahmed Start");


*/

    }

    private String getVinNumber(String contents) {


        int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 0, 1,
                2, 3, 4, 5, 0, 7, 0, 9, 2, 3,
                4, 5, 6, 7, 8, 9 };
        int[] weights = { 8, 7, 6, 5, 4, 3, 2, 10, 0, 9,
                8, 7, 6, 5, 4, 3, 2 };

        String s = contents;
        s = s.replaceAll("-", "");
        s = s.toUpperCase();
        if (s.length() != 17){
            StringBuilder sb = new StringBuilder(s);
            sb.deleteCharAt(0);
            s = sb.toString();
        }

        if (s.length() != 17){
            return "";
        }

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char c = s.charAt(i);
            int value;
            int weight = weights[i];

            // letter
            if (c >= 'A' && c <= 'Z') {
                value = values[c - 'A'];
                if (value == 0)
                    return "";
            }

            // number
            else if (c >= '0' && c <= '9') value = c - '0';

                // illegal character
            else  return "";

            sum = sum + weight * value;

        }

        // check digit
        sum = sum % 11;
        char check = s.charAt(8);
        if (check != 'X' && (check < '0' || check > '9'))
            throw new RuntimeException("Illegal check digit: " + check);
        if      (sum == 10 && check == 'X')  return s;
        else if (sum == check - '0')        return s;
        else                                return s;

    }




    @Override
    public void onClick(View v) {

        if(v == buttonUpload){
            captureImage();
        }

        if(v == buttonView){
            viewImage();
        }
    }

    private void captureImage() {

        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    2);

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    3);
        }else {
            EasyImage.openCameraForImage(this,0);
        }
    }

    //qr code scanner object
    private IntentIntegrator qrScan;
    private void viewImage() {


        qrScan.initiateScan();


    }

    }
