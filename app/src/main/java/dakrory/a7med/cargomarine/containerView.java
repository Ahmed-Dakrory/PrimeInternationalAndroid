package dakrory.a7med.cargomarine;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dakrory.a7med.cargomarine.CustomViews.containerImagesAdapter;
import dakrory.a7med.cargomarine.Models.containersDetails;
import dakrory.a7med.cargomarine.Models.userData;
import dakrory.a7med.cargomarine.helpers.Api;
import dakrory.a7med.cargomarine.helpers.Constants;
import dakrory.a7med.cargomarine.helpers.FileUploader;
import dakrory.a7med.cargomarine.helpers.MyResponse;
import dakrory.a7med.cargomarine.helpers.modelsFunctions;
import dakrory.a7med.cargomarine.layoutManagers.ZoomCenterCardLayoutManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class containerView extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    RecyclerView recyclerViewImages;


    int mCurrentImageIndex=0;
    List<Bitmap> animBitmaps;
    int numberOfImages;

    dialogWithProgress mainDialog;

    RelativeLayout layoutForImages;



    FloatingActionButton addImageFloatingButton;

    FloatingActionButton saveAllNewResultsFloatingActionButton;

    containerImagesAdapter adapterForImages;


    Button imagesButton;



    EditText container_numberEdit;
    EditText descriptionEdit;



    LinearLayout layoutRelease;
    ImageButton setContainerDateButton;
    TextView ContainerDateTextView;



    int idOfContainer=-1;
    int Mode;
    private boolean AllowedToModify = false;
    static containersDetails containerData;


    //Add New Variables
    String container_numberNew;

    Retrofit retrofit = null;
    RelativeLayout loaderPanel;
    userData thisAccountUserData = LoginActivity.thisAccountCredData;



    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_container_view);
        containerData=new containersDetails();
        containerData.setData(new containersDetails.containerDetails());
        containerData.setImages(new ArrayList<containersDetails.urlItem>());

        defineViewsAndIntegrate();


        addImageFloatingButton.setOnClickListener(this);
        saveAllNewResultsFloatingActionButton.setOnClickListener(this);


        imagesButton.setOnClickListener(this);
        setContainerDateButton.setOnClickListener(this);

        setContainerDateButton.setOnClickListener(this);
        Intent lastIntentGet=getIntent();
        Mode = lastIntentGet.getIntExtra(Constants.SET_MODE_INTENT,-1);


        if(Mode == Constants.MODE_ADD_NEW){
            container_numberNew = lastIntentGet.getStringExtra(Constants.Container_NUMBER_Add_New);
            Log.v("AhmedDakrory5",container_numberNew);

            handleContainer_NUMBERAndSetViewAfterRequestFromOutSide(container_numberNew);
        }else{

            idOfContainer = lastIntentGet.getIntExtra(Constants.ContainerIdData,-1);

            getAllDataToAdapter();
        }

        Log.v("AhmedTest","DATANEW_:"+String.valueOf(idOfContainer));


        if(thisAccountUserData.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_MAIN || thisAccountUserData.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_MAIN2){
            AllowedToModify = true;
        }else{
            AllowedToModify = false;
        }


        if(!AllowedToModify){
            setALLEditableFieldsAccess();

        }












    }




    private void handleContainer_NUMBERAndSetViewAfterRequestFromOutSide(final String container_number) {

        if(modelsFunctions.checkNetworkStatus(this)) {

            containersDetails.containerDetails dataOfContainer = new containersDetails.containerDetails();

            dataOfContainer.setContainer_number(container_number);
            dataOfContainer.setDescription_of_container("");




            containerData.setData(dataOfContainer);
            setTextData(containerData.getData());

            addNewContainerToServer();

        }else {

            Toast.makeText(this,R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
        }
    }

    private void addNewContainerToServer() {
        containerData.getData().setContainer_number(container_numberEdit.getText().toString());
        containerData.getData().setDescription_of_container(descriptionEdit.getText().toString());




        loaderPanel.setVisibility(View.VISIBLE);

        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating our api
        Api api = retrofit.create(Api.class);

        //creating a call and calling the upload image method
        Call<containersDetails> call = api.insertNewContainer(containerData.getData());

        if(modelsFunctions.checkNetworkStatus(this)) {
            call.enqueue(new Callback<containersDetails>() {
                @Override
                public void onResponse(Call<containersDetails> call, Response<containersDetails> response) {
                    containersDetails vDetails = response.body();


                    containerData.setData(vDetails.getData());
                    containerData.getImages().addAll(vDetails.getImages());

                    idOfContainer = containerData.getData().getId();
                    setTextData(containerData.getData());

                    loaderPanel.setVisibility(View.GONE);
                    Log.v("AhmedDakrory33", String.valueOf(vDetails.getData().getContainer_number() + ", " + vDetails.getData().getId() ));
                    adapterForImages.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<containersDetails> call, Throwable t) {
                    Toast.makeText(containerView.this, getString(R.string.ProblemErrorOnServerAddNewContainer), Toast.LENGTH_LONG).show();
                    loaderPanel.setVisibility(View.GONE);
                    Log.v("AhmedDakrory", t.toString());
                }
            });
        }else{

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(containerView.this,R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
                    loaderPanel.setVisibility(View.GONE);
                }
            });
            }




    }

    private void setALLEditableFieldsAccess() {
        container_numberEdit.setKeyListener(null);
        descriptionEdit.setKeyListener(null);
        saveAllNewResultsFloatingActionButton.hide();
        addImageFloatingButton.hide();

        setContainerDateButton.setEnabled(false);
       }



    private void defineViewsAndIntegrate() {
        loaderPanel = (RelativeLayout)findViewById(R.id.loaderPanel2);
        recyclerViewImages = (RecyclerView) findViewById(R.id.recyclerViewForImages2);
        layoutForImages = (RelativeLayout) findViewById(R.id.layoutForImages2);


        addImageFloatingButton=(FloatingActionButton)findViewById(R.id.addImageFloating2);
        saveAllNewResultsFloatingActionButton=(FloatingActionButton)findViewById(R.id.saveAllNewResults2);


        imagesButton = (Button) findViewById(R.id.ImagesButton2);


        container_numberEdit= (EditText)findViewById(R.id.container_numberEdit);
        descriptionEdit= (EditText)findViewById(R.id.descriptionEdit2);




        ContainerDateTextView = (TextView)findViewById(R.id.DateTextView);
        setContainerDateButton=(ImageButton)findViewById(R.id.setDate);
        layoutRelease = (LinearLayout)findViewById(R.id.layoutRelease2);


        Log.v("AhmedDakrory","Size of Images"+containerData.getImages().size());
        adapterForImages = new containerImagesAdapter(recyclerViewImages,containerData.getImages(), containerView.this);
        ZoomCenterCardLayoutManager layoutManager=new ZoomCenterCardLayoutManager(containerView.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewImages.setLayoutManager(layoutManager);
        recyclerViewImages.setAdapter(adapterForImages);




    }

    private void getAllDataToAdapter() {

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

        //creating a call and calling the upload image method
        Call<containersDetails> call = api.getAllDetailsForContainer(idOfContainer);
        if(modelsFunctions.checkNetworkStatus(this)) {

            call.enqueue(new Callback<containersDetails>() {
                @Override
                public void onResponse(Call<containersDetails> call, Response<containersDetails> response) {

                    containersDetails container_data = response.body();
                    Log.v("AhmedTest","NEW: "+String.valueOf(container_data));
                    if (container_data != null) {

                        containerData.setData(container_data.getData());



                        containerData.getImages().addAll(container_data.getImages());
                        adapterForImages.notifyDataSetChanged();




                        setTextData(containerData.getData());
                        loaderPanel.setVisibility(View.GONE);





                    }
                }

                @Override
                public void onFailure(Call<containersDetails> call, Throwable t) {

                    loaderPanel.setVisibility(View.GONE);
                    Log.v("AhmedTest",String.valueOf(t.getMessage()));
                    Toast.makeText(containerView.this, getString(R.string.ProblemOnHandleData), Toast.LENGTH_LONG).show();
                }
            });
        }else{

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(containerView.this,R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
                }
            });
        }


    }


    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    private void setTextData(containersDetails.containerDetails data) {

         container_numberEdit.setText(String.valueOf(data.getContainer_number()));
         descriptionEdit.setText(String.valueOf(data.getDescription_of_container()));


        setDateContainer(data.getDatetime());



    }





    private void setDateContainer(String releaseDate) {

            if(releaseDate != null) {
                if (!releaseDate.equalsIgnoreCase("")) {
                    //Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(releaseDate);

                    ContainerDateTextView.setText(releaseDate);
                }
            }

    }

    DialogInterface.OnClickListener dialogImageClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    captureImage(Constants.TypeImageForServer);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    storageImage(Constants.TypeImageForServer);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ImagesButton2){
            //ShowImages
            layoutForImages.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.addImageFloating2){
            Log.v("AhmedDakrory","ButtonImage");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("From Camera or Storage?").setPositiveButton("Camera", dialogImageClickListener)
                    .setNegativeButton("Storage", dialogImageClickListener).show();
        }else if(v.getId()==R.id.saveAllNewResults2){
            addNewContainerToServer();
        }else if(v.getId()==R.id.setDate){
            selectDateToRelease();
        }
    }



    private void selectDateToRelease() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                containerView.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dpd.show(getFragmentManager(), "Release Date");
    }

    private void storageImage(int typeForImageOrDoc) {

        if(ContextCompat.checkSelfPermission(containerView.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){


            ActivityCompat.requestPermissions(containerView.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        }else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            photoPickerIntent.putExtra("Type_Of_Return","SELECT_INTERNAL");
            Log.v("AhmedDakrory","Type1: "+typeForImageOrDoc);
            startActivityForResult(photoPickerIntent, typeForImageOrDoc);
        }
    }







    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }
    private void captureImage(int typeForImageOrDoc) {


        if(ContextCompat.checkSelfPermission(containerView.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){


            ActivityCompat.requestPermissions(containerView.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        }else {
            Intent openImageOrDoc= new Intent(containerView.this,multiple_capture.class);
            openImageOrDoc.putExtra("Data",String.valueOf(containerData.getData().getId()));
            startActivityForResult(openImageOrDoc, typeForImageOrDoc);
        }



    }


    public void upload_file_number(File dir,String[] children,int i,int typeForImageOrDoc){

        boolean finalEnd_image = false;
        if(i==children.length-1){
            finalEnd_image = true;
        }

        boolean finalEnd_image1 = finalEnd_image;
        final int[] typeForImageOrDocf = {typeForImageOrDoc};
        new FileUploader().uploadFileContainer(new File(dir, children[i]).getPath(), containerData.getData().getId(), containerView.this, new FileUploader.FileUploaderCallback() {
            @Override
            public void onError(Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mainDialog.dialog.dismiss();
                    }
                });
                Log.v("AhmedDakrory77","Error: "+String.valueOf(t.toString()));
            }

            @Override
            public void onFinish(Response<MyResponse> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        File dir_delete = new File(dir, children[i]);

                        if (dir_delete.isDirectory())
                        {

                            dir_delete.delete();

                        }
                        if(dir_delete.exists())
                            dir_delete.delete();
//                        Log.v("AhmedDakrory","Pers: "+String.valueOf(i)+" , "+String.valueOf(children.length-1)+" , "+String.valueOf((float) ((((i*100 /(children.length-1)) ))))+" , "+String.valueOf((int)(float) ((((i*100 /(children.length-1)) )))));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mainDialog.dialog.show();
                                try {
                                    mainDialog.setPercentage((int) 100,"File number "+String.valueOf(i+1)+"/"+String.valueOf(children.length)+" Loading ");


                                }catch (Error error){
                                    Log.v("AhmedDakroryError",String.valueOf(error));
                                }catch (Exception exc){
                                    Log.v("AhmedDakroryExc",String.valueOf(exc));

                                }
                            }
                        });

                        if (finalEnd_image1){

                            mainDialog.dialog.dismiss();
                            getAllDataToAdapter();
//                            if(typeForImageOrDocf[0] ==Constants.TypeDocForServer){
//
//                                adapterForImages.notifyItemInserted(adapterForDocs.getItemCount() - 1);
//                                recyclerViewImages.scrollToPosition(adapterForDocs.getItemCount() - 1);
//                            }else if(typeForImageOrDocf[0] ==Constants.TypeImageForServer){
//
//                                adapterForImages.notifyItemInserted(adapterForImages.getItemCount() - 1);
//                                recyclerViewImages.scrollToPosition(adapterForImages.getItemCount() - 1);
//                            }
                        }else{

                            upload_file_number(dir,children,i+1,typeForImageOrDoc);
                        }





                    }
                });
                Log.v("AhmedDakrory","Pers: "+String.valueOf("Loaded"));
                MyResponse response1 = response.body();
                if (finalEnd_image1) {
                    Log.v("AhmedDakrory", "Pers: " + response1.getMessage());
                    Toast.makeText(containerView.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onProgressUpdate(int currentpercent, int totalpercent) {
//                        Log.v("AhmedDakrory","Pers: "+String.valueOf(currentpercent));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mainDialog.dialog.show();
                        try {
                            mainDialog.setPercentage((int) currentpercent,"File number "+String.valueOf(i+1)+"/"+String.valueOf(children.length)+" Loading ");


                        }catch (Error error){
                            Log.v("AhmedDakroryError",String.valueOf(error));
                        }catch (Exception exc){
                            Log.v("AhmedDakroryExc",String.valueOf(exc));

                        }
                    }
                });


            }
        },typeForImageOrDoc);


    }


    public  void uploadFileContainerAndAddToAdapter(final File file, final int typeForImageOrDoc) {
        if (containerData.getData().getId() != 0) {
            Log.v("AhmedDakrory", file.getAbsolutePath() + " :Ok");
            if (typeForImageOrDoc == Constants.TypeImageForServer) {

                Log.v("AhmedDakrory", typeForImageOrDoc + " :Ok 3");
                Log.v("AhmedDakrory", containerData.getData().getId() + " :Ok 3");

                boolean end_image = false;
                Log.v("AhmedDakrory", containerData.getData().getId() + " :Ok 4");
                mainDialog = new containerView.dialogWithProgress(containerView.this);
                Log.v("AhmedDakrory", containerData.getData().getId() + " :Ok 5");
                mainDialog.dialog.show();
                Log.v("AhmedDakrory", containerData.getData().getId() + " :Ok 6");
                mainDialog.setPercentage(0, "");
                Log.v("AhmedDakrory", containerData.getData().getId() + " :Ok 7");
                File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "nycargoCarMainImages" + File.separator + containerData.getData().getId());

                Log.v("AhmedDakrory",String.valueOf(dir.isDirectory()));
                Log.v("AHMED_DARKRORY3",String.valueOf(dir));
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    Log.v("AHMED_DARKRORY3",String.valueOf(children.length));
                    upload_file_number(dir, children, 0, typeForImageOrDoc);

                }
            }
            else {
                Toast.makeText(this, "Problem For Container", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("AhmedDakrory","Returned3");
        if(requestCode == Constants.TypeImageForServer){
            if(resultCode == RESULT_OK) {
                File folder = new File(Environment.getExternalStorageDirectory() + File.separator +"nycargoCarMainImages");
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdir();
                }

                folder = new File(Environment.getExternalStorageDirectory() + File.separator +"nycargoCarMainImages"+ File.separator +containerData.getData().getId());
                success = true;
                if (!folder.exists()) {
                    success = folder.mkdir();
                }

                String TYPE = data.getStringExtra("Type_Of_Return");
                Log.v("AhmedDakrory",String.valueOf(TYPE));
                if(TYPE!=null){
                    String imageUri2 = data.getStringExtra("DATA");

                    Log.v("AhmedDakrory", imageUri2.toString());
                    final File file = new File(imageUri2.toString());
                    Log.v("AhmedDakrory", "Type: "+Constants.TypeImageForServer);
                    uploadFileContainerAndAddToAdapter(file,Constants.TypeImageForServer);

                }else{
                    Log.v("AhmedDakrory","Ahmed");

                    if(data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        final File file = new File(Environment.getExternalStorageDirectory() + File.separator +"nycargoCarMainImages"+ File.separator +containerData.getData().getId());

                        for(int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //do something with the image (save it to some directory or whatever you need to do with it here)


                            File auxFile = new File(Environment.getExternalStorageDirectory() + File.separator +"nycargoCarMainImages"+ File.separator +containerData.getData().getId()+File.separator+"Image_"+String.valueOf(i)+".jpg");
                            OutputStream os = null;
                            try {
                                os = new BufferedOutputStream(new FileOutputStream(auxFile));
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                                os.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }





                        }

                        uploadFileContainerAndAddToAdapter(file,Constants.TypeImageForServer);
                    }


                }


            }
        }
        else if(requestCode!=100) {
            Log.v("AhmedDakrory", "Ok");


        }
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
            int SIZE_LIMIT = 1500;
            if (lengthInKb > SIZE_LIMIT) {
                compressBitmap(fileNew, (sampleSize*2), (quality/4));
            }

            bitmap.recycle();
            return fileNew;
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("AhmedDakrory99","Error: "+String.valueOf(e.toString()));
        }
        return file;
    }

    public  Retrofit getClient(String vin) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_TO_GET_VIN_DATA+vin)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        containerData.getData().setDatetime(format1.format(date));
        setTextData(containerData.getData());
    }


    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
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

        public void setPercentage(int percentage,String s){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text.setProgress(percentage);
                    text2.setText(s+String.valueOf(percentage));
                }
            });




        }
    }

}
