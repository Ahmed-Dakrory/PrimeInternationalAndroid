package dakrory.a7med.cargomarine.helpers;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import androidx.loader.content.CursorLoader;
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



    public void uploadFile(String fileName, String desc, final Activity activity, final FileUploaderCallback fileUploaderCallback) {

        Log.v("AhmedDakrory:","Start Upload");
        this.fileUploaderCallback=fileUploaderCallback;
        File file  = new File(fileName);
        //creating a file
        totalFileLength = totalFileLength + file.length();
        PRRequestBody fileBody = new PRRequestBody(file);
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
        Call<MyResponse> call = api.uploadImage(filePart, desc);

        //finally performing the call
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                fileUploaderCallback.onFinish(response);
                Log.v("AhmedDakrory:","Start Response");
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                fileUploaderCallback.onError(t);
                Log.v("AhmedDakrory:","Start Error");
                Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface FileUploaderCallback{
        void onError(Throwable t);
        void onFinish(Response<MyResponse> response);
        void onProgressUpdate(int currentpercent, int totalpercent);
    }

    public class PRRequestBody extends RequestBody {
        private File mFile;

        private static final int DEFAULT_BUFFER_SIZE = 50000;

        public PRRequestBody(final File file) {
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
