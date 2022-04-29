package dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels;

import com.google.gson.annotations.SerializedName;

public class CheckForUpdates_Response {

    @SerializedName("stateFingerprint")
    public String stateFingerprint;

    public String getStateFingerprint() {
        return stateFingerprint;
    }

    public void setStateFingerprint(String stateFingerprint) {
        this.stateFingerprint = stateFingerprint;
    }
}
