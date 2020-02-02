package dakrory.a7med.cargomarine.helpers;

import com.google.gson.JsonObject;

import dakrory.a7med.cargomarine.Models.vehicalsDataAllList;
import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.Models.vinDetails;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Sheetal on 5/16/18.
 */




public interface Api {

    String BASE_URL = "http://cargomarin.com:8083";


    @Multipart
    @POST("Api.php?apicall=upload")
    Call<MyResponse> uploadImage(@Part MultipartBody.Part file, @Part("desc") String desc);


    @POST("Api.php?apicall=getallimages")
    Call<JsonObject> getAllImages();


    @FormUrlEncoded
    @POST("Api.php?apicall=getAllCarsForMainAccount")
    Call<vehicalsDataAllList> getAllCarsForMainUser(@Field("mainId") int mainId, @Field("page") int page, @Field("N_items") int N_items, @Field("type") int type);

    @FormUrlEncoded
    @POST("Api.php?apicall=getCarData")
    Call<vehicalsDetails> getAllDetailsForCar(@Field("id") int id);

    @GET("?format=json")
    Call<vinDetails> getCarDetailsfromVin();

}

