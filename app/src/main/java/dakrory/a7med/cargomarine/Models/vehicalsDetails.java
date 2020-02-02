package dakrory.a7med.cargomarine.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class vehicalsDetails {

    public static int TYPE_FILE=0;
    public static int TYPE_Server=1;

    @SerializedName("error")
    private String error;

    @SerializedName("images")
    private List<urlItem> images;

    @SerializedName("docs")
    private List<urlItem> docs;

    @SerializedName("data")
    private carDetails data;




    public class urlItem{
        @SerializedName("url")
        private String url;


        @SerializedName("type")
        private int type;


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


    public static class carDetails{


        @SerializedName("id")
        private int id;


        @SerializedName("userfirstName")
        private String userfirstName="";


        @SerializedName("userlastName")
        private String userlastName="";


        @SerializedName("mainTwofirstName")
        private String mainTwofirstName="";


        @SerializedName("mainTwolastName")
        private String mainTwolastName="";


        @SerializedName("shipperfirstName")
        private String shipperfirstName="";

        @SerializedName("shipperlastName")
        private String shipperlastName="";

        @SerializedName("vendorfirstName")
        private String vendorfirstName="";


        @SerializedName("vendorlastName")
        private String vendorlastName="";


        @SerializedName("consigneefirstName")
        private String consigneefirstName="";

        @SerializedName("consigneelastName")
        private String consigneelastName="";

        @SerializedName("customerfirstName")
        private String customerfirstName="";

        @SerializedName("customerlastName")
        private String customerlastName="";


        @SerializedName("make")
        private String make="";


        @SerializedName("model")
        private String model="";


        @SerializedName("year")
        private String year="";


        @SerializedName("bodyStyle")
        private String bodyStyle="";


        @SerializedName("engineType")
        private String engineType="";



        @SerializedName("engineLiters")
        private String engineLiters="";


        @SerializedName("assemlyCountry")
        private String assemlyCountry="";


        @SerializedName("color")
        private String color="";

        @SerializedName("seacost")
        private String seacost="";



        @SerializedName("landcost")
        private String landcost="";


        @SerializedName("state")
        private String state="";


        @SerializedName("releaseOption")
        private String releaseOption="";

        @SerializedName("releaseDate")
        private String releaseDate="";


        @SerializedName("uuid")
        private String uuid="";



        @SerializedName("description")
        private String description="";



        @SerializedName("containerLink")
        private String containerLink="";


        @SerializedName("eta")
        private String eta="";



        @SerializedName("etd")
        private String etd="";


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserfirstName() {
            return userfirstName;
        }

        public void setUserfirstName(String userfirstName) {
            this.userfirstName = userfirstName;
        }

        public String getUserlastName() {
            return userlastName;
        }

        public void setUserlastName(String userlastName) {
            this.userlastName = userlastName;
        }

        public String getMainTwofirstName() {
            return mainTwofirstName;
        }

        public void setMainTwofirstName(String mainTwofirstName) {
            this.mainTwofirstName = mainTwofirstName;
        }

        public String getMainTwolastName() {
            return mainTwolastName;
        }

        public void setMainTwolastName(String mainTwolastName) {
            this.mainTwolastName = mainTwolastName;
        }

        public String getShipperfirstName() {
            return shipperfirstName;
        }

        public void setShipperfirstName(String shipperfirstName) {
            this.shipperfirstName = shipperfirstName;
        }

        public String getShipperlastName() {
            return shipperlastName;
        }

        public void setShipperlastName(String shipperlastName) {
            this.shipperlastName = shipperlastName;
        }

        public String getVendorfirstName() {
            return vendorfirstName;
        }

        public void setVendorfirstName(String vendorfirstName) {
            this.vendorfirstName = vendorfirstName;
        }

        public String getVendorlastName() {
            return vendorlastName;
        }

        public void setVendorlastName(String vendorlastName) {
            this.vendorlastName = vendorlastName;
        }

        public String getConsigneefirstName() {
            return consigneefirstName;
        }

        public void setConsigneefirstName(String consigneefirstName) {
            this.consigneefirstName = consigneefirstName;
        }

        public String getConsigneelastName() {
            return consigneelastName;
        }

        public void setConsigneelastName(String consigneelastName) {
            this.consigneelastName = consigneelastName;
        }

        public String getCustomerfirstName() {
            return customerfirstName;
        }

        public void setCustomerfirstName(String customerfirstName) {
            this.customerfirstName = customerfirstName;
        }

        public String getCustomerlastName() {
            return customerlastName;
        }

        public void setCustomerlastName(String customerlastName) {
            this.customerlastName = customerlastName;
        }

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getBodyStyle() {
            return bodyStyle;
        }

        public void setBodyStyle(String bodyStyle) {
            this.bodyStyle = bodyStyle;
        }

        public String getEngineType() {
            return engineType;
        }

        public void setEngineType(String engineType) {
            this.engineType = engineType;
        }

        public String getEngineLiters() {
            return engineLiters;
        }

        public void setEngineLiters(String engineLiters) {
            this.engineLiters = engineLiters;
        }

        public String getAssemlyCountry() {
            return assemlyCountry;
        }

        public void setAssemlyCountry(String assemlyCountry) {
            this.assemlyCountry = assemlyCountry;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSeacost() {
            return seacost;
        }

        public void setSeacost(String seacost) {
            this.seacost = seacost;
        }

        public String getLandcost() {
            return landcost;
        }

        public void setLandcost(String landcost) {
            this.landcost = landcost;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getReleaseOption() {
            return releaseOption;
        }

        public void setReleaseOption(String releaseOption) {
            this.releaseOption = releaseOption;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContainerLink() {
            return containerLink;
        }

        public void setContainerLink(String containerLink) {
            this.containerLink = containerLink;
        }

        public String getEta() {
            return eta;
        }

        public void setEta(String eta) {
            this.eta = eta;
        }

        public String getEtd() {
            return etd;
        }

        public void setEtd(String etd) {
            this.etd = etd;
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

    public List<urlItem> getDocs() {
        return docs;
    }

    public void setDocs(List<urlItem> docs) {
        this.docs = docs;
    }

    public carDetails getData() {
        return data;
    }

    public void setData(carDetails data) {
        this.data = data;
    }
}
