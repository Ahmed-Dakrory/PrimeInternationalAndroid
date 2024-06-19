package dakrory.a7med.cargomarine.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class containersDataAllList {
    @SerializedName("error")
    private String error;

    @SerializedName("data")
    private List<containerItemOfAllList> data;

    public class containerItemOfAllList{


        @SerializedName("id")
        private int id;


        @SerializedName("datetime")
        private Date datetime;

        @SerializedName("container_number")
        private String container_number;

        @SerializedName("description_of_container")
        private String description_of_container;


        @SerializedName("mainUrl")
        private String mainUrl;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Date getDatetime() {
            return datetime;
        }

        public void setDatetime(Date datetime) {
            this.datetime = datetime;
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


    public List<containerItemOfAllList> getData() {
        return data;
    }

    public void setData(List<containerItemOfAllList> data) {
        this.data = data;
    }
}
