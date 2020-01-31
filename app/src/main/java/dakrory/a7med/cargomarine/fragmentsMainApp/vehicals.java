package dakrory.a7med.cargomarine.fragmentsMainApp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dakrory.a7med.cargomarine.CustomViews.MyImageAdapter;
import dakrory.a7med.cargomarine.CustomViews.vehicalsAdapter;
import dakrory.a7med.cargomarine.Models.vehicalsData;
import dakrory.a7med.cargomarine.R;
import dakrory.a7med.cargomarine.helpers.Api;
import dakrory.a7med.cargomarine.layoutManagers.ZoomCenterCardLayoutManager;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class vehicals extends Fragment {

    RecyclerView recyclerView;
    vehicalsAdapter adapter;
    ProgressBar loaderRecy;
    ImageView emptyImage;
    List<dakrory.a7med.cargomarine.Models.vehicalsData.vehicalItem> vehicalItems;
    List<dakrory.a7med.cargomarine.Models.vehicalsData.vehicalItem> vehicalItemsFull;
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

        page = 0;
        //Variables for Pagination
        isLoading =true;
        pastVisibleItems = 0; visibleItemCount=0; totalItemCount=0; previous_total = 0;
        view_threshold = 10;
        vehicalItems=new ArrayList<vehicalsData.vehicalItem>();
        vehicalItemsFull=new ArrayList<vehicalsData.vehicalItem>();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewVehicals);
        loaderRecy=(ProgressBar)getActivity().findViewById(R.id.progBar);
        emptyImage=(ImageView)getActivity().findViewById(R.id.emptyImage);
        spinner= (Spinner) getActivity().findViewById(R.id.spinnerVehicalState);
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
        adapter = new vehicalsAdapter(vehicalItems,vehicalItemsFull);
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

        PerformPagination(stateSelected);

    }

    private void PerformPagination(int type) {


            //WareHouse
            loaderRecy.setVisibility(View.VISIBLE);
            //creating a call and calling the upload image method
            Call<vehicalsData> call = api.getAllCarsForMainUser(48,page,N_item,type);
            call.enqueue(new Callback<vehicalsData>() {
                @Override
                public void onResponse(Call<vehicalsData> call, Response<vehicalsData> response) {

                    vehicalsData vehicalsData=response.body();

                    if(vehicalsData.getError().equalsIgnoreCase("false")){

                        vehicalItems.addAll(vehicalsData.getData());
                        vehicalItemsFull.addAll(vehicalsData.getData());
                        if(vehicalItemsFull.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyImage.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                        loaderRecy.setVisibility(View.GONE);

                        Log.v("AhmedDakrory","SizeOfList: "+stateSelected+" is "+vehicalItemsFull.size());

                    }

                    loaderRecy.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<vehicalsData> call, Throwable t) {
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
}
