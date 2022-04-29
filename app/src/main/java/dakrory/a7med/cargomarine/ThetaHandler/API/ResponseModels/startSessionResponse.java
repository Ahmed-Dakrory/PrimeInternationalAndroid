package dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels;

import com.google.gson.annotations.SerializedName;

public class startSessionResponse {

    @SerializedName("name")
    public String name;

    @SerializedName("state")
    public String state;


    @SerializedName("results")
    public results results;


    public class results {
        @SerializedName("sessionId")
        public String sessionId;


        @SerializedName("timeout")
        public Integer timeout;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public Integer getTimeout() {
            return timeout;
        }

        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
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

    public startSessionResponse.results getResults() {
        return results;
    }

    public void setResults(startSessionResponse.results results) {
        this.results = results;
    }
}
