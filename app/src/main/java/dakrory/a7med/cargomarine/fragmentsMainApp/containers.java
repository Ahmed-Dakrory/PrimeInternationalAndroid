package dakrory.a7med.cargomarine.fragmentsMainApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dakrory.a7med.cargomarine.CustomViews.containersAdapter;
import dakrory.a7med.cargomarine.LoginActivity;
import dakrory.a7med.cargomarine.Models.userData;
import dakrory.a7med.cargomarine.Models.containersDataAllList;
import dakrory.a7med.cargomarine.R;
import dakrory.a7med.cargomarine.containerView;
import dakrory.a7med.cargomarine.helpers.Api;
import dakrory.a7med.cargomarine.helpers.Constants;
import dakrory.a7med.cargomarine.helpers.modelsFunctions;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class containers extends Fragment {


    RecyclerView recyclerView;
    containersAdapter adapter;
    ProgressBar loaderRecy;
    ImageView emptyImage;
    FloatingActionButton addNewContainerButton;
    List<containersDataAllList.containerItemOfAllList> containerItems;
    List<containersDataAllList.containerItemOfAllList> containerItemsFull;
    Api api ;
    LinearLayoutManager layoutManager;
    private int page=0;
    private int N_item=10;
    Spinner spinner;

    //Variables for Pagination
    private boolean isLoading =true;
    private int pastVisibleItems, visibleItemCount,totalItemCount,previous_total = 0;
    private int view_threshold = 10;
    private boolean allowEdit = false;
    private userData userDataOfThisAccount = LoginActivity.thisAccountCredData;
    public static containers newInstance() {
        containers v = new containers();
        return v;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.containers, container, false);

    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
        setHasOptionsMenu(true);

        if(userDataOfThisAccount.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_MAIN||userDataOfThisAccount.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_MAIN2){
            allowEdit = true;
        }else{
            allowEdit = false;
        }


        //All Containers List
        page = 0;
        //Variables for Pagination
        isLoading =true;
        pastVisibleItems = 0; visibleItemCount=0; totalItemCount=0; previous_total = 0;
        view_threshold = 10;
        containerItems=new ArrayList<containersDataAllList.containerItemOfAllList>();
        containerItemsFull=new ArrayList<containersDataAllList.containerItemOfAllList>();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewContainers);
        loaderRecy=(ProgressBar)getActivity().findViewById(R.id.progBar2);
        emptyImage=(ImageView)getActivity().findViewById(R.id.emptyImage2);
        addNewContainerButton = (FloatingActionButton)getActivity().findViewById(R.id.addNewContainer);

        if(allowEdit){
            addNewContainerButton.show();
        }else{
            addNewContainerButton.hide();
        }


        addNewContainerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewContainer();
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new containersAdapter(containerItems,containerItemsFull,getActivity());
        recyclerView.setAdapter(adapter);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
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

        Log.v("AhmedDakrory:","Start Upload3");




        emptyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformPagination();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.v("AhmedDakrory","Recy");
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if(dy > 0){
                    if(isLoading){
                        if(totalItemCount>previous_total){
                            isLoading =false;
                            previous_total = totalItemCount;
                        }
                    }

                    if(!isLoading&&(totalItemCount-visibleItemCount)<=(pastVisibleItems+view_threshold)){
                        page =page +10;
                        PerformPagination();
                        isLoading=true;
                    }
                }
            }
        });



        PerformPagination();
    }

    private void addNewContainer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to add Container?").setPositiveButton("No", dialogClickListener)
                .setNegativeButton("Yes", dialogClickListener).show();
    }



    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){


                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    openEditNumberDialog();
                    break;
            }
        }
    };

    private void openEditNumberDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Container Number");
        alertDialog.setMessage("Enter Container Number");


        final EditText input = new EditText(this.getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_directions_car_black_24dp);

        alertDialog.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       String number = input.getText().toString();
                        addNewContainerWithNumber(number);
                    }
                });

        alertDialog.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void addNewContainerWithNumber(String number) {
        Intent addNewContainer=new Intent(getActivity(), containerView.class);
        addNewContainer.putExtra(Constants.SET_MODE_INTENT,Constants.MODE_ADD_NEW);
        addNewContainer.putExtra(Constants.Container_NUMBER_Add_New,number);
        startActivityForResult(addNewContainer,Constants.ADD_NEW_CONTAINER_REQ_CODE);
    }

    private void PerformPagination() {


            //WareHouse
            loaderRecy.setVisibility(View.VISIBLE);
        Call<containersDataAllList> call = null;
            if(userDataOfThisAccount.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_MAIN) {
                Log.v("AHMED_NOW__","I'M HERE "+String.valueOf(userDataOfThisAccount.getUserDetails().getMainUserId()));
                 call = api.getAllContainersForMainUser(userDataOfThisAccount.getUserDetails().getMainUserId(), page, N_item);

            }
        if(modelsFunctions.checkNetworkStatus(getActivity())) {
            call.enqueue(new Callback<containersDataAllList>() {
                @Override
                public void onResponse(Call<containersDataAllList> call, Response<containersDataAllList> response) {

                    containersDataAllList containersDataAllList = response.body();

                    if (containersDataAllList.getError().equalsIgnoreCase("false")) {

                        containerItems.addAll(containersDataAllList.getData());
                        containerItemsFull.addAll(containersDataAllList.getData());
                        if (containerItemsFull.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyImage.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                        loaderRecy.setVisibility(View.GONE);

                        Log.v("AhmedDakrory", "page: " + page + "SizeOfList:  is " + containerItemsFull.size());

                    }

                    loaderRecy.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<containersDataAllList> call, Throwable t) {
                    isLoading = false;
                    loaderRecy.setVisibility(View.GONE);
                    if (containerItemsFull.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        emptyImage.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Error_In_Loading), Toast.LENGTH_LONG).show();
                    Log.v("AhmedDakrory", "Error: " + t.toString());
                }
            });
        }else{
           getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(),R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
                }
            });

        }
      }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.action_search);
        Log.v("AhmedDakrory","Query");
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.v("AhmedDakrory",newText);
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("AhmedDakrory","Done3");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                String container_number = result.getContents();
               // Toast.makeText(getActivity(), number, Toast.LENGTH_LONG).show();

                Log.v("AhmedDakrory", container_number); // Prints scan results
                addNewContainerWithNumber(container_number);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
           if(requestCode == Constants.ADD_NEW_CONTAINER_REQ_CODE) {
               page = 0;
               //Variables for Pagination
               isLoading = true;
               pastVisibleItems = 0;
               visibleItemCount = 0;
               totalItemCount = 0;
               previous_total = 0;
               view_threshold = 10;
               containerItems.clear();
               containerItemsFull.clear();

               PerformPagination();

           }
           }
    }
}
