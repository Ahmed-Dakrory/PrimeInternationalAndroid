package dakrory.a7med.cargomarine.fragmentsMainApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dakrory.a7med.cargomarine.CustomViews.vehicalsAdapter;
import dakrory.a7med.cargomarine.LoginActivity;
import dakrory.a7med.cargomarine.Models.userData;
import dakrory.a7med.cargomarine.Models.vehicalsDataAllList;
import dakrory.a7med.cargomarine.R;
import dakrory.a7med.cargomarine.helpers.Api;
import dakrory.a7med.cargomarine.helpers.Constants;
import dakrory.a7med.cargomarine.vehicalView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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


public class vehicals extends Fragment {

    //qr code scanner object
    private IntentIntegrator qrScan;


    RecyclerView recyclerView;
    vehicalsAdapter adapter;
    ProgressBar loaderRecy;
    ImageView emptyImage;
    FloatingActionButton addNewVehicalButton;
    List<vehicalsDataAllList.vehicalItemOfAllList> vehicalItems;
    List<vehicalsDataAllList.vehicalItemOfAllList> vehicalItemsFull;
    Api api ;
    LinearLayoutManager layoutManager;
    private int page=0;
    private int N_item=10;
    Spinner spinner;
    int stateSelected=0;//For All Cars

    //Variables for Pagination
    private boolean isLoading =true;
    private int pastVisibleItems, visibleItemCount,totalItemCount,previous_total = 0;
    private int view_threshold = 10;
    private boolean allowEdit = false;
    private userData userDataOfThisAccount = LoginActivity.thisAccountCredData;
    public static vehicals newInstance() {
        vehicals v = new vehicals();
        return v;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.vehicals, container, false);

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
//intializing scan object
        qrScan = new IntentIntegrator(this.getActivity());


        //All Cars List
        page = 0;
        //Variables for Pagination
        isLoading =true;
        pastVisibleItems = 0; visibleItemCount=0; totalItemCount=0; previous_total = 0;
        view_threshold = 10;
        vehicalItems=new ArrayList<vehicalsDataAllList.vehicalItemOfAllList>();
        vehicalItemsFull=new ArrayList<vehicalsDataAllList.vehicalItemOfAllList>();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewVehicals);
        loaderRecy=(ProgressBar)getActivity().findViewById(R.id.progBar);
        emptyImage=(ImageView)getActivity().findViewById(R.id.emptyImage);
        spinner= (Spinner) getActivity().findViewById(R.id.spinnerVehicalState);
        addNewVehicalButton = (FloatingActionButton)getActivity().findViewById(R.id.addNewVehical);

        if(allowEdit){
            addNewVehicalButton.show();
        }else{
            addNewVehicalButton.hide();
        }


        addNewVehicalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewCar();
            }
        });
        ArrayAdapter<CharSequence> adapterSpiner = ArrayAdapter.createFromResource(getActivity(),
               R.array.states_vehicals, R.layout.simple_spinner_item);
        adapterSpiner.setDropDownViewResource(R.layout.dropstate);
        spinner.setAdapter(adapterSpiner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("AhmedDakrory","Position: "+position);

                page = 0;
                //Variables for Pagination
                isLoading =true;
                pastVisibleItems = 0; visibleItemCount=0; totalItemCount=0; previous_total = 0;
                view_threshold = 10;
                vehicalItems.clear();
                vehicalItemsFull.clear();

                stateSelected = position;
                PerformPagination(stateSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new vehicalsAdapter(vehicalItems,vehicalItemsFull,getActivity());
        recyclerView.setAdapter(adapter);


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

        Log.v("AhmedDakrory:","Start Upload3");



        emptyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformPagination(stateSelected);
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
                        PerformPagination(stateSelected);
                        isLoading=true;
                    }
                }
            }
        });


    }

    private void addNewCar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Use Scan Or Enter Vin?").setPositiveButton("Scan", dialogClickListener)
                .setNegativeButton("Type it", dialogClickListener).show();
    }

    private String getVinNumber(String contents) {


        int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 0, 1,
                2, 3, 4, 5, 0, 7, 0, 9, 2, 3,
                4, 5, 6, 7, 8, 9 };
        int[] weights = { 8, 7, 6, 5, 4, 3, 2, 10, 0, 9,
                8, 7, 6, 5, 4, 3, 2 };

        String s = contents;
        s = s.replaceAll("-", "");
        s = s.toUpperCase();
        if (s.length() != 17){
            StringBuilder sb = new StringBuilder(s);
            sb.deleteCharAt(0);
            s = sb.toString();
        }

        if (s.length() != 17){
            return "";
        }

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char c = s.charAt(i);
            int value;
            int weight = weights[i];

            // letter
            if (c >= 'A' && c <= 'Z') {
                value = values[c - 'A'];
                if (value == 0)
                    return "";
            }

            // number
            else if (c >= '0' && c <= '9') value = c - '0';

                // illegal character
            else  return "";

            sum = sum + weight * value;

        }

        // check digit
        sum = sum % 11;
        char check = s.charAt(8);
        if (check != 'X' && (check < '0' || check > '9'))
            throw new RuntimeException("Illegal check digit: " + check);
        if      (sum == 10 && check == 'X')  return s;
        else if (sum == check - '0')        return s;
        else                                return s;

    }



    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    qrScan.initiateScan();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    openEditVinDialog();
                    break;
            }
        }
    };

    private void openEditVinDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Vin");
        alertDialog.setMessage("Enter Vin");


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
                       String vin = input.getText().toString();
                        addNewCarWithVin(vin);
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

    private void addNewCarWithVin(String vin) {
        Intent addNewCar=new Intent(getActivity(), vehicalView.class);
        addNewCar.putExtra(Constants.SET_MODE_INTENT,Constants.MODE_ADD_NEW);
        addNewCar.putExtra(Constants.Car_VIN_Add_New,vin);
        startActivityForResult(addNewCar,Constants.ADD_NEW_VEHICAL_REQ_CODE);
    }

    private void PerformPagination(int type) {


            //WareHouse
            loaderRecy.setVisibility(View.VISIBLE);
        Call<vehicalsDataAllList> call = null;
            if(userDataOfThisAccount.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_MAIN) {
                 call = api.getAllCarsForMainUser(userDataOfThisAccount.getUserDetails().getMainUserId(), page, N_item, type);

            }else if(userDataOfThisAccount.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_MAIN2) {
                call = api.getAllCarsForMainTwoAccount(userDataOfThisAccount.getUserDetails().getMainTwoId(), page, N_item, type);

            }else if(userDataOfThisAccount.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_SHIPPER) {
                 call = api.getAllCarsForShipperAccount(userDataOfThisAccount.getUserDetails().getShipperId(), page, N_item, type);

            }else if(userDataOfThisAccount.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_VENDOR) {
              call = api.getAllCarsForVendorAccount(userDataOfThisAccount.getUserDetails().getVendorId(), page, N_item, type);

            }else if(userDataOfThisAccount.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_CUSTOMER) {
               call = api.getAllCarsForCustomerAccount(userDataOfThisAccount.getUserDetails().getCustomerId(), page, N_item, type);

            }else if(userDataOfThisAccount.getUserDetails().getRole() == userData.dataClassOfUser.ROLE_CONGSIGNEE) {
                 call = api.getAllCarsForConsigneeAccount(userDataOfThisAccount.getUserDetails().getConsigneeId(), page, N_item, type);

            }

            call.enqueue(new Callback<vehicalsDataAllList>() {
                @Override
                public void onResponse(Call<vehicalsDataAllList> call, Response<vehicalsDataAllList> response) {

                    vehicalsDataAllList vehicalsDataAllList =response.body();

                    if(vehicalsDataAllList.getError().equalsIgnoreCase("false")){

                        vehicalItems.addAll(vehicalsDataAllList.getData());
                        vehicalItemsFull.addAll(vehicalsDataAllList.getData());
                        if(vehicalItemsFull.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyImage.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                        loaderRecy.setVisibility(View.GONE);

                        Log.v("AhmedDakrory","page: "+page+"SizeOfList: "+stateSelected+" is "+vehicalItemsFull.size());

                    }

                    loaderRecy.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<vehicalsDataAllList> call, Throwable t) {
                    isLoading=false;
                    loaderRecy.setVisibility(View.GONE);
                    if(vehicalItemsFull.size()==0) {
                        recyclerView.setVisibility(View.GONE);
                        emptyImage.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.Error_In_Loading),Toast.LENGTH_LONG).show();
                    Log.v("AhmedDakrory","Error: "+t.toString());
                }
            });

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
                String vin = getVinNumber( result.getContents());
               // Toast.makeText(getActivity(), vin, Toast.LENGTH_LONG).show();

                Log.v("AhmedDakrory", vin); // Prints scan results
                addNewCarWithVin(vin);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
           if(requestCode == Constants.ADD_NEW_VEHICAL_REQ_CODE) {
               page = 0;
               //Variables for Pagination
               isLoading = true;
               pastVisibleItems = 0;
               visibleItemCount = 0;
               totalItemCount = 0;
               previous_total = 0;
               view_threshold = 10;
               vehicalItems.clear();
               vehicalItemsFull.clear();

               PerformPagination(stateSelected);

           }
           }
    }
}
