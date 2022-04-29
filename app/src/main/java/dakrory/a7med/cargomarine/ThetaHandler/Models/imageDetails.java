package dakrory.a7med.cargomarine.ThetaHandler.Models;

public class imageDetails {

    String url="";
    String fileName ="";
    String size = "";


    public imageDetails(String url, String fileName,String size) {
        this.url = url;
        this.fileName = fileName;
        this.size=size;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
