package dakrory.a7med.cargomarine.helpers;

import com.google.gson.JsonObject;

import dakrory.a7med.cargomarine.Models.userData;
import dakrory.a7med.cargomarine.Models.vehicalsDataAllList;
import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.Models.vinDetails;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
    Call<MyResponse> uploadImage(@Part MultipartBody.Part file, @Part("carId") int carId, @Part("type") int type);


    @POST("Api.php?apicall=getallimages")
    Call<JsonObject> getAllImages();


    @FormUrlEncoded
    @POST("Api.php?apicall=getAllCarsForMainAccount")
    Call<vehicalsDataAllList> getAllCarsForMainUser(@Field("mainId") int mainId, @Field("page") int page, @Field("N_items") int N_items, @Field("type") int type);


    @FormUrlEncoded
    @POST("Api.php?apicall=getAllCarsForMainTwoAccount")
    Call<vehicalsDataAllList> getAllCarsForMainTwoAccount(@Field("mainTwoId") int mainId, @Field("page") int page, @Field("N_items") int N_items, @Field("type") int type);


    @FormUrlEncoded
    @POST("Api.php?apicall=getAllCarsForShipperAccount")
    Call<vehicalsDataAllList> getAllCarsForShipperAccount(@Field("shipperId") int mainId, @Field("page") int page, @Field("N_items") int N_items, @Field("type") int type);

    @FormUrlEncoded
    @POST("Api.php?apicall=getAllCarsForVendorAccount")
    Call<vehicalsDataAllList> getAllCarsForVendorAccount(@Field("vendorId") int mainId, @Field("page") int page, @Field("N_items") int N_items, @Field("type") int type);

    @FormUrlEncoded
    @POST("Api.php?apicall=getAllCarsForCustomerAccount")
    Call<vehicalsDataAllList> getAllCarsForCustomerAccount(@Field("customerId") int mainId, @Field("page") int page, @Field("N_items") int N_items, @Field("type") int type);


    @FormUrlEncoded
    @POST("Api.php?apicall=getAllCarsForConsigneeAccount")
    Call<vehicalsDataAllList> getAllCarsForConsigneeAccount(@Field("consigneeId") int mainId, @Field("page") int page, @Field("N_items") int N_items, @Field("type") int type);



    @FormUrlEncoded
    @POST("Api.php?apicall=getUserWithNameAndPassword")
    Call<userData> getUserWithNameAndPassword(@Field("userName") String userName, @Field("password") String password);

    @FormUrlEncoded
    @POST("Api.php?apicall=getCarData")
    Call<vehicalsDetails> getAllDetailsForCar(@Field("id") int id);

    @POST("Api.php?apicall=insertNewCar")
    Call<vehicalsDetails> insertNewCar(@Body vehicalsDetails.carDetails carData);

    @GET("?format=json")
    Call<vinDetails> getCarDetailsfromVin();

}

