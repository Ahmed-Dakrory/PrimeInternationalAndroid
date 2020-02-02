package dakrory.a7med.cargomarine;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dakrory.a7med.cargomarine.CustomViews.vehicalImagesAdapter;
import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.Models.vehicalsDetails.carDetails;
import dakrory.a7med.cargomarine.Models.vinDetails;
import dakrory.a7med.cargomarine.helpers.Api;
import dakrory.a7med.cargomarine.helpers.Constants;
import dakrory.a7med.cargomarine.layoutManagers.ZoomCenterCardLayoutManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class vehicalView extends AppCompatActivity implements View.OnClickListener {


    RecyclerView recyclerViewImages;
    RecyclerView recyclerViewDocs;


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

        defineViewsAndIntegrate();



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

                    dataOfCar.setUuid(vinNew);
                    dataOfCar.setMake(car.Results.get(0).Make);
                    dataOfCar.setModel(car.Results.get(0).Model);
                    dataOfCar.setYear(car.Results.get(0).ModelYear);
                    dataOfCar.setAssemlyCountry(car.Results.get(0).PlantCountry);
                    dataOfCar.setBodyStyle(car.Results.get(0).DriveType);
                    dataOfCar.setEngineLiters(car.Results.get(0).DisplacementL);
                    dataOfCar.setEngineType(car.Results.get(0).EngineConfiguration+"- "+car.Results.get(0).EngineCylinders+" Cylinders");
                    carData.setData(dataOfCar);
                    setTextData(carData.getData());

                }
            }

            @Override
            public void onFailure(Call<vinDetails> call, Throwable t) {

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

                 carData =  response.body();
                if(carData!=null){
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
            recyclerViewImages.setVisibility(View.VISIBLE);
            recyclerViewDocs.setVisibility(View.GONE);
        }else if(v.getId()==R.id.DocumentsButton){
            recyclerViewImages.setVisibility(View.GONE);
            recyclerViewDocs.setVisibility(View.VISIBLE);
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
