package dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels;

import com.google.gson.annotations.SerializedName;

public class SET_API_VERSION_Response {

    @SerializedName("name")
    public String name;

    @SerializedName("state")
    public String state;



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


}
