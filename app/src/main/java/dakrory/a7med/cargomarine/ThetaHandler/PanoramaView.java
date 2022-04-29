package dakrory.a7med.cargomarine.ThetaHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.R;
import com.lespinside.simplepanorama.view.SphericalView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class PanoramaView extends Activity   implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer;

    ProgressDialog progress ;


    private float AccelData[] = new float[3];


    private long lastTime;

    private SphericalView sphericalView;

    boolean imageSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }



        setContentView(R.layout.activity_panorama_view);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        progress.show();
        sphericalView = (SphericalView) findViewById(R.id.spherical_view);



        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);
        mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);


        try {
            Intent intent = getIntent();
            String urlImage = intent.getExtras().getString("pan3D");
            int pan3DType = intent.getExtras().getInt("pan3DType");
//            URL url = new URL(urlImage);

            Thread th1 =new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.v("AhmedDakrory",urlImage);

                    try {
//                        Bitmap original = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//
//                        ByteArrayOutputStream out = new ByteArrayOutputStream();
//                        original.compress(Bitmap.CompressFormat.PNG, 1, out);
//                        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                        Bitmap bitmap = null;
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        options.inSampleSize = 2;
                            if(pan3DType== vehicalsDetails.TYPE_FILE) {

                         bitmap = BitmapFactory.decodeFile(urlImage, options);
                            }else {
                                URL url = new URL(urlImage);
                                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream(),null,options);
                            }
                        bitmap = getResizedBitmap(bitmap, 2048, 1024);

                        sphericalView.setPanorama(bitmap, false);
                        Log.v("AhmedDakrory", String.valueOf(sphericalView.isInertiaEnabled()));
                        Log.v("AhmedDakrory", String.valueOf(sphericalView.isAccelerometerEnabled()));

                        imageSet  = true;
//            sphericalView.setPanorama(PLUtils.getBitmap(this, R.raw.ieye), false);

                    } catch (Error | MalformedURLException err){
                        Log.v("AhmedDakroryError:",err.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PanoramaView.this,"Error handle image",Toast.LENGTH_SHORT).show();

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.v("AhmedDakroryError:",e.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PanoramaView.this,"Error handle image",Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                    progress.dismiss();

                }
            });
            th1.start();




            }catch (Error err){
                Log.v("AhmedDakroryError:",err.toString());
                Toast.makeText(PanoramaView.this,"Error handle image",Toast.LENGTH_SHORT).show();

            }



    }


    float ConstantPI2 = (float) (2);
    public void onSensorChanged (SensorEvent event) {
        if(imageSet) {
            if (event.sensor.getType() == mSensorAccelerometer.TYPE_GYROSCOPE) {
                long currentTime = System.currentTimeMillis();
                long TimeInterval = currentTime - lastTime;
                if (TimeInterval > 20) {
                    lastTime = currentTime;

                    AccelData[0] = event.values[0];
                    AccelData[1] = event.values[1];
                    AccelData[2] = event.values[2];


                    try {

                        sphericalView.getCamera().lookAt((AccelData[0]*ConstantPI2)+sphericalView.getCamera().getLookAtRotation().pitch, (-AccelData[1]*ConstantPI2)+sphericalView.getCamera().getLookAtRotation().yaw);
                    }catch (Exception ee){

                    }

                }
            }
        }
        }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    protected int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sphericalView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sphericalView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sphericalView.onDestroy();
    }




}