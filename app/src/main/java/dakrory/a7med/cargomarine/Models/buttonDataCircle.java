package dakrory.a7med.cargomarine.Models;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class buttonDataCircle {
    String name="";
    Drawable drawableImage;
    String description="";
    int type=0;



    public Bitmap BITMAP_RESIZER(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(resizedBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return resizedBitmap;

    }

    public buttonDataCircle(String name, Drawable drawableImage, String description, Activity activity,int type) {
        this.name = name;
        this.drawableImage = drawableImage;
        this.type= type;
        Bitmap bitmap = ((BitmapDrawable) this.drawableImage).getBitmap();
        // Scale it to 50 x 50



        this.drawableImage = new BitmapDrawable(activity.getResources(), BITMAP_RESIZER(bitmap, 100, 100));

        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Drawable getDrawableImage() {
        return drawableImage;
    }

    public void setDrawableImage(Drawable drawableImage) {
        this.drawableImage = drawableImage;
    }
}
