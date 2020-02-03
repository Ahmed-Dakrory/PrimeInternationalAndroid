package dakrory.a7med.cargomarine;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adprogressbarlib.AdCircleProgress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dakrory.a7med.cargomarine.CustomViews.CallBackViewChanger;
import dakrory.a7med.cargomarine.CustomViews.MyImageData;
import dakrory.a7med.cargomarine.CustomViews.vehicalImagesAdapter;
import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.Models.vehicalsDetails.carDetails;
import dakrory.a7med.cargomarine.Models.vinDetails;
import dakrory.a7med.cargomarine.helpers.Api;
import dakrory.a7med.cargomarine.helpers.Constants;
import dakrory.a7med.cargomarine.helpers.FileUploader;
import dakrory.a7med.cargomarine.helpers.MyResponse;
import dakrory.a7med.cargomarine.layoutManagers.ZoomCenterCardLayoutManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class vehicalView extends AppCompatActivity implements View.OnClickListener {


    RecyclerView recyclerViewImages;
    RecyclerView recyclerViewDocs;

    RelativeLayout layoutForImages;
    RelativeLayout layoutForDocs;

    FloatingActionButton addImageFloatingButton;
    FloatingActionButton addDocFloatingButton;

    vehicalImagesAdapter adapterForImages;
    vehicalImagesAdapter adapterForDocs;

    Button imagesButton;
    Button DocsButton;


    EditText vinEdit;
    EditText ModelEdit;
    EditText MakeEdit;
    EditText YearEdit;
    EditText descriptionEdit;
    EditText assemlyCountryEdit;
    EditText colorEdit;
    EditText engineLitersEdit;
    EditText bodyStyleEdit;
    EditText engineTypeEdit;


    EditText mainUserName;
    EditText main2UserName;
    EditText shipperUserName;
    EditText vendorUserName;
    EditText customerUserName;
    EditText consigneeUserName;


    int idOfCar=-1;
    int Mode;
    private boolean AllowedToModify = false;
    vehicalsDetails carData;


    //Add New Variables
    String vinNew;

    Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehical_view);
        carData=new vehicalsDetails();
        carData.setData(new carDetails());
        carData.setImages(new ArrayList<vehicalsDetails.urlItem>());
        carData.setDocs(new ArrayList<vehicalsDetails.urlItem>());


        defineViewsAndIntegrate();

        EasyImage.configuration(this)
                .setImagesFolderName("Cargo Marine") // images folder name, default is "EasyImage"
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(false); // if you want to use internal memory for storying images - default



        addImageFloatingButton.setOnClickListener(this);
        addDocFloatingButton.setOnClickListener(this);

        imagesButton.setOnClickListener(this);
        DocsButton.setOnClickListener(this);

        Intent lastIntentGet=getIntent();
        Mode = lastIntentGet.getIntExtra(Constants.SET_MODE_INTENT,-1);


        if(Mode == Constants.MODE_ADD_NEW){
            vinNew = lastIntentGet.getStringExtra(Constants.Car_VIN_Add_New);
            Log.v("AhmedDakrory5",vinNew);

            handleVinAndSetViewAfterRequestFromOutSide(vinNew);
        }else{

            idOfCar = lastIntentGet.getIntExtra(Constants.CarIdData,-1);

            getAllDataToAdapter();
        }

        if(!AllowedToModify){
            setViewsToNotEdit();
        }

    }

    private void handleVinAndSetViewAfterRequestFromOutSide(final String vinNew) {
        Api apiInterface = getClient(vinNew+"/").create(Api.class);
        //creating a call and calling the upload image method
        Call<vinDetails> call = apiInterface.getCarDetailsfromVin();
        call.enqueue(new Callback<vinDetails>() {
            @Override
            public void onResponse(Call<vinDetails> call, Response<vinDetails> response) {
                vinDetails car = response.body();
                if(car !=null){
                    vehicalsDetails.carDetails dataOfCar = new carDetails();
                    dataOfCar.setState(0);
                    dataOfCar.setUuid(vinNew);
                    dataOfCar.setMake(car.Results.get(0).Make);
                    dataOfCar.setModel(car.Results.get(0).Model);
                    dataOfCar.setYear(car.Results.get(0).ModelYear);
                    dataOfCar.setAssemlyCountry(car.Results.get(0).PlantCountry);
                    dataOfCar.setBodyStyle(car.Results.get(0).DriveType);
                    dataOfCar.setEngineLiters(car.Results.get(0).DisplacementL);
                    dataOfCar.setEngineType(car.Results.get(0).EngineConfiguration+"- "+car.Results.get(0).EngineCylinders+" Cylinders");



                    dataOfCar.setMainId(48);

                    carData.setData(dataOfCar);
                    setTextData(carData.getData());

                    addNewCarToServer();

                }
            }

            @Override
            public void onFailure(Call<vinDetails> call, Throwable t) {

            }
        });
    }

    private void addNewCarToServer() {


        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating our api
        Api api = retrofit.create(Api.class);

        //creating a call and calling the upload image method
        Call<vehicalsDetails> call = api.insertNewCar(carData.getData());
        call.enqueue(new Callback<vehicalsDetails>() {
            @Override
            public void onResponse(Call<vehicalsDetails> call, Response<vehicalsDetails> response) {
               vehicalsDetails vDetails = response.body();
               carData.setData(vDetails.getData());
               carData.getImages().addAll(vDetails.getImages());
               carData.getDocs().addAll(vDetails.getDocs());
               Log.v("AhmedDakrory33",String.valueOf(vDetails.getData().getUuid()+", "+vDetails.getData().getId()));
            }

            @Override
            public void onFailure(Call<vehicalsDetails> call, Throwable t) {
                Log.v("AhmedDakrory",t.toString());
            }
        });
    }

    private void setViewsToNotEdit() {
        vinEdit.setKeyListener(null);
        ModelEdit.setKeyListener(null);
        MakeEdit.setKeyListener(null);
        YearEdit.setKeyListener(null);
        descriptionEdit.setKeyListener(null);
        assemlyCountryEdit.setKeyListener(null);
        colorEdit.setKeyListener(null);
        engineLitersEdit.setKeyListener(null);
        bodyStyleEdit.setKeyListener(null);
        engineTypeEdit.setKeyListener(null);



        //Persons
        mainUserName.setKeyListener(null);
        main2UserName.setKeyListener(null);
        shipperUserName.setKeyListener(null);
        vendorUserName.setKeyListener(null);
        customerUserName.setKeyListener(null);
        consigneeUserName.setKeyListener(null);
    }

    private void defineViewsAndIntegrate() {

        recyclerViewImages = (RecyclerView) findViewById(R.id.recyclerViewForImages);
        recyclerViewDocs = (RecyclerView) findViewById(R.id.recyclerViewForDocs);


        layoutForDocs = (RelativeLayout) findViewById(R.id.layoutForDocs);
        layoutForImages = (RelativeLayout) findViewById(R.id.layoutForImages);


        addImageFloatingButton=(FloatingActionButton)findViewById(R.id.addImageFloating);
        addDocFloatingButton=(FloatingActionButton)findViewById(R.id.addDocFloating);

        imagesButton = (Button) findViewById(R.id.ImagesButton);
        DocsButton = (Button) findViewById(R.id.DocumentsButton);


        vinEdit= (EditText)findViewById(R.id.vinEdit);
        ModelEdit= (EditText)findViewById(R.id.modelEdit);
        MakeEdit= (EditText)findViewById(R.id.makeEdit);
        YearEdit= (EditText)findViewById(R.id.yearEdit);
        descriptionEdit= (EditText)findViewById(R.id.descriptionEdit);
        assemlyCountryEdit= (EditText)findViewById(R.id.assemlyCountryEdit);
        colorEdit= (EditText)findViewById(R.id.colorEdit);
        engineLitersEdit= (EditText)findViewById(R.id.engineLitersEdit);
        bodyStyleEdit= (EditText)findViewById(R.id.bodyStyleEdit);
        engineTypeEdit= (EditText)findViewById(R.id.engineTypeEdit);



        //Persons

        mainUserName= (EditText)findViewById(R.id.MainUserEdit);
        main2UserName= (EditText)findViewById(R.id.MainUser2Edit);
        shipperUserName= (EditText)findViewById(R.id.shipperNameEdit);
        vendorUserName= (EditText)findViewById(R.id.vendorEdit);
        customerUserName= (EditText)findViewById(R.id.customerEdit);
        consigneeUserName= (EditText)findViewById(R.id.consigneeEdit);

        Log.v("AhmedDakrory","Size of Images"+carData.getImages().size());
        adapterForImages = new vehicalImagesAdapter(recyclerViewImages,carData.getImages(),vehicalView.this);
        ZoomCenterCardLayoutManager layoutManager=new ZoomCenterCardLayoutManager(vehicalView.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewImages.setLayoutManager(layoutManager);
        recyclerViewImages.setAdapter(adapterForImages);


        Log.v("AhmedDakrory","Size of Images"+carData.getDocs().size());
        adapterForDocs = new vehicalImagesAdapter(recyclerViewDocs,carData.getDocs(),vehicalView.this);
        ZoomCenterCardLayoutManager layoutManager2=new ZoomCenterCardLayoutManager(vehicalView.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDocs.setHasFixedSize(true);
        recyclerViewDocs.setLayoutManager(layoutManager2);
        recyclerViewDocs.setAdapter(adapterForDocs);
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
        Call<vehicalsDetails> call = api.getAllDetailsForCar(idOfCar);
        call.enqueue(new Callback<vehicalsDetails>() {
            @Override
            public void onResponse(Call<vehicalsDetails> call, Response<vehicalsDetails> response) {

                 vehicalsDetails car_data =  response.body();
                if(car_data!=null){

                    carData.setData(car_data.getData());

                    carData.getImages().addAll(car_data.getImages());
                    Log.v("AhmedDakrory","Size of Images"+carData.getImages().size());

                    adapterForImages.notifyDataSetChanged();


                    carData.getDocs().addAll(car_data.getDocs());
                    Log.v("AhmedDakrory","Size of Images"+carData.getDocs().size());
                    adapterForDocs.notifyDataSetChanged();

                    setTextData(carData.getData());
                }
            }

            @Override
            public void onFailure(Call<vehicalsDetails> call, Throwable t) {

            }
        });
    }

    private void setTextData(vehicalsDetails.carDetails data) {
         vinEdit.setText(String.valueOf(data.getUuid()));
         ModelEdit.setText(String.valueOf(data.getModel()));
         MakeEdit.setText(String.valueOf(data.getMake()));
         YearEdit.setText(String.valueOf(data.getYear()));
         descriptionEdit.setText(String.valueOf(data.getDescription()));
         assemlyCountryEdit.setText(String.valueOf(data.getAssemlyCountry()));
         colorEdit.setText(String.valueOf(data.getColor()));
         engineLitersEdit.setText(String.valueOf(data.getEngineLiters()));
         bodyStyleEdit.setText(String.valueOf(data.getBodyStyle()));
        engineTypeEdit.setText(String.valueOf(data.getEngineType()));


        setTextUsernameToField(mainUserName,data.getUserfirstName(),data.getUserlastName());
        setTextUsernameToField(main2UserName,data.getMainTwofirstName(),data.getMainTwolastName());
        setTextUsernameToField(shipperUserName,data.getShipperfirstName(),data.getShipperlastName());
        setTextUsernameToField(vendorUserName,data.getVendorfirstName(),data.getVendorlastName());
        setTextUsernameToField(customerUserName,data.getCustomerfirstName(),data.getCustomerlastName());
        setTextUsernameToField(consigneeUserName,data.getConsigneefirstName(),data.getConsigneelastName());

    }

    public void setTextUsernameToField(EditText field,String firstName,String lastName){
        if(firstName!=null ||firstName.equalsIgnoreCase("")){

                field.setText(String.valueOf(firstName+" "+lastName));

        }else{
            field.setText(String.valueOf("Not Set!!!"));
        }

        }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ImagesButton){
            //ShowImages
            layoutForImages.setVisibility(View.VISIBLE);
            layoutForDocs.setVisibility(View.GONE);
        }else if(v.getId()==R.id.DocumentsButton){
            layoutForImages.setVisibility(View.GONE);
            layoutForDocs.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.addImageFloating){
            Log.v("AhmedDakrory","ButtonImage");
            captureImage(Constants.TypeImageForServer);
        }else if(v.getId()==R.id.addDocFloating){
            Log.v("AhmedDakrory","ButtonDoc");
            captureImage(Constants.TypeDocForServer);

        }
    }

    private void captureImage(int typeForImageOrDoc) {

        if(ContextCompat.checkSelfPermission(vehicalView.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(vehicalView.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(vehicalView.this,
                    new String[]{Manifest.permission.CAMERA},
                    2);

            ActivityCompat.requestPermissions(vehicalView.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    3);
        }else {
            EasyImage.openCameraForImage(this,typeForImageOrDoc);
        }
    }
    private void uploadFileAndAddToAdapter(final File file, final int typeForImageOrDoc) {
if(carData.getData().getId()!=0) {
    Log.v("AhmedDakrory", typeForImageOrDoc + " :Ok");
    if (typeForImageOrDoc == Constants.TypeImageForServer) {

        Log.v("AhmedDakrory", typeForImageOrDoc + " :Ok 3");
        Log.v("AhmedDakrory", carData.getData().getId() + " :Ok 3");
        carData.getImages().add(new vehicalsDetails.urlItem(file.getPath(), vehicalsDetails.TYPE_FILE, new CallBackViewChanger() {
            @Override
            public void setViewToPercentage(final AdCircleProgress loader, final TextView overlayView, final TextView markView) {
                new FileUploader().uploadFile(file.getPath(), carData.getData().getId(), vehicalView.this, new FileUploader.FileUploaderCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(vehicalView.this, "Some error occurred...", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinish(Response<MyResponse> response) {
                        Log.v("AhmedDakrory:", "Finish");
                        MyResponse response1 = response.body();
                        loader.setVisibility(View.GONE);
                        overlayView.setVisibility(View.GONE);
                        markView.setTextColor(vehicalView.this.getResources().getColor(R.color.colorGreenSign));

                        Toast.makeText(vehicalView.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgressUpdate(int currentpercent, int totalpercent) {

                        loader.setProgress(Float.parseFloat(String.valueOf(currentpercent)));
                        // Log.v("AhmedDakrory:", String.valueOf(currentpercent) + " / " + String.valueOf(totalpercent));
                    }
                }, typeForImageOrDoc);
            }
        }));


        adapterForImages.notifyItemInserted(adapterForImages.getItemCount() - 1);
        recyclerViewImages.scrollToPosition(adapterForImages.getItemCount() - 1);
    } else if (typeForImageOrDoc == Constants.TypeDocForServer) {
        carData.getDocs().add(new vehicalsDetails.urlItem(file.getPath(), vehicalsDetails.TYPE_FILE, new CallBackViewChanger() {
            @Override
            public void setViewToPercentage(final AdCircleProgress loader, final TextView overlayView, final TextView markView) {
                new FileUploader().uploadFile(file.getPath(), carData.getData().getId(), vehicalView.this, new FileUploader.FileUploaderCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(vehicalView.this, "Some error occurred...", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinish(Response<MyResponse> response) {
                        Log.v("AhmedDakrory:", "Finish");
                        MyResponse response1 = response.body();
                        loader.setVisibility(View.GONE);
                        overlayView.setVisibility(View.GONE);
                        markView.setTextColor(vehicalView.this.getResources().getColor(R.color.colorGreenSign));

                        Toast.makeText(vehicalView.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgressUpdate(int currentpercent, int totalpercent) {

                        loader.setProgress(Float.parseFloat(String.valueOf(currentpercent)));
                        //Log.v("AhmedDakrory:", String.valueOf(currentpercent) + " / " + String.valueOf(totalpercent));
                    }
                }, typeForImageOrDoc);
            }
        }));


        adapterForDocs.notifyItemInserted(adapterForDocs.getItemCount() - 1);
        recyclerViewDocs.scrollToPosition(adapterForDocs.getItemCount() - 1);
    }

}else{
    Toast.makeText(this,"Problem For Car",Toast.LENGTH_LONG).show();
}
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
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
                    //Log.v("AhmedDakrory","Code: "+requestCode);
                    if(list.get(0)!=null) {
                        File file = list.get(0);
                        uploadFileAndAddToAdapter(file,i);

                    }
                }



                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                    //Cancel handling, you might wanna remove taken photo if it was canceled
                    if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                        File photoFile = EasyImage.lastlyTakenButCanceledPhoto(vehicalView.this);
                        if (photoFile != null) photoFile.delete();
                    }
                }


            });

        }
    }

    public  Retrofit getClient(String vin) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_TO_GET_VIN_DATA+vin)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;
    }
}
