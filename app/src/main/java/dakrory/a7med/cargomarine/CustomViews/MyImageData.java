package dakrory.a7med.cargomarine.CustomViews;


import java.io.File;

public class MyImageData {
    private String description;
    private String imgUrl;
    private String image;
    private int Type;
    CallBackViewChanger callBackViewChanger;
    public static int TYPE_FILE=0;
    public static int TYPE_Server=1;
    public MyImageData(String description, String imgUrl, String image,int Type,CallBackViewChanger callBackViewChanger) {
        this.description = description;
        this.imgUrl = imgUrl;
        this.image=image;
        this.Type= Type;
        this.callBackViewChanger=callBackViewChanger;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}