package dakrory.a7med.cargomarine.Models;

import com.google.gson.annotations.SerializedName;

public class userImage {


    @SerializedName("error")
    private boolean error;


    @SerializedName("data")
    private DataOfImage data;

    public class DataOfImage{

        @SerializedName("id")
        private Integer id;


        @SerializedName("image")
        private String image;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public DataOfImage getData() {
        return data;
    }

    public void setData(DataOfImage data) {
        this.data = data;
    }
}
