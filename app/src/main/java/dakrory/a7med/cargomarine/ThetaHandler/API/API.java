package dakrory.a7med.cargomarine.ThetaHandler.API;

import android.os.Handler;
import android.os.Looper;

import dakrory.a7med.cargomarine.ThetaHandler.API.RequestModels.fingerPrint_name;
import dakrory.a7med.cargomarine.ThetaHandler.API.RequestModels.session_name;
import dakrory.a7med.cargomarine.ThetaHandler.API.RequestModels.startSession;
import dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels.CheckForUpdates_Response;
import dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels.SET_API_VERSION_Response;
import dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels.data_Response;
import dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels.startSessionResponse;
import dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels.takePicture_Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Sheetal on 5/16/18.
 */




public interface API {



    @POST("/osc/commands/execute")
    Call<startSessionResponse> StartSession(@Body startSession body);

    @POST("/osc/commands/execute")
    Call<SET_API_VERSION_Response> SET_API_VERSION(@Body startSession body);


    @POST("/osc/commands/execute")
    Call<takePicture_Response> takePicture(@Body session_name body);

    @Headers({
            "Content-Type: application/json"
    })

    @POST("/osc/commands/execute")
    Call<Response> livePreview(@Body session_name body);


    @POST("/osc/commands/execute")
    Call<SET_API_VERSION_Response> setSettings(@Body startSession body);


    @POST("/osc/state")
    Call<data_Response> handleData();


    @POST("/osc/checkForUpdates")
    Call<CheckForUpdates_Response> CheckForUpdates(@Body fingerPrint_name body);





    public class PRRequestBody extends RequestBody {
        private File mFile;
        private int type = 0;
        private static final int DEFAULT_BUFFER_SIZE = 99999999;

        public PRRequestBody(final File file,int type) {
            mFile = file;
            this.type = type;

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
                    //handler.post(new ProgressUpdater(uploaded, fileLength));
                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                in.close();
            }
        }
    }

}

