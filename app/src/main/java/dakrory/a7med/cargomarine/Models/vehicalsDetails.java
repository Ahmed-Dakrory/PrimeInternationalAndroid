package dakrory.a7med.cargomarine.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import dakrory.a7med.cargomarine.CustomViews.CallBackViewChanger;

public class vehicalsDetails {

    public static int TYPE_FILE=0;
    public static int TYPE_Server=1;

    @SerializedName("error")
    private String error;

    @SerializedName("images")
    private List<urlItem> images;


    @SerializedName("images3D")
    private List<urlItem> images3D;

    @SerializedName("docs")
    private List<urlItem> docs;

    @SerializedName("pdfs")
    private List<urlItem> pdfs;

    @SerializedName("data")
    private carDetails data;


    @SerializedName("allshippers")
    private List<modelIdAndName> allshippers;



    public static class modelIdAndName {
        public modelIdAndName(String name, int id) {
            this.name = name;
            this.id = id;
        }


        @SerializedName("name")
        private String name;


        @SerializedName("id")
        private int id;



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

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


    public static class carDetails{


        @SerializedName("id")
        private int id;

        @SerializedName("mainId")
        private int mainId;


        @SerializedName("mainTwoId")
        private int mainTwoId;

        @SerializedName("shipperId")
        private int shipperId;

        @SerializedName("vendorId")
        private int vendorId;


        @SerializedName("customerId")
        private int customerId;


        @SerializedName("consigneeId")
        private int consigneeId;






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



        @SerializedName("type")
        private String type="";


        @SerializedName("bodyStyle")
        private String bodyStyle="";


        @SerializedName("exteriorImg")
        private String exteriorImg="";


        @SerializedName("keyExist")
        private boolean keyExist=false;


        @SerializedName("exteriorExists")
        private boolean exteriorExists=false;



        @SerializedName("titleExist")
        private boolean titleExist=false;


        @SerializedName("engineType")
        private String engineType="";



        @SerializedName("engineLiters")
        private String engineLiters="";


        @SerializedName("assemlyCountry")
        private String assemlyCountry="";


        @SerializedName("color")
        private String color="";

        @SerializedName("seacost")
        private float seacost;



        @SerializedName("landcost")
        private float landcost;


        @SerializedName("state")
        private int state;





        @SerializedName("releaseOption")
        private int releaseOption;


        @SerializedName("stateOut")
        private int stateOut;

        @SerializedName("releaseDate")
        private String releaseDate="";


        @SerializedName("uuid")
        private String uuid="";



        @SerializedName("description")
        private String description="";



        @SerializedName("containerLink")
        private String containerLink="";


        @SerializedName("weight")
        private String weight="";


        @SerializedName("eta")
        private String eta="";



        @SerializedName("etd")
        private String etd="";



        @SerializedName("dateOfDriverSignture")
        private String dateOfDriverSignture="";





        @SerializedName("urlOfDriverSignture")
        private String urlOfDriverSignture="";



        @SerializedName("driverName")
        private String driverName="";


        @SerializedName("driverPhone")
        private String driverPhone="";

        @SerializedName("companyTransName")
        private String companyTransName="";






        @SerializedName("dateOfCrashImage")
        private String dateOfCrashImage="";


        @SerializedName("urlOfCrashImage")
        private String urlOfCrashImage="";


        @SerializedName("crashPointsJson")
        private String crashPointsJson="";


        @SerializedName("CarType")
        private String CarType="";


        @SerializedName("numberOfKeys")
        private int numberOfKeys;

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public int getNumberOfKeys() {
            return numberOfKeys;
        }

        public void setNumberOfKeys(int numberOfKeys) {
            this.numberOfKeys = numberOfKeys;
        }

        public String getCarType() {
            return CarType;
        }

        public void setCarType(String carType) {
            CarType = carType;
        }

        public String getDateOfCrashImage() {
            return dateOfCrashImage;
        }

        public void setDateOfCrashImage(String dateOfCrashImage) {
            this.dateOfCrashImage = dateOfCrashImage;
        }


        public boolean isExteriorExists() {
            return exteriorExists;
        }

        public void setExteriorExists(boolean exteriorExists) {
            this.exteriorExists = exteriorExists;
        }

        public String getUrlOfCrashImage() {
            return urlOfCrashImage;
        }

        public void setUrlOfCrashImage(String urlOfCrashImage) {
            this.urlOfCrashImage = urlOfCrashImage;
        }

        public String getCrashPointsJson() {
            return crashPointsJson;
        }

        public void setCrashPointsJson(String crashPointsJson) {
            this.crashPointsJson = crashPointsJson;
        }

        public String getExteriorImg() {
            return exteriorImg;
        }

        public void setExteriorImg(String exteriorImg) {
            this.exteriorImg = exteriorImg;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDateOfDriverSignture() {
            return dateOfDriverSignture;
        }

        public void setDateOfDriverSignture(String dateOfDriverSignture) {
            this.dateOfDriverSignture = dateOfDriverSignture;
        }

        public String getUrlOfDriverSignture() {
            return urlOfDriverSignture;
        }

        public void setUrlOfDriverSignture(String urlOfDriverSignture) {
            this.urlOfDriverSignture = urlOfDriverSignture;
        }

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

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getDriverPhone() {
            return driverPhone;
        }

        public void setDriverPhone(String driverPhone) {
            this.driverPhone = driverPhone;
        }

        public String getCompanyTransName() {
            return companyTransName;
        }

        public void setCompanyTransName(String companyTransName) {
            this.companyTransName = companyTransName;
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

        public float getSeacost() {
            return seacost;
        }

        public void setSeacost(float seacost) {
            this.seacost = seacost;
        }

        public float getLandcost() {
            return landcost;
        }

        public void setLandcost(float landcost) {
            this.landcost = landcost;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getReleaseOption() {
            return releaseOption;
        }

        public void setReleaseOption(int releaseOption) {
            this.releaseOption = releaseOption;
        }

        public int getStateOut() {
            return stateOut;
        }

        public void setStateOut(int stateOut) {
            this.stateOut = stateOut;
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

        public int getMainId() {
            return mainId;
        }

        public void setMainId(int mainId) {
            this.mainId = mainId;
        }

        public int getMainTwoId() {
            return mainTwoId;
        }

        public void setMainTwoId(int mainTwoId) {
            this.mainTwoId = mainTwoId;
        }

        public int getShipperId() {
            return shipperId;
        }

        public void setShipperId(int shipperId) {
            this.shipperId = shipperId;
        }

        public int getVendorId() {
            return vendorId;
        }

        public void setVendorId(int vendorId) {
            this.vendorId = vendorId;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public int getConsigneeId() {
            return consigneeId;
        }

        public void setConsigneeId(int consigneeId) {
            this.consigneeId = consigneeId;
        }

        public boolean isTitleExist() {
            return titleExist;
        }

        public void setTitleExist(boolean titleExist) {
            this.titleExist = titleExist;
        }

        public boolean isKeyExist() {
            return keyExist;
        }

        public void setKeyExist(boolean keyExist) {
            this.keyExist = keyExist;
        }
    }

    public List<modelIdAndName> getAllshippers() {
        return allshippers;
    }

    public void setAllshippers(List<modelIdAndName> allshippers) {
        this.allshippers = allshippers;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<urlItem> getImages3D() {
        return images3D;
    }

    public void setImages3D(List<urlItem> images3D) {
        this.images3D = images3D;
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

    public List<urlItem> getPdfs() {
        return pdfs;
    }

    public void setPdfs(List<urlItem> pdfs) {
        this.pdfs = pdfs;
    }
}
