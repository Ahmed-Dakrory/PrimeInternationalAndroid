package dakrory.a7med.cargomarine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dakrory.a7med.cargomarine.CustomViews.buttonCirculeAdapter;
import dakrory.a7med.cargomarine.CustomViews.vehicalsAdapter;
import dakrory.a7med.cargomarine.Models.ObjectOFImageTag;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;
import dakrory.a7med.cargomarine.CustomViews.buttonCirculeAdapter;
import dakrory.a7med.cargomarine.Models.buttonDataCircle;

public class damageActivity extends Activity implements View.OnClickListener {


    ImageButton damage1;
    ImageButton damage2;

    public static Drawable drawableImage;
    public static int drawableImageType;

    RecyclerView mRecentRecyclerView;
    LinearLayoutManager mRecentLayoutManager;
    ImageView imageOfMainVehicle;

    public static buttonCirculeAdapter buttonAdapter;

    List<ObjectOFImageTag> allTags;
    Button removeAllButtonCircles;
    Button saveAllTags;
    Button returnBack;
    RelativeLayout rr;

    public static JSONObject allTagsJsonArray;
    public static int indexOfElement=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        try {
            allTagsJsonArray=new JSONObject("{'length':0}");
            indexOfElement = 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent data = getIntent();
        String carType = data.getStringExtra("TYPE");

        Log.v("AhmedType",carType);

        setContentView(R.layout.activity_damage);


        imageOfMainVehicle = (ImageView) findViewById(R.id.imageOfMainVehicle);
        saveAllTags = (Button) findViewById(R.id.saveallTags);
        returnBack = (Button) findViewById(R.id.returnback);

        mRecentRecyclerView = (RecyclerView) findViewById(R.id.allCirculesNotes);
        mRecentRecyclerView.setHasFixedSize(true);
        mRecentLayoutManager = new LinearLayoutManager(this);
        mRecentRecyclerView.setLayoutManager(mRecentLayoutManager);
        allTags =new ArrayList<ObjectOFImageTag>();
        removeAllButtonCircles = (Button) findViewById(R.id.removeAllButtonCircles);

        saveAllTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicalView.carData.getData().setCarAllTagsOfCrash(allTagsJsonArray.toString());
                rr.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(rr.getDrawingCache());
                rr.setDrawingCacheEnabled(false);
                vehicalView.bitmapToFile(damageActivity.this,bitmap,"car");
            }
        });
        removeAllButtonCircles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<allTags.size();i++){

                    ((ViewGroup) rr).removeView(allTags.get(i).getImage());
                    indexOfElement = 0;

                    try {
                        allTagsJsonArray=new JSONObject("{'length':0}");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        fillListAdapter();
        mRecentRecyclerView.setAdapter(buttonAdapter);




        switch (carType){
            case "SUV":

                imageOfMainVehicle.setImageDrawable(getResources().getDrawable(R.mipmap.suv));
                break;

            case "Coupe":

                imageOfMainVehicle.setImageDrawable(getResources().getDrawable(R.mipmap.coupe));
                break;

            case "Sedan":

                imageOfMainVehicle.setImageDrawable(getResources().getDrawable(R.mipmap.sedan));
                break;

            case "Van":

                imageOfMainVehicle.setImageDrawable(getResources().getDrawable(R.mipmap.van));
                break;

            case "Truck (day cab)":

                imageOfMainVehicle.setImageDrawable(getResources().getDrawable(R.mipmap.truck_daycab));
                break;

            case "Truck (With Sleeper)":

                imageOfMainVehicle.setImageDrawable(getResources().getDrawable(R.mipmap.truck_withsleeper));
                break;


            case "Motor Cycle":

                imageOfMainVehicle.setImageDrawable(getResources().getDrawable(R.mipmap.motorcycle));
                break;

            case "Pickup 2 Doors":

                imageOfMainVehicle.setImageDrawable(getResources().getDrawable(R.mipmap.pickup2doors));
                break;

            case "Pickup 4 Doors":


                imageOfMainVehicle.setImageDrawable(getResources().getDrawable(R.mipmap.pickup4doors));
                break;

            default:

                imageOfMainVehicle.setImageDrawable(getResources().getDrawable(R.mipmap.other));
        }


        rr = (RelativeLayout) findViewById(R.id.imageContainer);

        try {
            Log.v("AhmedNew",String.valueOf(vehicalView.carData.getData().getCarAllTagsOfCrash()));
            JSONObject allPoints =new JSONObject(vehicalView.carData.getData().getCarAllTagsOfCrash());


            for(int i=0;i<allPoints.getInt("length");i++){
            try {
                JSONObject item = allPoints.getJSONObject(String.valueOf(i));
                int x = item.getInt("x");
                int y = item.getInt("y");
                int type = item.getInt("TYPE");

                switch (type) {
                    case 0:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.ms));
                        break;
                    case 1:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.s));
                        break;
                    case 2:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.br));
                        break;
                    case 3:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.ch));
                        break;
                    case 4:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.cr));
                        break;
                    case 5:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.d));
                        break;
                    case 6:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.f));
                        break;
                    case 7:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.ff));
                        break;
                    case 8:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.ft));
                        break;
                    case 9:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.g));
                        break;
                    case 10:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.hd));
                        break;
                    case 11:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.lc));
                        break;
                    case 12:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.m));
                        break;
                    case 13:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.md));
                        break;

                    case 14:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.o));
                        break;
                    case 15:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.pc));
                        break;
                    case 16:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.r));
                        break;
                    case 17:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.ru));
                        break;

                    default:
                        drawableImage = BITMAP_RESIZER(getResources().getDrawable(R.mipmap.sc));
                        break;

                }


                ObjectOFImageTag imageTag = new ObjectOFImageTag(getApplicationContext(), x, y, rr,
                        drawableImage, indexOfElement);

                allTags.add(imageTag);
                JSONObject dataJson = new JSONObject();
                dataJson.put("x", x);
                dataJson.put("y", y);
                dataJson.put("TYPE", type);
                allTagsJsonArray.put(String.valueOf(indexOfElement), dataJson);
                allTagsJsonArray.put("length", String.valueOf(indexOfElement + 1));
                indexOfElement++;


                ((ViewGroup) rr).addView(imageTag.getImage());
            }catch (Exception e){

            }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception ec){

        }



        rr.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    x=x-50;
                    y=y-50;

                    try {
                    ObjectOFImageTag imageTag =new ObjectOFImageTag(getApplicationContext(),x,y, v,
                            drawableImage,indexOfElement);

                    allTags.add(imageTag);
                    JSONObject dataJson = new JSONObject();

                    dataJson.put("x",x);
                    dataJson.put("y",y);
                    dataJson.put("TYPE",drawableImageType);
                    allTagsJsonArray.put(String.valueOf(indexOfElement),dataJson);
                    allTagsJsonArray.put("length",String.valueOf(indexOfElement+1));
                    indexOfElement++;
                    ((ViewGroup) rr).addView(imageTag.getImage());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }



    public List<buttonDataCircle> allCircles;
    public static Button lastSelectedButton = null;
    private void fillListAdapter() {

        addAllButtons();
        buttonAdapter = new buttonCirculeAdapter(allCircles,damageActivity.this);
    }

    private void addAllButtons() {

        allCircles = new ArrayList<buttonDataCircle>();
        allCircles.add(new buttonDataCircle("MS",getResources().getDrawable(R.mipmap.ms),"Multiple Scratches",damageActivity.this,0));
        allCircles.add(new buttonDataCircle("S",getResources().getDrawable(R.mipmap.s),"Scratched",damageActivity.this,1));
        allCircles.add(new buttonDataCircle("BR",getResources().getDrawable(R.mipmap.br),"Broken",damageActivity.this,2));
        allCircles.add(new buttonDataCircle("CH",getResources().getDrawable(R.mipmap.ch),"Chipped",damageActivity.this,3));
        allCircles.add(new buttonDataCircle("CR",getResources().getDrawable(R.mipmap.cr),"Cracked",damageActivity.this,4));
        allCircles.add(new buttonDataCircle("D",getResources().getDrawable(R.mipmap.d),"Dent",damageActivity.this,5));
        allCircles.add(new buttonDataCircle("F",getResources().getDrawable(R.mipmap.f),"Faded",damageActivity.this,6));
        allCircles.add(new buttonDataCircle("FF",getResources().getDrawable(R.mipmap.ff),"Foreign Fluid",damageActivity.this,7));
        allCircles.add(new buttonDataCircle("FT",getResources().getDrawable(R.mipmap.ft),"Flat Tire",damageActivity.this,8));
        allCircles.add(new buttonDataCircle("g",getResources().getDrawable(R.mipmap.g),"Gouge",damageActivity.this,9));
        allCircles.add(new buttonDataCircle("HD",getResources().getDrawable(R.mipmap.hd),"Hail Damage",damageActivity.this,10));
        allCircles.add(new buttonDataCircle("LC",getResources().getDrawable(R.mipmap.lc),"Loose Contents",damageActivity.this,11));
        allCircles.add(new buttonDataCircle("M",getResources().getDrawable(R.mipmap.m),"Missing",damageActivity.this,12));
        allCircles.add(new buttonDataCircle("MD",getResources().getDrawable(R.mipmap.md),"Major Damage",damageActivity.this,13));
        allCircles.add(new buttonDataCircle("O",getResources().getDrawable(R.mipmap.o),"Other",damageActivity.this,14));
        allCircles.add(new buttonDataCircle("PC",getResources().getDrawable(R.mipmap.pc),"Paint Chip",damageActivity.this,15));
        allCircles.add(new buttonDataCircle("R",getResources().getDrawable(R.mipmap.r),"Rubbed",damageActivity.this,16));
        allCircles.add(new buttonDataCircle("RU",getResources().getDrawable(R.mipmap.ru),"Rust",damageActivity.this,17));
        allCircles.add(new buttonDataCircle("SC",getResources().getDrawable(R.mipmap.sc),"Scuffed",damageActivity.this,18));

    }


    @Override
    public void onClick(View v) {

    }



    public Drawable BITMAP_RESIZER(Drawable d) {
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        // Scale it to 50 x 50


        int newWidth = 100;
        int newHeight = 100;
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

        return new BitmapDrawable(getResources(),resizedBitmap);

    }


}
