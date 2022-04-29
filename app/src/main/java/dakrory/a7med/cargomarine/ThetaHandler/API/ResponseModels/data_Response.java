package dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels;

import com.google.gson.annotations.SerializedName;

public class data_Response {

    @SerializedName("fingerprint")
    public String fingerprint;

    @SerializedName("state")
    public state state;


    public class state {
        @SerializedName("batteryLevel")
        public float batteryLevel;


        @SerializedName("storageUri")
        public String storageUri;

        @SerializedName("_captureStatus")
        public String _captureStatus;

        @SerializedName("_recordedTime")
        public float _recordedTime;

        @SerializedName("_recordableTime")
        public float _recordableTime;

        @SerializedName("_latestFileUrl")
        public String _latestFileUrl;

        @SerializedName("_batteryState")
        public String _batteryState;


        @SerializedName("_apiVersion")
        public float _apiVersion;


        public float getBatteryLevel() {
            return batteryLevel;
        }

        public void setBatteryLevel(float batteryLevel) {
            this.batteryLevel = batteryLevel;
        }

        public String getStorageUri() {
            return storageUri;
        }

        public void setStorageUri(String storageUri) {
            this.storageUri = storageUri;
        }

        public String get_captureStatus() {
            return _captureStatus;
        }

        public void set_captureStatus(String _captureStatus) {
            this._captureStatus = _captureStatus;
        }

        public float get_recordedTime() {
            return _recordedTime;
        }

        public void set_recordedTime(float _recordedTime) {
            this._recordedTime = _recordedTime;
        }

        public float get_recordableTime() {
            return _recordableTime;
        }

        public void set_recordableTime(float _recordableTime) {
            this._recordableTime = _recordableTime;
        }

        public String get_latestFileUrl() {
            return _latestFileUrl;
        }

        public void set_latestFileUrl(String _latestFileUrl) {
            this._latestFileUrl = _latestFileUrl;
        }

        public String get_batteryState() {
            return _batteryState;
        }

        public void set_batteryState(String _batteryState) {
            this._batteryState = _batteryState;
        }

        public float get_apiVersion() {
            return _apiVersion;
        }

        public void set_apiVersion(float _apiVersion) {
            this._apiVersion = _apiVersion;
        }
    }


    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public data_Response.state getState() {
        return state;
    }

    public void setState(data_Response.state state) {
        this.state = state;
    }
}
