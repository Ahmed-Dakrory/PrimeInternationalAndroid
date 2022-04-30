package dakrory.a7med.cargomarine;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import dakrory.a7med.cargomarine.ThetaHandler.Models.ImageListArrayAdapter;
import dakrory.a7med.cargomarine.ThetaHandler.Models.imageDetails;
import dakrory.a7med.cargomarine.ThetaHandler.PanoramaView;

import java.io.File;
import java.util.ArrayList;

import androidx.core.app.ActivityCompat;

public class allImagesActivity extends Activity {

    private ListView allImagesList;
    ArrayList<imageDetails> allImages;
    ImageListArrayAdapter imageAdapter;

    ImageButton openCameraCap;
    ImageButton reloadImages;

    ProgressDialog progress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_images);
        allImagesList = (ListView)findViewById(R.id.object_list);
        openCameraCap = (ImageButton) findViewById(R.id.goToCapturing);
        reloadImages = (ImageButton) findViewById(R.id.reloadImages);

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        progress.show();

        reloadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.show();
                handleAllImages();

                progress.dismiss();
            }
        });

        openCameraCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(allImagesActivity.this, CameraActivity.class);
                startActivity(i);


            }
        });




        new Thread(new Runnable() {
            @Override
            public void run() {

                handleAllImages();
            }
        }).start();


    }

    private void handleAllImages() {


        File folder = new File(Environment.getExternalStorageDirectory() + File.separator +"PrimeShippingCarServices");

        if (!folder.exists()) {
            //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            folder.mkdir();
        }

        String targetPath = Environment.getExternalStorageDirectory() + File.separator +"PrimeShippingCarServices";



        allImages = new ArrayList<imageDetails>();
        File targetDirector = new File(targetPath);

        File[] files = targetDirector.listFiles();
        for (File file : files) {
            String fileUrl = file.getAbsolutePath();
            String size = String.valueOf((float)((float)file.length()/1024/1024));
            String filename = fileUrl.split("/")[fileUrl.split("/").length-1];
            allImages.add(new imageDetails(fileUrl,filename,size));
            Log.v("AhmedDakrory",file.getAbsolutePath());
        }


        imageAdapter = new ImageListArrayAdapter(this, R.layout.listlayout_object,allImages);

        try {
            allImagesList.setAdapter(imageAdapter);
        }catch (Error e){

        }catch (Exception ex){

        }

        allImagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                AlertDialog.Builder builderInner = new AlertDialog.Builder(allImagesActivity.this);
//		final EditText edittext = new EditText(MainActivity.this);
//		builderInner.setMessage(strName);

                builderInner.setIcon(R.mipmap.cap);
                builderInner.setTitle("Do you want to select or Show?");
                builderInner.setNegativeButton("Show", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v("AhmedDakrorySa","Open it");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.show();
                                Intent i = new Intent(allImagesActivity.this, PanoramaView.class);
                                i.putExtra("pan3D", allImages.get(position).getUrl());
                                startActivity(i);
                            }
                        });
                    }
                });



                builderInner.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent data = new Intent();
                        //---set the data to pass back---
                        data.setData(Uri.parse(allImages.get(position).getUrl()));
                        setResult(RESULT_OK, data);
                        //---close the activity---
                        finish();

                    }
                });

                builderInner.show();





            }
        });


        Log.v("AhmedDakroryItems",String.valueOf(allImagesList.getAdapter().getCount()));



        imageAdapter.notifyDataSetInvalidated();
        progress.dismiss();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(allImagesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(allImagesActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 88);

        }


        handleAllImages();
        progress.dismiss();
    }
}