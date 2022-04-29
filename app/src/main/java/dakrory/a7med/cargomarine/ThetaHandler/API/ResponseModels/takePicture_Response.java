package dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels;

import com.google.gson.annotations.SerializedName;

public class takePicture_Response {

    @SerializedName("name")
    public String name;

    @SerializedName("state")
    public String state;

    @SerializedName("id")
    public String id;


    @SerializedName("progress")
    public progress progress;


    public class progress {
        @SerializedName("completion")
        public Float completion;

        public Float getCompletion() {
            return completion;
        }

        public void setCompletion(Float completion) {
            this.completion = completion;
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public takePicture_Response.progress getProgress() {
        return progress;
    }

    public void setProgress(takePicture_Response.progress progress) {
        this.progress = progress;
    }
}
