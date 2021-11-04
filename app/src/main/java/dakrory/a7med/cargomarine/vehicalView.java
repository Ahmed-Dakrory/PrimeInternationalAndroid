package dakrory.a7med.cargomarine;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adprogressbarlib.AdCircleProgress;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dakrory.a7med.cargomarine.CustomViews.CallBackViewChanger;
import dakrory.a7med.cargomarine.CustomViews.CustomAdapterForPersonModel;
import dakrory.a7med.cargomarine.CustomViews.vehicalImagesAdapter;
import dakrory.a7med.cargomarine.CustomViews.vehicalPdfsAdapter;
import dakrory.a7med.cargomarine.Models.userData;
import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.Models.vinDetails;
import dakrory.a7med.cargomarine.helpers.Api;
import dakrory.a7med.cargomarine.helpers.Constants;
import dakrory.a7med.cargomarine.helpers.FilePath;
import dakrory.a7med.cargomarine.helpers.FileUploader;
import dakrory.a7med.cargomarine.helpers.MyResponse;
import dakrory.a7med.cargomarine.helpers.modelsFunctions;
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


public class vehicalView extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    RecyclerView recyclerViewImages;
    RecyclerView recyclerViewDocs;
    RecyclerView recyclerViewPdfs;

    RelativeLayout layoutForImages;
    RelativeLayout layoutForDocs;
    RelativeLayout layoutForPdfs;




    FloatingActionButton addDamageFloatingButton;


    FloatingActionButton addImageFloatingButton;
    FloatingActionButton addDocFloatingButton;
    FloatingActionButton addPdfFloatingButton;

    FloatingActionButton saveAllNewResultsFloatingActionButton;

    vehicalImagesAdapter adapterForImages;
    vehicalImagesAdapter adapterForDocs;
    vehicalPdfsAdapter adapterForPdfs;

    Button imagesButton;
    Button DocsButton;
    Button PdfsButton;


    EditText vinEdit;
    EditText ModelEdit;
    EditText MakeEdit;
    EditText YearEdit;
    Spinner typeSelect;
    EditText descriptionEdit;
    EditText assemlyCountryEdit;
    EditText colorEdit;
    EditText engineLitersEdit;
    EditText bodyStyleEdit;
    EditText driverNameEdit;
    EditText driverPhoneEdit;
    EditText companyTransNameEdit;
    EditText engineTypeEdit;
    CheckBox keyExistEdit;


    EditText mainUserName;
    EditText main2UserName;
    Spinner shipperUserName;
    CustomAdapterForPersonModel shipperUserNameAdapter;
    EditText vendorUserName;
    EditText customerUserName;
    EditText consigneeUserName;


    LinearLayout layoutRelease;
    ImageButton setReleaseDateButton;
    TextView ReleaseDateTextView;

    CheckBox releaseStateCheckBox;
    Spinner releaseTypeSpinner;
    Spinner stateTypeSpinner;


    int idOfCar=-1;
    int Mode;
    private boolean AllowedToModify = false;
    static vehicalsDetails carData;


    //Add New Variables
    String vinNew;

    Retrofit retrofit = null;
    RelativeLayout loaderPanel;
    userData thisAccountUserData = LoginActivity.thisAccountCredData;



    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePadForDriver;
    private Button mClearButtonForDriver;
    private Button mSaveButtonForDriver;





    public TextView TimeStampForSigniture;
    public static TextView overlayViewDriverSign;
    public static AdCircleProgress loaderDriverSign;
    public ImageView imageDriverSignView;
    public static TextView markDriverSignView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_vehical_view);
        carData=new vehicalsDetails();
        carData.setData(new vehicalsDetails.carDetails());
        carData.setImages(new ArrayList<vehicalsDetails.urlItem>());
        carData.setDocs(new ArrayList<vehicalsDetails.urlItem>());
        carData.setPdfs(new ArrayList<vehicalsDetails.urlItem>());
        carData.setAllshippers(new ArrayList<vehicalsDetails.modelIdAndName>());


        defineViewsAndIntegrate();

        EasyImage.configuration(this)
                .setImagesFolderName("Cargo Marine") // images folder name, default is "EasyImage"
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(false); // if you want to use internal memory for storying images - default



        addDamageFloatingButton.setOnClickListener(this);
        addImageFloatingButton.setOnClickListener(this);
        addDocFloatingButton.setOnClickListener(this);
        addPdfFloatingButton.setOnClickListener(this);
        saveAllNewResultsFloatingActionButton.setOnClickListener(this);


        imagesButton.setOnClickListener(this);
        DocsButton.setOnClickListener(this);
        PdfsButton.setOnClickListener(this);

        setReleaseDateButton.setOnClickListener(this);

        setReleaseDateButton.setOnClickListener(this);
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


        setPersonDataAccess();

        if(thisAccountUserData.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_MAIN || thisAccountUserData.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_MAIN2){
            AllowedToModify = true;
        }else{
            AllowedToModify = false;
        }


        if(!AllowedToModify){
            setALLEditableFieldsAccess();

        }



        releaseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               setReleaseType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        stateTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setStateOfCar(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        releaseStateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                carData.getData().setReleaseOption(isChecked?1:0);
                setTextData(carData.getData());
                //addNewCarToServer();
            }
        });




        mSignaturePadForDriver.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            //    Toast.makeText(vehicalView.this, "You are Signing Now", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mSaveButtonForDriver.setEnabled(true);
                mClearButtonForDriver.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButtonForDriver.setEnabled(false);
                mClearButtonForDriver.setEnabled(false);
            }
        });


        mClearButtonForDriver.setOnClickListener(this);
        mSaveButtonForDriver.setOnClickListener(this);


    }

    private void setStateOfCar(int position) {
        carData.getData().setState(position);
       // addNewCarToServer();
    }

    private void setReleaseType(int position) {
        if(position == 0){
            carData.getData().setStateOut(-1);
        }else if(position == 1){
            carData.getData().setStateOut(1);

        }else if(position == 2){
            carData.getData().setStateOut(2);

        }else if(position == 3){
            carData.getData().setStateOut(3);

        }
        Log.v("AhmedDakrory",idOfCar+ "position: "+position+", setStateOut: "+carData.getData().getStateOut());
    }

    private void handleVinAndSetViewAfterRequestFromOutSide(final String vinNew) {
        Api apiInterface = getClient(vinNew+"/").create(Api.class);
        //creating a call and calling the upload image method
        Call<vinDetails> call = apiInterface.getCarDetailsfromVin();

        if(modelsFunctions.checkNetworkStatus(this)) {
            call.enqueue(new Callback<vinDetails>() {
                @Override
                public void onResponse(Call<vinDetails> call, Response<vinDetails> response) {
                    vinDetails car = response.body();
                    if (car != null) {
                        vehicalsDetails.carDetails dataOfCar = new vehicalsDetails.carDetails();
                        dataOfCar.setState(0);
                        dataOfCar.setUuid(vinNew);
                        dataOfCar.setMake(car.Results.get(0).Make);
                        dataOfCar.setModel(car.Results.get(0).Model);
                        dataOfCar.setYear(car.Results.get(0).ModelYear);
                        dataOfCar.setAssemlyCountry(car.Results.get(0).PlantCountry);
                        dataOfCar.setBodyStyle(car.Results.get(0).DriveType);
                        dataOfCar.setEngineLiters(car.Results.get(0).DisplacementL);
                        dataOfCar.setEngineType(car.Results.get(0).EngineConfiguration + "- " + car.Results.get(0).EngineCylinders + " Cylinders");


                        if (thisAccountUserData.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_MAIN) {
                            dataOfCar.setMainId(thisAccountUserData.getUserDetails().getMainUserId());
                        } else if (thisAccountUserData.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_MAIN2) {
                            dataOfCar.setMainId(thisAccountUserData.getUserDetails().getMainUserId());
                            dataOfCar.setMainTwoId(thisAccountUserData.getUserDetails().getMainTwoId());
                            Log.v("AhmedDakrory", "Done ya : " + dataOfCar.getMainTwoId());
                        }
                        carData.setData(dataOfCar);
                        setTextData(carData.getData());

                        addNewCarToServer();

                    }
                }

                @Override
                public void onFailure(Call<vinDetails> call, Throwable t) {

                    loaderPanel.setVisibility(View.GONE);
                    Toast.makeText(vehicalView.this, getString(R.string.ProblemErrorOnServerAddNewCar), Toast.LENGTH_LONG).show();
                    Log.v("AhmedDakroryTwo", t.toString());
                }
            });
        }else {

            Toast.makeText(this,R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
        }
    }

    private void addNewCarToServer() {

        Log.v("AhmedDakrory","Done ya : "+carData.getData().getMainTwoId());
        carData.getData().setUuid(vinEdit.getText().toString());
        carData.getData().setModel(ModelEdit.getText().toString());
        carData.getData().setMake(MakeEdit.getText().toString());
        carData.getData().setYear(YearEdit.getText().toString());
        carData.getData().setCarType(typeSelect.getSelectedItem().toString());
        carData.getData().setDescription(descriptionEdit.getText().toString());
        carData.getData().setAssemlyCountry(assemlyCountryEdit.getText().toString());
        carData.getData().setColor(colorEdit.getText().toString());
        carData.getData().setEngineLiters(engineLitersEdit.getText().toString());
        carData.getData().setBodyStyle(bodyStyleEdit.getText().toString());
        carData.getData().setDriverName(driverNameEdit.getText().toString());
        carData.getData().setDriverPhone(driverPhoneEdit.getText().toString());
        carData.getData().setCompanyTransName(companyTransNameEdit.getText().toString());
        carData.getData().setEngineType(engineTypeEdit.getText().toString());
        carData.getData().setKeyExist(keyExistEdit.isChecked());

        try {
            carData.getData().setShipperId(carData.getAllshippers().get(shipperUserName.getSelectedItemPosition()).getId());
        }catch (Exception ex){

        }




        loaderPanel.setVisibility(View.VISIBLE);

        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating our api
        Api api = retrofit.create(Api.class);

        //creating a call and calling the upload image method
        Call<vehicalsDetails> call = api.insertNewCar(carData.getData());

        if(modelsFunctions.checkNetworkStatus(this)) {
            call.enqueue(new Callback<vehicalsDetails>() {
                @Override
                public void onResponse(Call<vehicalsDetails> call, Response<vehicalsDetails> response) {
                    vehicalsDetails vDetails = response.body();

                    carData.setData(vDetails.getData());
                    carData.getImages().addAll(vDetails.getImages());
                    carData.getDocs().addAll(vDetails.getDocs());
                    carData.getPdfs().addAll(vDetails.getPdfs());

                    idOfCar = carData.getData().getId();
                    setTextData(carData.getData());

                    carData.getAllshippers().addAll(vDetails.getAllshippers());

                    loaderPanel.setVisibility(View.GONE);
                    Log.v("AhmedDakrory33", String.valueOf(vDetails.getData().getUuid() + ", " + vDetails.getData().getId() + ", " + vDetails.getData().getStateOut()));
                    adapterForImages.notifyDataSetChanged();
                    adapterForDocs.notifyDataSetChanged();
                    adapterForPdfs.notifyDataSetChanged();
                    shipperUserNameAdapter.notifyDataSetChanged();
                    carData.getData().setShipperId(carData.getAllshippers().get(shipperUserName.getSelectedItemPosition()).getId());
                }

                @Override
                public void onFailure(Call<vehicalsDetails> call, Throwable t) {
                    Toast.makeText(vehicalView.this, getString(R.string.ProblemErrorOnServerAddNewCar), Toast.LENGTH_LONG).show();
                    loaderPanel.setVisibility(View.GONE);
                    Log.v("AhmedDakrory", t.toString());
                }
            });
        }else{

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(vehicalView.this,R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
                    loaderPanel.setVisibility(View.GONE);
                }
            });
            }
    }

    private void setALLEditableFieldsAccess() {
        vinEdit.setKeyListener(null);
        ModelEdit.setKeyListener(null);
        MakeEdit.setKeyListener(null);
        YearEdit.setKeyListener(null);
        typeSelect.setSelection(0);
        descriptionEdit.setKeyListener(null);
        assemlyCountryEdit.setKeyListener(null);
        colorEdit.setKeyListener(null);
        engineLitersEdit.setKeyListener(null);
        bodyStyleEdit.setKeyListener(null);
        driverNameEdit.setKeyListener(null);
        driverPhoneEdit.setKeyListener(null);
        companyTransNameEdit.setKeyListener(null);
        engineTypeEdit.setKeyListener(null);
        keyExistEdit.setKeyListener(null);


        saveAllNewResultsFloatingActionButton.hide();
        addImageFloatingButton.hide();
        addDocFloatingButton.hide();
        addPdfFloatingButton.hide();

        releaseStateCheckBox.setEnabled(false);
        releaseTypeSpinner.setEnabled(false);
        setReleaseDateButton.setEnabled(false);
        stateTypeSpinner.setEnabled(false);
    }

        private void setPersonDataAccess() {
        //Persons
        mainUserName.setKeyListener(null);
        main2UserName.setKeyListener(null);
        vendorUserName.setKeyListener(null);
        customerUserName.setKeyListener(null);
        consigneeUserName.setKeyListener(null);
    }

    private void defineViewsAndIntegrate() {
        loaderPanel = (RelativeLayout)findViewById(R.id.loaderPanel);
        recyclerViewImages = (RecyclerView) findViewById(R.id.recyclerViewForImages);
        recyclerViewDocs = (RecyclerView) findViewById(R.id.recyclerViewForDocs);
        recyclerViewPdfs = (RecyclerView) findViewById(R.id.recyclerViewForPdfs);


        layoutForDocs = (RelativeLayout) findViewById(R.id.layoutForDocs);
        layoutForImages = (RelativeLayout) findViewById(R.id.layoutForImages);
        layoutForPdfs = (RelativeLayout) findViewById(R.id.layoutForPdfs);

        addDamageFloatingButton=(FloatingActionButton)findViewById(R.id.setAllDamages);
        addImageFloatingButton=(FloatingActionButton)findViewById(R.id.addImageFloating);
        addDocFloatingButton=(FloatingActionButton)findViewById(R.id.addDocFloating);
        addPdfFloatingButton=(FloatingActionButton)findViewById(R.id.addPdfFloating);
        saveAllNewResultsFloatingActionButton=(FloatingActionButton)findViewById(R.id.saveAllNewResults);


        imagesButton = (Button) findViewById(R.id.ImagesButton);
        DocsButton = (Button) findViewById(R.id.DocumentsButton);
        PdfsButton = (Button) findViewById(R.id.PdfsButton);


        vinEdit= (EditText)findViewById(R.id.vinEdit);
        ModelEdit= (EditText)findViewById(R.id.modelEdit);
        MakeEdit= (EditText)findViewById(R.id.makeEdit);
        YearEdit= (EditText)findViewById(R.id.yearEdit);
        typeSelect = (Spinner) findViewById(R.id.typeSelect);
        descriptionEdit= (EditText)findViewById(R.id.descriptionEdit);
        assemlyCountryEdit= (EditText)findViewById(R.id.assemlyCountryEdit);
        colorEdit= (EditText)findViewById(R.id.colorEdit);
        engineLitersEdit= (EditText)findViewById(R.id.engineLitersEdit);
        bodyStyleEdit= (EditText)findViewById(R.id.bodyStyleEdit);
        driverNameEdit= (EditText)findViewById(R.id.driverNameEdit);
        driverPhoneEdit= (EditText)findViewById(R.id.driverPhoneEdit);
        companyTransNameEdit= (EditText)findViewById(R.id.companyTransNameEdit);
        engineTypeEdit= (EditText)findViewById(R.id.engineTypeEdit);
        keyExistEdit = (CheckBox) findViewById(R.id.keyExistEdit);



        //Persons

        mainUserName= (EditText)findViewById(R.id.MainUserEdit);
        main2UserName= (EditText)findViewById(R.id.MainUser2Edit);
        shipperUserName= (Spinner) findViewById(R.id.shipperNameEdit);
        vendorUserName= (EditText)findViewById(R.id.vendorEdit);
        customerUserName= (EditText)findViewById(R.id.customerEdit);
        consigneeUserName= (EditText)findViewById(R.id.consigneeEdit);


        //Shipment state
        releaseStateCheckBox = (CheckBox)findViewById(R.id.releaseOption);
        releaseTypeSpinner = (Spinner)findViewById(R.id.spinnerReleaseState);
        stateTypeSpinner = (Spinner)findViewById(R.id.spinnerstates);

        ReleaseDateTextView = (TextView)findViewById(R.id.releaseDateTextView);
        setReleaseDateButton=(ImageButton)findViewById(R.id.setReleaseDate);
        layoutRelease = (LinearLayout)findViewById(R.id.layoutRelease);


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


        Log.v("AhmedDakrory","Size of Pdfs"+carData.getPdfs().size());
        adapterForPdfs = new vehicalPdfsAdapter(recyclerViewPdfs,carData.getPdfs(),vehicalView.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(vehicalView.this,4);
        recyclerViewPdfs.setHasFixedSize(true);
        recyclerViewPdfs.setLayoutManager(gridLayoutManager);
        recyclerViewPdfs.setAdapter(adapterForPdfs);



        shipperUserNameAdapter = new CustomAdapterForPersonModel(vehicalView.this,
                R.layout.listitems_names_layout, R.id.title, carData.getAllshippers());
        shipperUserName.setAdapter(shipperUserNameAdapter);



        //Define Driver Signiture
        mSignaturePadForDriver = (SignaturePad) findViewById(R.id.signature_padForDriverSign);
        mClearButtonForDriver = (Button) findViewById(R.id.clear_buttonForDriverSign);
        mSaveButtonForDriver = (Button) findViewById(R.id.save_buttonForDriverSign);




        overlayViewDriverSign = (TextView)findViewById(R.id.backgroundWhiteDriverSign);
        markDriverSignView = (TextView)findViewById(R.id.markDriverSign);
        loaderDriverSign = (AdCircleProgress) findViewById(R.id.donut_progressDriverSign);
        TimeStampForSigniture = (TextView) findViewById(R.id.TimeStampForSigniture);


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
        if(modelsFunctions.checkNetworkStatus(this)) {

            call.enqueue(new Callback<vehicalsDetails>() {
                @Override
                public void onResponse(Call<vehicalsDetails> call, Response<vehicalsDetails> response) {

                    vehicalsDetails car_data = response.body();
                    if (car_data != null) {

                        carData.setData(car_data.getData());

                        carData.getImages().addAll(car_data.getImages());
//                        Log.v("AhmedDakrory", "Size of Images" + carData.getImages().size());
//
//                        Log.v("AhmedTest",String.valueOf(car_data.getAllshippers().size()));
                        carData.getAllshippers().addAll(car_data.getAllshippers());
                        adapterForImages.notifyDataSetChanged();


                        carData.getDocs().addAll(car_data.getDocs());
//                        Log.v("AhmedDakrory", "Size of Images" + carData.getDocs().size());
                        adapterForDocs.notifyDataSetChanged();

                        carData.getPdfs().addAll(car_data.getPdfs());
                        Log.v("AhmedDakrory", "Size of Pdfs" + carData.getPdfs().size());
                        adapterForPdfs.notifyDataSetChanged();

                        setTextData(carData.getData());
                        loaderPanel.setVisibility(View.GONE);


                        shipperUserNameAdapter.notifyDataSetChanged();

                        for(int i=0;i<carData.getAllshippers().size();i++){
                            if(carData.getData().getShipperId()==carData.getAllshippers().get(i).getId()){
                                shipperUserName.setSelection(i);

                                return;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<vehicalsDetails> call, Throwable t) {

                    loaderPanel.setVisibility(View.GONE);
                    Log.v("AhmedTest",String.valueOf(t.getMessage()));
                    Toast.makeText(vehicalView.this, getString(R.string.ProblemOnHandleData), Toast.LENGTH_LONG).show();
                }
            });
        }else{

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(vehicalView.this,R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
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

    private void setTextData(vehicalsDetails.carDetails data) {

         vinEdit.setText(String.valueOf(data.getUuid()));
         ModelEdit.setText(String.valueOf(data.getModel()));
         MakeEdit.setText(String.valueOf(data.getMake()));
         YearEdit.setText(String.valueOf(data.getYear()));
         typeSelect.setSelection(getIndex(typeSelect,String.valueOf(data.getCarType())));
         descriptionEdit.setText(String.valueOf(data.getDescription()));
         assemlyCountryEdit.setText(String.valueOf(data.getAssemlyCountry()));
         colorEdit.setText(String.valueOf(data.getColor()));
         engineLitersEdit.setText(String.valueOf(data.getEngineLiters()));
        bodyStyleEdit.setText(String.valueOf(data.getBodyStyle()));
        driverNameEdit.setText(String.valueOf(data.getDriverName()));
        driverPhoneEdit.setText(String.valueOf(data.getDriverPhone()));
        companyTransNameEdit.setText(String.valueOf(data.getCompanyTransName()));
        engineTypeEdit.setText(String.valueOf(data.getEngineType()));
        keyExistEdit.setChecked(data.isKeyExist());


        try{
            Log.v("AhmedDakrory","Image Loading");
            String imageDriverSign = Constants.ImageBaseUrl +data.getUrlOfDriverSignture();
//            final URL url = new URL(imageDriverSign);
            TimeStampForSigniture.setText(data.getDateOfDriverSignture());

            String[] urls = {imageDriverSign};
            new RetrieveFeedTask().execute(urls);





        }catch (Exception ex){

            Log.v("AhmedDakrory","Error Image Loading"+ex.toString());
        }
        setTextUsernameToField(mainUserName,data.getUserfirstName(),data.getUserlastName());
        setTextUsernameToField(main2UserName,data.getMainTwofirstName(),data.getMainTwolastName());


        setTextUsernameToField(vendorUserName,data.getVendorfirstName(),data.getVendorlastName());
        setTextUsernameToField(customerUserName,data.getCustomerfirstName(),data.getCustomerlastName());
        setTextUsernameToField(consigneeUserName,data.getConsigneefirstName(),data.getConsigneelastName());


        setCheckedRelease(data.getReleaseOption()==1?true:false);


        stateSpinnerSetState(data.getState());
        releaseSpinnerSetType(data.getStateOut());

        setDateRelease(data.getReleaseDate());



    }



    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {



                try  {
                    //Your code goes here
                    final URL url = new URL(urls[0]);
                    final Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                try {
                                    mSignaturePadForDriver.setSignatureBitmap(image);
                                    Log.v("AhmedDakrorySig", String.valueOf("Done"));
                                }catch (Exception ex){

                                }
                            }
                    });
                } catch (Exception e) {
                    Log.v("AhmedDakrorySig",String.valueOf(e.getMessage()));
                    Log.v("AhmedDakrorySig",String.valueOf(e));
                    e.printStackTrace();
                }

                return "";

        }

        protected void onPostExecute(Void feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }


    private void setDateRelease(String releaseDate) {

            if(releaseDate != null) {
                if (!releaseDate.equalsIgnoreCase("")) {
                    //Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(releaseDate);

                    ReleaseDateTextView.setText(releaseDate);
                }
            }

    }

    private void setCheckedRelease(boolean b) {
        releaseStateCheckBox.setChecked(b);
        if(b) {
            releaseTypeSpinner.setVisibility(View.VISIBLE);
            layoutRelease.setVisibility(View.VISIBLE);
        }else{

            releaseTypeSpinner.setVisibility(View.GONE);
            layoutRelease.setVisibility(View.INVISIBLE);
        }

    }

    private void stateSpinnerSetState(int state) {

            stateTypeSpinner.setSelection(state);

    }

    private void releaseSpinnerSetType(int state) {
        if(state == -1){
            releaseTypeSpinner.setSelection(0);
        }else if(state == 1){
            releaseTypeSpinner.setSelection(1);

        }else if(state == 2){
            releaseTypeSpinner.setSelection(2);

        }else if(state == 3){
            releaseTypeSpinner.setSelection(3);

        }
    }

    public void setTextUsernameToField(EditText field,String firstName,String lastName){
        if(firstName!=null ||firstName.equalsIgnoreCase("")){

                field.setText(String.valueOf(firstName+" "+lastName));

        }else{
            field.setText(String.valueOf("Not Set!!!"));
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

    DialogInterface.OnClickListener dialogDocClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    captureImage(Constants.TypeDocForServer);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    storageImage(Constants.TypeDocForServer);
                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ImagesButton){
            //ShowImages
            layoutForImages.setVisibility(View.VISIBLE);
            layoutForDocs.setVisibility(View.GONE);
            layoutForPdfs.setVisibility(View.GONE);
        }else if(v.getId()==R.id.DocumentsButton){
            layoutForImages.setVisibility(View.GONE);
            layoutForDocs.setVisibility(View.VISIBLE);
            layoutForPdfs.setVisibility(View.GONE);
        }else if(v.getId()==R.id.PdfsButton){
            layoutForImages.setVisibility(View.GONE);
            layoutForDocs.setVisibility(View.GONE);
            layoutForPdfs.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.addImageFloating){
            Log.v("AhmedDakrory","ButtonImage");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("From Camera or Storage?").setPositiveButton("Camera", dialogImageClickListener)
                    .setNegativeButton("Storage", dialogImageClickListener).show();
        }else if(v.getId()==R.id.setAllDamages){
            Log.v("AhmedDakrory","ButtonImage");
            Intent openDamage = new Intent(vehicalView.this,damageActivity.class);
            openDamage.putExtra("TYPE",typeSelect.getSelectedItem().toString());
            startActivity(openDamage);
        }else if(v.getId()==R.id.addDocFloating){
            Log.v("AhmedDakrory","ButtonDoc");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("From Camera or Storage?").setPositiveButton("Camera", dialogDocClickListener)
                    .setNegativeButton("Storage", dialogDocClickListener).show();

        }else if(v.getId()==R.id.addPdfFloating){
            Log.v("AhmedDakrory","ButtonDoc");
           // captureImage(Constants.TypePdfForServer);
            storagePdf(Constants.TypePdfForServer);
        }else if(v.getId()==R.id.saveAllNewResults){
            addNewCarToServer();
        }else if(v.getId()==R.id.setReleaseDate){
            selectDateToRelease();
        }else if(v.getId()==R.id.save_buttonForDriverSign){
            Bitmap signatureBitmap = mSignaturePadForDriver.getSignatureBitmap();

            final File file = bitmapToFile(vehicalView.this,signatureBitmap,"tempSigniture");

            uploadFileAndAddToAdapter(file,Constants.TypeSignitureForDriverForServer);

            Toast.makeText(vehicalView.this, "Signature saved", Toast.LENGTH_SHORT).show();
        }else if(v.getId()==R.id.clear_buttonForDriverSign){
            mSignaturePadForDriver.clear();
        }
    }


    public static File bitmapToFile(Context context, Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        File f3=new File("/sdcard/Download/"+fileNameToSave+"/");
        if(!f3.exists())
            f3.mkdirs();
        OutputStream outStream = null;
        Log.v("AhmedDakrory",f3.getPath());
        File file = new File("/sdcard/Download/"+fileNameToSave+"/seconds"+".jpeg");
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.close();

            Log.v("AhmedDakrory","File maked2");
            return file;
//            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();

            Log.v("AhmedDakrory","Error File maked");
            Log.v("AhmedDakrory",e.getMessage());

            Log.v("AhmedDakrory",e.toString());
            return file;
        }
    }

    private void selectDateToRelease() {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = DatePickerDialog.newInstance(
                vehicalView.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dpd.show(getFragmentManager(), "Release Date");
    }

    private void storagePdf(int typeForPdf) {

        if(ContextCompat.checkSelfPermission(vehicalView.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){


            ActivityCompat.requestPermissions(vehicalView.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        }else {
            String[] mimeTypes =
                    { "application/pdf"};

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                if (mimeTypes.length > 0) {
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
            } else {
                String mimeTypesStr = "";
                for (String mimeType : mimeTypes) {
                    mimeTypesStr += mimeType + "|";
                }
                intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
            }
            startActivityForResult(Intent.createChooser(intent,"ChooseFile"), typeForPdf);
            Log.v("AhmedDakrory","Type1: "+typeForPdf);

        }
    }

    private void storageImage(int typeForImageOrDoc) {

        if(ContextCompat.checkSelfPermission(vehicalView.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){


            ActivityCompat.requestPermissions(vehicalView.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        }else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
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
    public  void uploadFileAndAddToAdapter(final File file, final int typeForImageOrDoc) {
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
    }

    else if (typeForImageOrDoc == Constants.TypeCrashPointsForServer) {

        Log.v("AhmedDakrory", typeForImageOrDoc + " :Ok");
        CallBackViewChanger viewChanger1 =   new CallBackViewChanger() {
            @Override
            public void setViewToPercentage(final AdCircleProgress loader, final TextView overlayView, final TextView markView) {




                new FileUploader().uploadCrashImage(carData.getData().getCrashPointsJson(),file.getPath(), carData.getData().getId(), vehicalView.this, new FileUploader.FileUploaderCallback() {
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
                        file.delete();
                        Toast.makeText(vehicalView.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgressUpdate(int currentpercent, int totalpercent) {
                        loader.setVisibility(View.VISIBLE);
                        overlayView.setVisibility(View.VISIBLE);
                        loader.setProgress(Float.parseFloat(String.valueOf(currentpercent)));
                        Log.v("AhmedDakrory:", String.valueOf(currentpercent) + " / " + String.valueOf(totalpercent));
                    }
                });
            }
        };

        viewChanger1.setViewToPercentage(loaderDriverSign,overlayViewDriverSign,markDriverSignView);

        //Update Map
    }


    else if (typeForImageOrDoc == Constants.TypeSignitureForDriverForServer) {

        Log.v("AhmedDakrory", typeForImageOrDoc + " :Ok");
        CallBackViewChanger viewChanger1 =   new CallBackViewChanger() {
            @Override
            public void setViewToPercentage(final AdCircleProgress loader, final TextView overlayView, final TextView markView) {




                new FileUploader().uploadSignitureOfDriver(file.getPath(), carData.getData().getId(), vehicalView.this, new FileUploader.FileUploaderCallback() {
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
                        file.delete();
                        Toast.makeText(vehicalView.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgressUpdate(int currentpercent, int totalpercent) {
                        loader.setVisibility(View.VISIBLE);
                        overlayView.setVisibility(View.VISIBLE);
                        loader.setProgress(Float.parseFloat(String.valueOf(currentpercent)));
                        Log.v("AhmedDakrory:", String.valueOf(currentpercent) + " / " + String.valueOf(totalpercent));
                    }
                });
            }
        };

        viewChanger1.setViewToPercentage(loaderDriverSign,overlayViewDriverSign,markDriverSignView);

        //Update Map
    }else if (typeForImageOrDoc == Constants.TypeDocForServer) {
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


    else if (typeForImageOrDoc == Constants.TypePdfForServer) {
        carData.getPdfs().add(new vehicalsDetails.urlItem(file.getPath(), vehicalsDetails.TYPE_FILE, new CallBackViewChanger() {
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


        adapterForPdfs.notifyItemInserted(adapterForPdfs.getItemCount() - 1);
        recyclerViewPdfs.scrollToPosition(adapterForPdfs.getItemCount() - 1);
    }

    }else{
        Toast.makeText(this,"Problem For Car",Toast.LENGTH_LONG).show();
    }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("AhmedDakrory","Returned");
        if(requestCode == Constants.TypeDocForServer){
            if(resultCode == RESULT_OK) {
                final Uri imageUri = data.getData();
                String selectedFilePath = FilePath.getPath(this, imageUri);
                final File file = new File(selectedFilePath);

                Log.v("AhmedDakrory", "Type: "+Constants.TypeDocForServer);
                uploadFileAndAddToAdapter(file,Constants.TypeDocForServer);
                Log.v("AhmedDakrory", imageUri.getPath());
            }
        }
        else if(requestCode == Constants.TypeImageForServer){
            if(resultCode == RESULT_OK) {
                final Uri imageUri = data.getData();
                String selectedFilePath = FilePath.getPath(this, imageUri);
                final File file = new File(selectedFilePath);
                Log.v("AhmedDakrory", "Type: "+Constants.TypeImageForServer);
                uploadFileAndAddToAdapter(file,Constants.TypeImageForServer);
                Log.v("AhmedDakrory", imageUri.getPath());
            }
        }
        else if(requestCode == Constants.TypePdfForServer){
            Log.v("AhmedDakrory", "Type: "+Constants.TypePdfForServer);
            if(resultCode == RESULT_OK) {
                final Uri imageUri = data.getData();
                String selectedFilePath = FilePath.getPath(this, imageUri);
                final File file = new File(selectedFilePath);
                Log.v("AhmedDakrory", "Type: "+Constants.TypePdfForServer);
                uploadFileAndAddToAdapter(file,Constants.TypePdfForServer);
                Log.v("AhmedDakrory", imageUri.getPath());
            }
        }
        else if(requestCode!=100) {
            Log.v("AhmedDakrory", "Ok");

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


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        carData.getData().setReleaseDate(format1.format(date));
        setTextData(carData.getData());
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
}
