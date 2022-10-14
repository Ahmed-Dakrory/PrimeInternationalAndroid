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
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dakrory.a7med.cargomarine.CustomViews.CallBackViewChanger;
import dakrory.a7med.cargomarine.CustomViews.CustomAdapterForPersonModel;
import dakrory.a7med.cargomarine.CustomViews.vehical3DImagesAdapter;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class vehicalView extends Activity implements View.OnClickListener,View.OnTouchListener, DatePickerDialog.OnDateSetListener {


    RecyclerView recyclerViewImages;
    RecyclerView recyclerViewDocs;
    RecyclerView recyclerViewPdfs;
    RecyclerView recyclerView3DImages;


    int mCurrentImageIndex=0;
    List<Bitmap> animBitmaps;
    int numberOfImages;

    dialogWithProgress mainDialog;

    RelativeLayout layoutForImages;
    RelativeLayout layoutForDocs;
    RelativeLayout layoutForPdfs;
    RelativeLayout layoutFor3DImages;
    RelativeLayout layoutFor3DExteriorImages;


    ImageView imageExterior;


    FloatingActionButton addDamageFloatingButton;
    FloatingActionButton add3DFloatingButton;

    FloatingActionButton addImageFloatingButton;
    FloatingActionButton addDocFloatingButton;
    FloatingActionButton addPdfFloatingButton;
    FloatingActionButton setExteriorImage;
    FloatingActionButton beforeViewOfEXT;
    FloatingActionButton nextViewOFExt;

    FloatingActionButton saveAllNewResultsFloatingActionButton;

    vehicalImagesAdapter adapterForImages;
    vehicalImagesAdapter adapterForDocs;
    vehicalPdfsAdapter adapterForPdfs;
    vehical3DImagesAdapter adapterFor3Dimages;


    Button imagesButton;
    Button DocsButton;
    Button PdfsButton;
    Button Images3DButton;
    Button Images3DExteriorButton;



    EditText vinEdit;
    EditText ModelEdit;
    EditText MakeEdit;
    EditText YearEdit;
    Spinner typeSelect;
    Spinner numberOfKeys;
    EditText descriptionEdit;
    EditText assemlyCountryEdit;
    EditText colorEdit;
    EditText weightEdit;
    EditText engineLitersEdit;
    EditText bodyStyleEdit;
    EditText driverNameEdit;
    EditText driverPhoneEdit;
    EditText companyTransNameEdit;
    EditText engineTypeEdit;
    CheckBox keyExistEdit;
    CheckBox titleExistEdit;


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


    ProgressBar loaderOfExteriorImage;


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
        carData.setImages3D(new ArrayList<vehicalsDetails.urlItem>());
        carData.setAllshippers(new ArrayList<vehicalsDetails.modelIdAndName>());


        defineViewsAndIntegrate();


        addDamageFloatingButton.setOnClickListener(this);
        add3DFloatingButton.setOnClickListener(this);
        addImageFloatingButton.setOnClickListener(this);
        setExteriorImage.setOnClickListener(this);
        beforeViewOfEXT.setOnTouchListener(this);
        nextViewOFExt.setOnTouchListener(this);
        addDocFloatingButton.setOnClickListener(this);
        addPdfFloatingButton.setOnClickListener(this);
        saveAllNewResultsFloatingActionButton.setOnClickListener(this);


        imagesButton.setOnClickListener(this);
        DocsButton.setOnClickListener(this);
        PdfsButton.setOnClickListener(this);
        Images3DButton.setOnClickListener(this);
        Images3DExteriorButton.setOnClickListener(this);

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
                        String weight = "";
                        try {
                            weight = car.Results.get(0).GVWR;
                            weight = weight.substring(weight.lastIndexOf("("));

                        }catch(Error err) {

                        }catch(Exception exc) {

                        }
                        dataOfCar.setWeight(weight);
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
        carData.getData().setNumberOfKeys(numberOfKeys.getSelectedItemPosition());
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
        carData.getData().setTitleExist(titleExistEdit.isChecked());
        carData.getData().setWeight(weightEdit.getText().toString());

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
                    carData.getImages3D().addAll(vDetails.getImages3D());

                    idOfCar = carData.getData().getId();
                    setTextData(carData.getData());

                    carData.getAllshippers().addAll(vDetails.getAllshippers());

                    loaderPanel.setVisibility(View.GONE);
                    Log.v("AhmedDakrory33", String.valueOf(vDetails.getData().getUuid() + ", " + vDetails.getData().getId() + ", " + vDetails.getData().getStateOut()));
                    adapterForImages.notifyDataSetChanged();
                    adapterForDocs.notifyDataSetChanged();
                    adapterForPdfs.notifyDataSetChanged();
                    adapterFor3Dimages.notifyDataSetChanged();

                    shipperUserNameAdapter.notifyDataSetChanged();
                    try {
                        carData.getData().setShipperId(carData.getAllshippers().get(shipperUserName.getSelectedItemPosition()).getId());
                    }catch (Exception ex){

                    }
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
        numberOfKeys.setSelection(0);
        descriptionEdit.setKeyListener(null);
        assemlyCountryEdit.setKeyListener(null);
        colorEdit.setKeyListener(null);
        weightEdit.setKeyListener(null);
        engineLitersEdit.setKeyListener(null);
        bodyStyleEdit.setKeyListener(null);
        driverNameEdit.setKeyListener(null);
        driverPhoneEdit.setKeyListener(null);
        companyTransNameEdit.setKeyListener(null);
        engineTypeEdit.setKeyListener(null);
        keyExistEdit.setKeyListener(null);
        titleExistEdit.setKeyListener(null);


        nextViewOFExt.hide();
        beforeViewOfEXT.hide();
        saveAllNewResultsFloatingActionButton.hide();
        addImageFloatingButton.hide();
        setExteriorImage.hide();
        nextViewOFExt.hide();
        beforeViewOfEXT.hide();
        addDocFloatingButton.hide();
        addPdfFloatingButton.hide();
        add3DFloatingButton.hide();

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
        recyclerView3DImages = (RecyclerView) findViewById(R.id.recyclerViewFor3DImages);

        layoutForDocs = (RelativeLayout) findViewById(R.id.layoutForDocs);
        layoutForImages = (RelativeLayout) findViewById(R.id.layoutForImages);
        layoutForPdfs = (RelativeLayout) findViewById(R.id.layoutForPdfs);
        layoutFor3DImages = (RelativeLayout) findViewById(R.id.layoutFor3DImages);
        layoutFor3DExteriorImages = (RelativeLayout) findViewById(R.id.layoutFor3DExteriorImages);


        addDamageFloatingButton=(FloatingActionButton)findViewById(R.id.setAllDamages);
        addImageFloatingButton=(FloatingActionButton)findViewById(R.id.addImageFloating);
        setExteriorImage = (FloatingActionButton)findViewById(R.id.setExteriorImage);
        nextViewOFExt = (FloatingActionButton)findViewById(R.id.nextViewOFExt);
        beforeViewOfEXT = (FloatingActionButton)findViewById(R.id.beforeViewOfEXT);
        addDocFloatingButton=(FloatingActionButton)findViewById(R.id.addDocFloating);
        addPdfFloatingButton=(FloatingActionButton)findViewById(R.id.addPdfFloating);
        add3DFloatingButton=(FloatingActionButton)findViewById(R.id.setAll3D);
        saveAllNewResultsFloatingActionButton=(FloatingActionButton)findViewById(R.id.saveAllNewResults);


        imageExterior = (ImageView) findViewById(R.id.imageExterior);

        imagesButton = (Button) findViewById(R.id.ImagesButton);
        DocsButton = (Button) findViewById(R.id.DocumentsButton);
        PdfsButton = (Button) findViewById(R.id.PdfsButton);
        Images3DButton = (Button) findViewById(R.id.threeDImagesButton);
        Images3DExteriorButton = (Button) findViewById(R.id.Images3DExteriorButton);


        vinEdit= (EditText)findViewById(R.id.vinEdit);
        ModelEdit= (EditText)findViewById(R.id.modelEdit);
        MakeEdit= (EditText)findViewById(R.id.makeEdit);
        YearEdit= (EditText)findViewById(R.id.yearEdit);
        typeSelect = (Spinner) findViewById(R.id.typeSelect);
        numberOfKeys = (Spinner) findViewById(R.id.numberOfKeys);
        descriptionEdit= (EditText)findViewById(R.id.descriptionEdit);
        assemlyCountryEdit= (EditText)findViewById(R.id.assemlyCountryEdit);
        colorEdit= (EditText)findViewById(R.id.colorEdit);
        weightEdit = (EditText)findViewById(R.id.weightEdit);
        engineLitersEdit= (EditText)findViewById(R.id.engineLitersEdit);
        bodyStyleEdit= (EditText)findViewById(R.id.bodyStyleEdit);
        driverNameEdit= (EditText)findViewById(R.id.driverNameEdit);
        driverPhoneEdit= (EditText)findViewById(R.id.driverPhoneEdit);
        companyTransNameEdit= (EditText)findViewById(R.id.companyTransNameEdit);
        engineTypeEdit= (EditText)findViewById(R.id.engineTypeEdit);
        keyExistEdit = (CheckBox) findViewById(R.id.keyExistEdit);
        titleExistEdit = (CheckBox) findViewById(R.id.titleExistEdit);



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
        adapterFor3Dimages = new vehical3DImagesAdapter(recyclerView3DImages,carData.getImages3D(),vehicalView.this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(vehicalView.this,4);
        recyclerViewPdfs.setHasFixedSize(true);
        recyclerViewPdfs.setLayoutManager(gridLayoutManager);
        recyclerViewPdfs.setAdapter(adapterForPdfs);



        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(vehicalView.this,4);
        recyclerView3DImages.setHasFixedSize(true);
        recyclerView3DImages.setLayoutManager(gridLayoutManager2);
        recyclerView3DImages.setAdapter(adapterFor3Dimages);

        List<vehicalsDetails.modelIdAndName> allWithNull=carData.getAllshippers();
        Log.v("AhmedDakrory1",String.valueOf(allWithNull.size()));
        allWithNull.add(0,new vehicalsDetails.modelIdAndName("No One Selected",-1));
        carData.setAllshippers(allWithNull);
        Log.v("AhmedDakrory2",String.valueOf(allWithNull.size()));
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


        loaderOfExteriorImage = (ProgressBar) findViewById(R.id.loaderOfExteriorImage);


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


                        setImageExteriorToHolder(carData.getData().getExteriorImg());

                        carData.getDocs().addAll(car_data.getDocs());
//                        Log.v("AhmedDakrory", "Size of Images" + carData.getDocs().size());
                        adapterForDocs.notifyDataSetChanged();

                        carData.getPdfs().addAll(car_data.getPdfs());
                        Log.v("AhmedDakrory", "Size of Pdfs" + carData.getPdfs().size());
                        adapterForPdfs.notifyDataSetChanged();


                        carData.getImages3D().addAll(car_data.getImages3D());
                        Log.v("AhmedDakrory", "Size of 3D" + carData.getImages3D().size());
                        adapterFor3Dimages.notifyDataSetChanged();

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

    private void setImageExteriorToHolder(String exteriorImg) {

        Log.v("AhmedDakrory","DataStartHere");
//        Log.v("AhmedDakrory",exteriorImg);
        if(exteriorImg!=null && !exteriorImg.equals("")) {
            loaderOfExteriorImage.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.v("AhmedDakrory","ShowLoader");
                    URL url = null;
                    try {
                        url = new URL(Constants.ImageBaseUrl + exteriorImg);
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        Log.v("AhmedDakrory","NumOld3"+String.valueOf(exteriorImg));
                        String[] aar = exteriorImg.split("_");

                        String num = aar[1].substring(1, aar[1].length());
                        Log.v("AhmedDakrory","Num"+String.valueOf(num));
                        numberOfImages = Integer.valueOf(num);
                        int bitmapWidth = bmp.getWidth() / numberOfImages;
                        int bitmapHeight = bmp.getHeight();

//list which holds individual bitmaps.
                        animBitmaps = new ArrayList<>(numberOfImages);


                        //split your bitmap in to individual bitmaps
                        for (int index = 0; index < numberOfImages; index++) {
                            animBitmaps.add(Bitmap.createBitmap(bmp, index * bitmapWidth, 0, bitmapWidth, bitmapHeight));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageExterior.setImageBitmap(animBitmaps.get(mCurrentImageIndex));


                                nextViewOFExt.show();
                                beforeViewOfEXT.show();

                                loaderOfExteriorImage.setVisibility(View.GONE);
                            }
                        });


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        }else{
            Log.v("AhmedDakrory","GONEIMAGEEX");
            loaderOfExteriorImage.setVisibility(View.GONE);
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
        numberOfKeys.setSelection(data.getNumberOfKeys());
         descriptionEdit.setText(String.valueOf(data.getDescription()));
         assemlyCountryEdit.setText(String.valueOf(data.getAssemlyCountry()));
         colorEdit.setText(String.valueOf(data.getColor()));
        weightEdit.setText(String.valueOf(data.getWeight()));
         engineLitersEdit.setText(String.valueOf(data.getEngineLiters()));
        bodyStyleEdit.setText(String.valueOf(data.getBodyStyle()));
        driverNameEdit.setText(String.valueOf(data.getDriverName()));
        driverPhoneEdit.setText(String.valueOf(data.getDriverPhone()));
        companyTransNameEdit.setText(String.valueOf(data.getCompanyTransName()));
        engineTypeEdit.setText(String.valueOf(data.getEngineType()));
        keyExistEdit.setChecked(data.isKeyExist());
        titleExistEdit.setChecked(data.isTitleExist());


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

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if(v.getId()==R.id.nextViewOFExt){

            goNextForBitmapExterior();
        }else if(v.getId()==R.id.beforeViewOfEXT){
            goBeforeForBitmapExterior();
        }
        return false;
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
            layoutFor3DExteriorImages.setVisibility(View.GONE);
            layoutFor3DImages.setVisibility(View.GONE);
        }else if(v.getId()==R.id.DocumentsButton){
            layoutForImages.setVisibility(View.GONE);
            layoutForDocs.setVisibility(View.VISIBLE);
            layoutForPdfs.setVisibility(View.GONE);
            layoutFor3DExteriorImages.setVisibility(View.GONE);
            layoutFor3DImages.setVisibility(View.GONE);
        }else if(v.getId()==R.id.PdfsButton){
            layoutForImages.setVisibility(View.GONE);
            layoutForDocs.setVisibility(View.GONE);
            layoutForPdfs.setVisibility(View.VISIBLE);
            layoutFor3DExteriorImages.setVisibility(View.GONE);
            layoutFor3DImages.setVisibility(View.GONE);
        }else if(v.getId()==R.id.threeDImagesButton){
            layoutForImages.setVisibility(View.GONE);
            layoutForDocs.setVisibility(View.GONE);
            layoutForPdfs.setVisibility(View.GONE);
            layoutFor3DExteriorImages.setVisibility(View.GONE);
            layoutFor3DImages.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.Images3DExteriorButton){
            layoutForImages.setVisibility(View.GONE);
            layoutForDocs.setVisibility(View.GONE);
            layoutForPdfs.setVisibility(View.GONE);
            layoutFor3DImages.setVisibility(View.GONE);
            layoutFor3DExteriorImages.setVisibility(View.VISIBLE);
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
        }else if(v.getId()==R.id.setExteriorImage){
            storage3DExteriorImage(Constants.Type3DExteriorForServer);
        }else if(v.getId()==R.id.nextViewOFExt){
            
            goNextForBitmapExterior();
        }else if(v.getId()==R.id.beforeViewOfEXT){
            goBeforeForBitmapExterior();
        }else if(v.getId()==R.id.setAll3D){
            Log.v("AhmedDakrory","ButtonImage");
            storage3D(Constants.Type3DForServer);

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

    private void goNextForBitmapExterior() {
        if(animBitmaps!=null) {
            mCurrentImageIndex++;
            //if current index is greater than the number of images, reset it to 0
            if (mCurrentImageIndex > numberOfImages - 1) {
                mCurrentImageIndex = 0;
            }
            imageExterior.setImageBitmap(animBitmaps.get(mCurrentImageIndex));
        }
    }

    private void goBeforeForBitmapExterior() {
    if(animBitmaps!=null) {
        mCurrentImageIndex--;
        //if current index is greater than the number of images, reset it to 0
        if (mCurrentImageIndex < 0) {
            mCurrentImageIndex = numberOfImages-1;
        }
        imageExterior.setImageBitmap(animBitmaps.get(mCurrentImageIndex));
    }
    }

    private void storage3DExteriorImage(int type3DExteriorForServer) {


            if(ContextCompat.checkSelfPermission(vehicalView.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){


                ActivityCompat.requestPermissions(vehicalView.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        3);
            }else {
                Intent open3D = new Intent(vehicalView.this,exteriorCam.class);
                open3D.putExtra("Data",carData.getData().getUuid().toString());
                startActivityForResult(open3D, type3DExteriorForServer);
            }
        }




        public static File bitmapToFile(Context context, Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        File f3=new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)+"/"+fileNameToSave+"/");
        if(!f3.exists())
            f3.mkdirs();
        OutputStream outStream = null;
        Log.v("AhmedDakrory",f3.getPath());
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)+"/"+fileNameToSave+"/seconds"+".jpeg");

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
            photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            photoPickerIntent.putExtra("Type_Of_Return","SELECT_INTERNAL");
            Log.v("AhmedDakrory","Type1: "+typeForImageOrDoc);
            startActivityForResult(photoPickerIntent, typeForImageOrDoc);
        }
    }



    private void storage3D(int typeFor3D) {

        if(ContextCompat.checkSelfPermission(vehicalView.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){


            ActivityCompat.requestPermissions(vehicalView.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        }else {
            Intent open3D = new Intent(vehicalView.this,allImagesActivity.class);
            open3D.putExtra("Data",carData.getData().getUuid().toString());
            startActivityForResult(open3D, typeFor3D);
        }
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }
    private void captureImage(int typeForImageOrDoc) {


        if(ContextCompat.checkSelfPermission(vehicalView.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){


            ActivityCompat.requestPermissions(vehicalView.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        }else {
            Intent openImageOrDoc= new Intent(vehicalView.this,multiple_capture.class);
            openImageOrDoc.putExtra("Data",carData.getData().getUuid().toString());
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
        new FileUploader().uploadFile(new File(dir, children[i]).getPath(), carData.getData().getId(), vehicalView.this, new FileUploader.FileUploaderCallback() {
            @Override
            public void onError(Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mainDialog.dialog.dismiss();
                    }
                });
                Log.v("AhmedDakrory","Error: "+String.valueOf(t.toString()));
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
                                    mainDialog.setPercentage((int) ((((i*100 /(children.length-1)) ))));


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
                    Toast.makeText(vehicalView.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onProgressUpdate(int currentpercent, int totalpercent) {
//                        Log.v("AhmedDakrory","Pers: "+String.valueOf(currentpercent));

            }
        },typeForImageOrDoc);


    }


    public  void uploadFileAndAddToAdapter(final File file, final int typeForImageOrDoc) {
        if(carData.getData().getId()!=0) {
    Log.v("AhmedDakrory", typeForImageOrDoc + " :Ok");
    if (typeForImageOrDoc == Constants.TypeImageForServer) {

        Log.v("AhmedDakrory", typeForImageOrDoc + " :Ok 3");
        Log.v("AhmedDakrory", carData.getData().getId() + " :Ok 3");

        boolean end_image = false;
        mainDialog=new vehicalView.dialogWithProgress(vehicalView.this);
        mainDialog.dialog.show();
        mainDialog.setPercentage(0);
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator +"nycargoCarMainImages"+ File.separator +carData.getData().getUuid());
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            upload_file_number(dir,children,0,typeForImageOrDoc);

        }



//
//        carData.getImages().add(new vehicalsDetails.urlItem(file.getPath(), vehicalsDetails.TYPE_FILE, new CallBackViewChanger() {
//            @Override
//            public void setViewToPercentage(final AdCircleProgress loader, final TextView overlayView, final TextView markView) {
//                new FileUploader().uploadFile(file.getPath(), carData.getData().getId(), vehicalView.this, new FileUploader.FileUploaderCallback() {
//                    @Override
//                    public void onError(Throwable t) {
//                        Toast.makeText(vehicalView.this, "Some error occurred...", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFinish(Response<MyResponse> response) {
//                        Log.v("AhmedDakrory:", "Finish");
//                        MyResponse response1 = response.body();
//                        loader.setVisibility(View.GONE);
//                        overlayView.setVisibility(View.GONE);
//                        markView.setTextColor(vehicalView.this.getResources().getColor(R.color.colorGreenSign));
//
//                        Toast.makeText(vehicalView.this, response1.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onProgressUpdate(int currentpercent, int totalpercent) {
//
//                        loader.setProgress(Float.parseFloat(String.valueOf(currentpercent)));
//                        // Log.v("AhmedDakrory:", String.valueOf(currentpercent) + " / " + String.valueOf(totalpercent));
//                    }
//                }, typeForImageOrDoc);
//            }
//        }));
//
//
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
    }

    else if (typeForImageOrDoc == Constants.TypeDocForServer) {

        Log.v("AhmedDakrory", typeForImageOrDoc + " :Ok 3");
        Log.v("AhmedDakrory", carData.getData().getId() + " :Ok 3");

        boolean end_image = false;
        mainDialog=new vehicalView.dialogWithProgress(vehicalView.this);
        mainDialog.dialog.show();
        mainDialog.setPercentage(0);
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator +"nycargoCarMainImages"+ File.separator +carData.getData().getUuid());
        if (dir.isDirectory())
        {
            String[] children = dir.list();

            upload_file_number(dir,children,0,typeForImageOrDoc);
        }

//        carData.getDocs().add(new vehicalsDetails.urlItem(file.getPath(), vehicalsDetails.TYPE_FILE, new CallBackViewChanger() {
//            @Override
//            public void setViewToPercentage(final AdCircleProgress loader, final TextView overlayView, final TextView markView) {
//                new FileUploader().uploadFile(file.getPath(), carData.getData().getId(), vehicalView.this, new FileUploader.FileUploaderCallback() {
//                    @Override
//                    public void onError(Throwable t) {
//                        Toast.makeText(vehicalView.this, "Some error occurred...", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFinish(Response<MyResponse> response) {
//                        Log.v("AhmedDakrory:", "Finish");
//                        MyResponse response1 = response.body();
//                        loader.setVisibility(View.GONE);
//                        overlayView.setVisibility(View.GONE);
//                        markView.setTextColor(vehicalView.this.getResources().getColor(R.color.colorGreenSign));
//
//                        Toast.makeText(vehicalView.this, response1.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onProgressUpdate(int currentpercent, int totalpercent) {
//
//                        loader.setProgress(Float.parseFloat(String.valueOf(currentpercent)));
//                        //Log.v("AhmedDakrory:", String.valueOf(currentpercent) + " / " + String.valueOf(totalpercent));
//                    }
//                }, typeForImageOrDoc);
//            }
//        }));


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

    else if (typeForImageOrDoc == Constants.Type3DForServer) {
        Log.v("AhmedDakrory","Akka Details");
        Log.v("AhmedDakrory",file.getPath().toString());
        carData.getImages3D().add(new vehicalsDetails.urlItem(file.getPath(), vehicalsDetails.TYPE_FILE, new CallBackViewChanger() {
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


                        File myFile = new File(file.getPath().toString());
                        if(myFile.exists())
                            myFile.delete();
                        Toast.makeText(vehicalView.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgressUpdate(int currentpercent, int totalpercent) {
                        Log.v("AhmedDakrory:", String.valueOf(currentpercent) + " / " + String.valueOf(totalpercent));

                        loader.setProgress(Float.parseFloat(String.valueOf(currentpercent)));
                    }
                }, typeForImageOrDoc);
            }
        }));




        adapterFor3Dimages.notifyItemInserted(adapterFor3Dimages.getItemCount() - 1);
        recyclerView3DImages.scrollToPosition(adapterFor3Dimages.getItemCount() - 1);
    }
    else if (typeForImageOrDoc == Constants.Type3DExteriorForServer) {

        mainDialog=new vehicalView.dialogWithProgress(vehicalView.this);
        mainDialog.dialog.show();
        mainDialog.setPercentage(0);

        new FileUploader().uploadFile(file.getPath(), carData.getData().getId(), vehicalView.this, new FileUploader.FileUploaderCallback() {
            @Override
            public void onError(Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mainDialog.dialog.dismiss();
                    }
                });
                Log.v("AhmedDakrory","Error: "+String.valueOf(t.toString()));
            }

            @Override
            public void onFinish(Response<MyResponse> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        File dir = new File(Environment.getExternalStorageDirectory() + File.separator +"nycargoCarServices"+ File.separator +carData.getData().getUuid());
                        if (dir.isDirectory())
                        {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++)
                            {
                                new File(dir, children[i]).delete();
                            }
                        }
                        if(dir.exists())
                            dir.delete();

                        mainDialog.dialog.dismiss();
                        getAllDataToAdapter();





                    }
                });
                Log.v("AhmedDakrory","Pers: "+String.valueOf("Loaded"));
                MyResponse response1 = response.body();

                Log.v("AhmedDakrory","Pers: "+response1.getMessage());
                Toast.makeText(vehicalView.this, response1.getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onProgressUpdate(int currentpercent, int totalpercent) {
                Log.v("AhmedDakrory","Pers: "+String.valueOf(currentpercent));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mainDialog.dialog.show();
                        try {
                            mainDialog.setPercentage((int) (((currentpercent))));
                        }catch (Error error){

                        }catch (Exception exc){

                        }
                    }
                });

            }
        },typeForImageOrDoc);


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

                String TYPE = data.getStringExtra("Type_Of_Return");

                if(TYPE!=null){
                    String imageUri2 = data.getStringExtra("DATA");

                    Log.v("AhmedDakrory", imageUri2.toString());
                    final File file = new File(imageUri2.toString());
                    Log.v("AhmedDakrory", "Type: "+Constants.TypeDocForServer);
                    uploadFileAndAddToAdapter(file,Constants.TypeDocForServer);

                }else{
                    Log.v("AhmedDakrory","Ahmed");

                    if(data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        final File file = new File(Environment.getExternalStorageDirectory() + File.separator +"nycargoCarMainImages"+ File.separator +carData.getData().getUuid());

                        for(int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //do something with the image (save it to some directory or whatever you need to do with it here)


                            File auxFile = new File(Environment.getExternalStorageDirectory() + File.separator +"nycargoCarMainImages"+ File.separator +carData.getData().getUuid()+File.separator+"Image_"+String.valueOf(i)+".jpg");
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

                        uploadFileAndAddToAdapter(file,Constants.TypeDocForServer);
                    }


                }




            }
        }
        else if(requestCode == Constants.TypeImageForServer){
            if(resultCode == RESULT_OK) {
                String TYPE = data.getStringExtra("Type_Of_Return");

                if(TYPE!=null){
                    String imageUri2 = data.getStringExtra("DATA");

                    Log.v("AhmedDakrory", imageUri2.toString());
                    final File file = new File(imageUri2.toString());
                    Log.v("AhmedDakrory", "Type: "+Constants.TypeImageForServer);
                    uploadFileAndAddToAdapter(file,Constants.TypeImageForServer);

                }else{
                    Log.v("AhmedDakrory","Ahmed");

                    if(data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        final File file = new File(Environment.getExternalStorageDirectory() + File.separator +"nycargoCarMainImages"+ File.separator +carData.getData().getUuid());

                        for(int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //do something with the image (save it to some directory or whatever you need to do with it here)


                            File auxFile = new File(Environment.getExternalStorageDirectory() + File.separator +"nycargoCarMainImages"+ File.separator +carData.getData().getUuid()+File.separator+"Image_"+String.valueOf(i)+".jpg");
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

                        uploadFileAndAddToAdapter(file,Constants.TypeImageForServer);
                    }


                }


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
        }else if(requestCode == Constants.Type3DForServer){
            Log.v("AhmedDakrory", "NewType: "+Constants.Type3DForServer);
            if(resultCode == RESULT_OK) {
                final Intent imageUri = data;
                Uri urlImage3D = imageUri.getData();
                Log.v("AhmedDakrory", urlImage3D.toString());
//                String selectedFilePath = FilePath.getPath(this, urlImage3D);
                final File file = new File(urlImage3D.toString());
                Log.v("AhmedDakrory", "Type: "+Constants.Type3DForServer);
                uploadFileAndAddToAdapter(file,Constants.Type3DForServer);
            }
        }else if(requestCode == Constants.Type3DExteriorForServer){
            Log.v("AhmedDakrory", "NewType: "+Constants.Type3DExteriorForServer);
            if(resultCode == RESULT_OK) {
                final Intent imageUri = data;
                Uri urlImage3D = imageUri.getData();
                Log.v("AhmedDakrory", urlImage3D.toString());
//                String selectedFilePath = FilePath.getPath(this, urlImage3D);
                final File file = new File(urlImage3D.toString());
                Log.v("AhmedDakrory", "Type: "+Constants.Type3DExteriorForServer);
                uploadFileAndAddToAdapter(file,Constants.Type3DExteriorForServer);
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
            Log.v("AhmedDakrory","Error: "+String.valueOf(e.toString()));
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

}
