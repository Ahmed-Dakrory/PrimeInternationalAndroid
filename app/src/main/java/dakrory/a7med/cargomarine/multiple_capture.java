package dakrory.a7med.cargomarine;

import android.Manifest;
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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
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

public class multiple_capture extends AppCompatActivity  implements SensorEventListener {

    private static final String TAG = multiple_capture.class.getSimpleName();

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


    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private float vibrateThreshold = 2;

    private ImageView rasterImage;
    public Vibrator v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_exterior_cam);

        thisIntent = getIntent();
        CarVin = thisIntent.getStringExtra("Data");
//        Log.v("AhmedDakrory",CarVin);
        container = findViewById(R.id.camera_container);
        save_image = findViewById(R.id.save_image);
        text_view = findViewById(R.id.text_prediction);
        camera_capture_button = findViewById(R.id.camera_capture_button);
        view_finder = findViewById(R.id.view_finder);
        rasterImage = findViewById(R.id.rasterImage);


        executor = Executors.newSingleThreadExecutor();

        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderInner = new AlertDialog.Builder(multiple_capture.this);
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Intent data = new Intent();
                                //---set the data to pass back---
                                data.putExtra("Type_Of_Return","CAMERA");
                                data.putExtra("DATA",getExternalFilesDir(null) + File.separator +"nycargoCarMainImages"+ File.separator +CarVin);
                                setResult(RESULT_OK, data);
                                //---close the activity---
                                finish();
                            }
                        });

                    }
                });

                builderInner.show();

            }
        });

        camera_capture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File folder = new File(getExternalFilesDir(null) + File.separator +"nycargoCarMainImages");
                boolean success = true;
                if (!folder.exists()) {
                    //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
                    success = folder.mkdir();
                }

                folder = new File(getExternalFilesDir(null) + File.separator +"nycargoCarMainImages"+ File.separator +CarVin);
                success = true;
                if (!folder.exists()) {
                    //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
                    success = folder.mkdir();
                }
                if(success) {
                    Bitmap bm = view_finder.getBitmap();
                    bm = resize(bm, 640, 410);
                    Log.v("AhmedDakrory", String.valueOf(bm.getHeight()));

                    try (FileOutputStream out = new FileOutputStream(folder.toString()+ File.separator +inc+".png")) {
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance

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




        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            // fai! we dont have an accelerometer!
        }

        //initialize vibration
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

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


    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
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

    @Override
    public void onSensorChanged(SensorEvent event) {

        // display the current x,y,z accelerometer values
        displayCurrentValues();
        // display the max x,y,z accelerometer values
        displayMaxValues();

        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if the change is below 2, it is just plain noise
        if (deltaZ < vibrateThreshold)
            deltaZ = 0;
        if (deltaY < vibrateThreshold)
            deltaY = 0;
        if ( (deltaY != 0) || (deltaZ != 0)) {

            Log.v("AccelerometerZ",String.valueOf("Vibrate"));
            v.vibrate(50);
            rasterImage.setImageResource(R.mipmap.red_raster);
        }else{

            rasterImage.setImageResource(R.mipmap.blue_raster);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void displayCurrentValues() {
        Log.v("AccelerometerY",String.valueOf(Float.toString(deltaY)));
        Log.v("AccelerometerZ",String.valueOf(Float.toString(deltaZ)));
    }

    // display the max x,y,z accelerometer values
    public void displayMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX;
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
        }
        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ;
        }
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