package dakrory.a7med.cargomarine.helpers;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import dakrory.a7med.cargomarine.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class FileUploader {

    public FileUploaderCallback fileUploaderCallback;

    private long totalFileLength = 0;
    private long totalFileUploaded = 0;


    //Responsible for upload Images and Doc
    public void uploadFile(String fileName, int carId, final Activity activity, final FileUploaderCallback fileUploaderCallback,int type) {

        this.fileUploaderCallback=fileUploaderCallback;
        File file  = new File(fileName);
        //creating a file
        totalFileLength = totalFileLength + file.length();
        PRRequestBody fileBody = new PRRequestBody(file,type);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);


        Log.v("AhmedDakrory:","Start Upload2");
        //The gson builder
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
        Call<MyResponse> call = api.uploadImage(filePart, carId,type);
        if(modelsFunctions.checkNetworkStatus(activity)) {
            //finally performing the call
            call.enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    fileUploaderCallback.onFinish(response);
                    Log.v("AhmedDakrory:", "Start Response");
                }

                @Override
                public void onFailure(Call<MyResponse> responseCall, Throwable t) {

                    fileUploaderCallback.onError(t);
                    Log.v("AhmedDakrory:", "Start Error");
                    Log.v("AhmedDakrory:", t.getMessage());
                    Log.v("AhmedDakrory:", t.toString());
                    Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
                }
            });
        }
    }



    //Responsible for upload Signiture Image
    public void uploadSignitureOfDriverDestination(String fileName, int carId, final Activity activity, final FileUploaderCallback fileUploaderCallback) {

        this.fileUploaderCallback=fileUploaderCallback;
        File file  = new File(fileName);
        //creating a file
        totalFileLength = totalFileLength + file.length();
        PRRequestBodyForDriverSigniture fileBody = new PRRequestBodyForDriverSigniture(file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);


        Log.v("AhmedDakrory:","Start Upload2 LEngth: "+String.valueOf(totalFileLength));
        //The gson builder
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
        Call<MyResponse> call = api.uploadSignitureOfDriverDestination(filePart, carId);
        if(modelsFunctions.checkNetworkStatus(activity)) {
            //finally performing the call
            call.enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    fileUploaderCallback.onFinish(response);
                    Log.v("AhmedDakrory:", "Start Response");
                }

                @Override
                public void onFailure(Call<MyResponse> responseCall, Throwable t) {

                    fileUploaderCallback.onError(t);
                    Log.v("AhmedDakrory:", "Start Error");
                    Log.v("AhmedDakrory:", t.getMessage());
                    Log.v("AhmedDakrory:", t.toString());
                    Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
                }
            });
        }
    }






    //Responsible for upload Signiture Image
    public void uploadSignitureOfDriver(String fileName, int carId, final Activity activity, final FileUploaderCallback fileUploaderCallback) {

        this.fileUploaderCallback=fileUploaderCallback;
        File file  = new File(fileName);
        //creating a file
        totalFileLength = totalFileLength + file.length();
        PRRequestBodyForDriverSigniture fileBody = new PRRequestBodyForDriverSigniture(file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);


        Log.v("AhmedDakrory:","Start Upload2 LEngth: "+String.valueOf(totalFileLength));
        //The gson builder
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
        Call<MyResponse> call = api.uploadSignitureOfDriver(filePart, carId);
        if(modelsFunctions.checkNetworkStatus(activity)) {
            //finally performing the call
            call.enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    fileUploaderCallback.onFinish(response);
                    Log.v("AhmedDakrory:", "Start Response");
                }

                @Override
                public void onFailure(Call<MyResponse> responseCall, Throwable t) {

                    fileUploaderCallback.onError(t);
                    Log.v("AhmedDakrory:", "Start Error");
                    Log.v("AhmedDakrory:", t.getMessage());
                    Log.v("AhmedDakrory:", t.toString());
                    Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
                }
            });
        }
    }





    //Responsible for upload Signiture Image
    public void uploadCrashImage(String JsonCrashOfCar,String fileName, int carId, final Activity activity, final FileUploaderCallback fileUploaderCallback) {

        this.fileUploaderCallback=fileUploaderCallback;
        File file  = new File(fileName);
        //creating a file
        totalFileLength = totalFileLength + file.length();
        PRRequestBodyForDriverSigniture fileBody = new PRRequestBodyForDriverSigniture(file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);


        Log.v("AhmedDakrory:","Start Upload2 LEngth: "+String.valueOf(totalFileLength));
        //The gson builder
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
        Call<MyResponse> call = api.uploadCrashImage(JsonCrashOfCar,filePart, carId);
        if(modelsFunctions.checkNetworkStatus(activity)) {
            //finally performing the call
            call.enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    fileUploaderCallback.onFinish(response);
                    Log.v("AhmedDakrory:", "Start Response");
                }

                @Override
                public void onFailure(Call<MyResponse> responseCall, Throwable t) {

                    fileUploaderCallback.onError(t);
                    Log.v("AhmedDakrory:", "Start Error");
                    Log.v("AhmedDakrory:", t.getMessage());
                    Log.v("AhmedDakrory:", t.toString());
                    Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,R.string.PleaseCheckNetworkConnection,Toast.LENGTH_LONG).show();
                }
            });
        }
    }




    public interface FileUploaderCallback{
        void onError(Throwable t);
        void onFinish(Response<MyResponse> response);
        void onProgressUpdate(int currentpercent, int totalpercent);
    }

    public class PRRequestBody extends RequestBody {
        private File mFile;
        private int type = 0;
        private static final int DEFAULT_BUFFER_SIZE = 50000;

        public PRRequestBody(final File file,int type) {
            mFile = file;
            this.type = type;

        }

        @Override
        public MediaType contentType() {
            // i want to upload only images
            if(type ==Constants.TypePdfForServer){
                return MediaType.parse("application/pdf");
            }else {
                return MediaType.parse("image/*");
            }
        }

        @Override
        public long contentLength() throws IOException {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            long fileLength = mFile.length();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(mFile);
            long uploaded = 0;

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = in.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new ProgressUpdater(uploaded, fileLength));
                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                in.close();
            }
        }
    }




    public class PRRequestBodyForDriverSigniture extends RequestBody {
        private File mFile;
        private static final int DEFAULT_BUFFER_SIZE = 50000;

        public PRRequestBodyForDriverSigniture(final File file) {
            mFile = file;

        }

        @Override
        public MediaType contentType() {
            // i want to upload only images

                return MediaType.parse("image/*");

        }

        @Override
        public long contentLength() throws IOException {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            long fileLength = mFile.length();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(mFile);
            long uploaded = 0;

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = in.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new ProgressUpdater(uploaded, fileLength));
                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                in.close();
            }
        }
    }



    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            int current_percent = (int)(100 * mUploaded / mTotal);
            int total_percent = (int)(100 * (totalFileUploaded+mUploaded) / totalFileLength);
            fileUploaderCallback.onProgressUpdate(current_percent, total_percent );
        }
    }

}
