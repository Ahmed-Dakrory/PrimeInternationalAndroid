package dakrory.a7med.cargomarine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import dakrory.a7med.cargomarine.ThetaHandler.Models.imageDetails;
import dakrory.a7med.cargomarine.ThetaHandler.PanoramaView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class exteriorCam extends AppCompatActivity {

    private static final String TAG = exteriorCam.class.getSimpleName();

    private static final int REQUEST_CODE_CAMERA_PERMISSION = 200;
    private static final String[] PERMISSIONS = {Manifest.permission.CAMERA};

    ConstraintLayout container;
    TextView text_view;
    ImageButton camera_capture_button;
    ImageButton save_image;
    PreviewView view_finder;

    dialogWithProgress mainDialog;

    Executor executor;
    private long mLastAnalysisResultTime;
    String CarVin="sdfkskdkd321sd6546sd";
    int lengthOfImages = 0;
    int inc=1;
    Intent thisIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_exterior_cam);

        thisIntent = getIntent();
        CarVin = thisIntent.getStringExtra("Data");
        Log.v("AhmedDakrory",CarVin);
        container = findViewById(R.id.camera_container);
        save_image = findViewById(R.id.save_image);
        text_view = findViewById(R.id.text_prediction);
        camera_capture_button = findViewById(R.id.camera_capture_button);
        view_finder = findViewById(R.id.view_finder);

        executor = Executors.newSingleThreadExecutor();

        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderInner = new AlertDialog.Builder(exteriorCam.this);
                builderInner.setIcon(R.mipmap.save);
                builderInner.setTitle("Do you want to upload the current selected Images?");
                builderInner.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v("AhmedDakrorySa","Open it");

                    }
                });



                builderInner.setPositiveButton("upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<Bitmap> bitmapList = new ArrayList<Bitmap>();

                                File folder = new File(Environment.getExternalStorageDirectory() + File.separator +"PrimeShippingCarServices");
                                boolean success = true;
                                if (!folder.exists()) {
                                    //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
                                    success = folder.mkdir();
                                }

                                folder = new File(Environment.getExternalStorageDirectory() + File.separator +"PrimeShippingCarServices"+ File.separator +CarVin);
                                success = true;
                                if (!folder.exists()) {
                                    //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
                                    success = folder.mkdir();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mainDialog=new dialogWithProgress(exteriorCam.this);
                                        mainDialog.dialog.show();
                                        mainDialog.setPercentage(0);

                                    }
                                });

                                File[] files = folder.listFiles();
                                Arrays.sort(files);
                                int allFilelength = files.length;
                                int ii=0;
                                for (int file_index=0;file_index<files.length;file_index++) {
                                    File file = new File(folder.toString()+ File.separator +String.valueOf(file_index+1)+".png");

                                    if(file.exists()) {
                                        Log.v("AhmedDakrory", file.getName());

                                        File newFile = compressBitmap(file, 5, 75);
                                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                        Bitmap bitmap = BitmapFactory.decodeFile(newFile.getAbsolutePath(), bmOptions);
                                        bitmapList.add(bitmap);
                                        ii++;
                                        int finalIi = ii;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mainDialog.dialog.show();
//                                                Log.v("AhmedDakrory", String.valueOf((((finalIi)* 100) / allFilelength )));
                                                mainDialog.setPercentage((int) (((finalIi)* 100) / allFilelength ));
                                            }
                                        });

                                    }
                                }
                                Bitmap mainBitmap = combinAllImages(bitmapList);

                                lengthOfImages = ii;
                                try (FileOutputStream out = new FileOutputStream(folder.toString()+ File.separator +"last_$"+String.valueOf(lengthOfImages)+"_$"+".png")) {
                                    mainBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                    // PNG is a lossless format, the compression factor (100) is ignored

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                File finalFolder = folder;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        mainDialog.dialog.dismiss();
                                        Intent data = new Intent();
                                        //---set the data to pass back---
                                        data.setData(Uri.parse(finalFolder.toString()+ File.separator +"last_$"+String.valueOf(lengthOfImages)+"_$"+".png"));
                                        setResult(RESULT_OK, data);
                                        //---close the activity---
                                        finish();
                                    }
                                });

                            }
                        });

                        t1.start();


                    }
                });

                builderInner.show();

            }
        });

        camera_capture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File folder = new File(Environment.getExternalStorageDirectory() + File.separator +"PrimeShippingCarServices");
                boolean success = true;
                if (!folder.exists()) {
                    //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
                    success = folder.mkdir();
                }

                folder = new File(Environment.getExternalStorageDirectory() + File.separator +"PrimeShippingCarServices"+ File.separator +CarVin);
                success = true;
                if (!folder.exists()) {
                    //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
                    success = folder.mkdir();
                }
                if(success) {
                    Bitmap bm = view_finder.getBitmap();
                    bm = resize(bm, 640, 480);
                    Log.v("AhmedDakrory", String.valueOf(bm.getHeight()));

                    try (FileOutputStream out = new FileOutputStream(folder.toString()+ File.separator +inc+".png")) {
                        bm.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                        inc++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if(checkPermission()) {
            startCamera();
        }
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    REQUEST_CODE_CAMERA_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(
                        this,
                        "You can't use image classification example without granting CAMERA permission",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
            } else {
                startCamera();
            }
        }
    }

    private void startCamera() {

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture
                = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        executor = Executors.newSingleThreadExecutor();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(224, 224))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(executor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                int rotationDegrees = image.getImageInfo().getRotationDegrees();

                if(SystemClock.elapsedRealtime() - mLastAnalysisResultTime < 500) {
                    image.close();
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        long duration = SystemClock.elapsedRealtime() - mLastAnalysisResultTime;
                        double fps;

                        if(duration > 0)
                            fps = 1000.f / duration;
                        else
                            fps = 1000.f;

                        text_view.setText(String.format(Locale.US, "%.1f fps", fps));
                    }
                });

                mLastAnalysisResultTime = SystemClock.elapsedRealtime();
                image.close();
            }
        });

        cameraProvider.unbindAll();
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this,
                cameraSelector, imageAnalysis, preview);

        preview.setSurfaceProvider(view_finder.getSurfaceProvider());

    }


    public Bitmap combinAllImages(List<Bitmap> bitmapList){
            if(bitmapList==null){
                return null;
            }else {
                if (bitmapList.size() == 1) {
                    return bitmapList.get(0);
                } else if (bitmapList.size() == 2) {
                    return combineImages(bitmapList.get(0), bitmapList.get(1));
                }else{
                    Bitmap mainBm = bitmapList.get(0);
                    for(int i=1;i<bitmapList.size();i++){
                        mainBm = combineImages(mainBm, bitmapList.get(i));
                    }
                    return mainBm;
                }
            }
    }

    public Bitmap combineImages(Bitmap c, Bitmap s) {
        // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth() + s.getWidth();
            height = c.getHeight();
        } else {
            width = s.getWidth() + s.getWidth();
            height = c.getHeight();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, c.getWidth(), 0f, null);


        return cs;
    }


    public File compressBitmap(File file, int sampleSize, int quality) {
        try {

            String filePath = file.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);


            File f3=new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES)+"/TempImages/");
            if(!f3.exists())
                f3.mkdirs();
            OutputStream outputStream = null;
            File fileNew = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES)+"/TempImages/seconds"+".jpeg");

            outputStream = new FileOutputStream(fileNew);

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.close();
            long lengthInKb = file.length() / 1024; //in kb
            int SIZE_LIMIT = 500;
            if (lengthInKb > SIZE_LIMIT) {
                compressBitmap(fileNew, (sampleSize*2), (quality/4));
            }

            bitmap.recycle();
            return fileNew;
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("AhmedDakrory","Error: "+String.valueOf(e.toString()));
        }
        return file;
    }




    class dialogWithProgress extends Dialog {

        final Dialog dialog ;
        ProgressBar text;
        TextView text2;
        public dialogWithProgress(@NonNull Context context) {
            super(context);
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_progress);
            text = (ProgressBar) dialog.findViewById(R.id.progress_horizontal);
            text2 = dialog.findViewById(R.id.value123);






            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);


        }


        public Dialog getDialog() {
            return dialog;
        }

        public void setPercentage(int percentage){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text.setProgress(percentage);
                    text2.setText(String.valueOf(percentage));
                }
            });




        }
    }


    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

}