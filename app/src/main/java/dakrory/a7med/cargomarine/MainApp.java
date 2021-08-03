package dakrory.a7med.cargomarine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import dakrory.a7med.cargomarine.Models.userData;
import dakrory.a7med.cargomarine.Models.userImage;
import dakrory.a7med.cargomarine.fragmentsMainApp.UserDetails;
import dakrory.a7med.cargomarine.fragmentsMainApp.vehicals;
import dakrory.a7med.cargomarine.helpers.Api;
import dakrory.a7med.cargomarine.helpers.modelsFunctions;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainApp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment vehicalsFragment;
    Fragment userDetailsFragment;

    TextView userNameTextView;
    ImageView userDataImage;
    ProgressBar userDataImageLoader;


    userData thisAccountUserData = LoginActivity.thisAccountCredData;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        vehicalsFragment = vehicals.newInstance();
        userDetailsFragment= UserDetails.newInstance();


        if (savedInstanceState == null) {
            setFragmentNow(vehicalsFragment);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initializeUserNameData(navigationView);
    }

    private void initializeUserNameData(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        userDataImageLoader = (ProgressBar) headerView.findViewById(R.id.userDataImageLoader);
        userDataImage = (ImageView) headerView.findViewById(R.id.userDataImage);
        userNameTextView = (TextView)headerView.findViewById(R.id.userNameData);
        userNameTextView.setText(thisAccountUserData.getUserDetails().getFirstName()+" "+thisAccountUserData.getUserDetails().getLastName());

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        api = retrofit.create(Api.class);
        Call<userImage> call  = api.getImageFromUserId(thisAccountUserData.getUserDetails().getId());
       if(modelsFunctions.checkNetworkStatus(this)) {
           call.enqueue(new Callback<userImage>() {
               @Override
               public void onResponse(Call<userImage> data, Response<userImage> response) {
                   userImage userImage = (dakrory.a7med.cargomarine.Models.userImage) response.body();
                   if (!userImage.getData().getImage().equalsIgnoreCase("")) {
                       byte[] decodedString = Base64.decode(userImage.getData().getImage(), Base64.DEFAULT);
                       Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                       userDataImage.setImageBitmap(decodedByte);
                       userDataImageLoader.setVisibility(View.GONE);
                       userDataImage.setVisibility(View.VISIBLE);
                   } else {
                       Picasso.get().load(R.mipmap.logo).into(userDataImage);
                       userDataImageLoader.setVisibility(View.GONE);
                       userDataImage.setVisibility(View.VISIBLE);
                   }
                   Log.v("AhmedDakrory", "Data: " + String.valueOf(userImage.getData().getImage()));
               }

               @Override
               public void onFailure(Call<userImage> call, Throwable t) {

               }
           });
       }else{
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   Toast.makeText(MainApp.this,R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
               }
           });
       }
       }

    public void setFragmentNow(Fragment fragmentNow){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragmentNow);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_app, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Freightlist) {
            // Handle the camera action

            setFragmentNow(vehicalsFragment);
        }else if(id == R.id.logOut){
            Intent goToLogIn = new Intent(MainApp.this,LoginActivity.class);
            LoginActivity.thisAccountCredData= null;
            startActivity(goToLogIn);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        vehicalsFragment.onActivityResult(requestCode, resultCode, data);
        Log.v("AhmedDakrory","Done1");
    }
}
