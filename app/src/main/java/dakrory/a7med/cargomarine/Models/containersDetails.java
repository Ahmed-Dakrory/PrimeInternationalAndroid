package dakrory.a7med.cargomarine.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import dakrory.a7med.cargomarine.CustomViews.CallBackViewChanger;

public class containersDetails {

    public static int TYPE_FILE=0;
    public static int TYPE_Server=1;

    @SerializedName("error")
    private String error;

    @SerializedName("images")
    private List<urlItem> images;

    @SerializedName("data")
    private containerDetails data;


    public static class urlItem{
        public urlItem(String url, int type, CallBackViewChanger callBackViewChanger) {
            this.url = url;
            this.type = type;
            this.callBackViewChanger = callBackViewChanger;
        }

        @SerializedName("url")
        private String url;


        @SerializedName("type")
        private int type;


        CallBackViewChanger callBackViewChanger;

        public CallBackViewChanger getCallBackViewChanger() {
            return callBackViewChanger;
        }

        public void setCallBackViewChanger(CallBackViewChanger callBackViewChanger) {
            this.callBackViewChanger = callBackViewChanger;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


    public static class containerDetails{


        @SerializedName("id")
        private int id;


        @SerializedName("container_number")
        private String container_number="";


        @SerializedName("description_of_container")
        private String description_of_container="";

        @SerializedName("datetime")
        private String datetime="";




        @SerializedName("mainUrl")
        private String mainUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContainer_number() {
            return container_number;
        }

        public void setContainer_number(String container_number) {
            this.container_number = container_number;
        }

        public String getDescription_of_container() {
            return description_of_container;
        }

        public void setDescription_of_container(String description_of_container) {
            this.description_of_container = description_of_container;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getMainUrl() {
            return mainUrl;
        }

        public void setMainUrl(String mainUrl) {
            this.mainUrl = mainUrl;
        }
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<urlItem> getImages() {
        return images;
    }

    public void setImages(List<urlItem> images) {
        this.images = images;
    }

    public containerDetails getData() {
        return data;
    }

    public void setData(containerDetails data) {
        this.data = data;
    }

}
