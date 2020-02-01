package dakrory.a7med.cargomarine;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dakrory.a7med.cargomarine.CustomViews.CallBackViewChanger;
import dakrory.a7med.cargomarine.CustomViews.MyImageAdapter;
import dakrory.a7med.cargomarine.CustomViews.MyImageData;
import dakrory.a7med.cargomarine.CustomViews.vehicalViewAdapter;
import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.helpers.Api;
import dakrory.a7med.cargomarine.helpers.Constants;
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


public class vehicalView extends AppCompatActivity {


    RecyclerView recyclerView;


    vehicalViewAdapter adapter;

    int idOfCar=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehical_view);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Intent lastIntentGet=getIntent();
        idOfCar = lastIntentGet.getIntExtra(Constants.CarIdData,-1);


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
        Call<vehicalsDetails> call = api.getAllDetailsForCar(idOfCar);
        call.enqueue(new Callback<vehicalsDetails>() {
            @Override
            public void onResponse(Call<vehicalsDetails> call, Response<vehicalsDetails> response) {

                vehicalsDetails post =  response.body();
                if(post!=null){

                    adapter = new vehicalViewAdapter(recyclerView,post.getImages(),vehicalView.this);
                    ZoomCenterCardLayoutManager layoutManager=new ZoomCenterCardLayoutManager(vehicalView.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<vehicalsDetails> call, Throwable t) {

            }
        });
    }



}
