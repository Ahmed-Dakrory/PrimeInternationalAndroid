package dakrory.a7med.cargomarine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.io.File;

import dakrory.a7med.cargomarine.Models.vehicalsDetails;
import dakrory.a7med.cargomarine.helpers.Constants;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class ViewFullPdf extends Activity implements DownloadFile.Listener  {
    RelativeLayout root;
    RemotePDFViewPager remotePDFViewPager;
    ProgressBar loader;
    PDFPagerAdapter adapter;
    String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_remote_pdf);
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra(Constants.ImageUrl_INTENT);
        int imageUrlType = intent.getIntExtra(Constants.ImageUrl_Type,0);



        root = (RelativeLayout) findViewById(R.id.remote_pdf_root);
        loader = (ProgressBar) findViewById(R.id.loader);

        if(imageUrlType== vehicalsDetails.TYPE_Server){

            remotePDFViewPager =
                    new RemotePDFViewPager(this, imageUrl, this);

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter.close();
        }
    }







    public void updateLayout() {
        root.removeAllViewsInLayout();
        root.addView(loader,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        root.addView(remotePDFViewPager,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
        loader.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
        Toast.makeText(this,"Problem While Loading",Toast.LENGTH_LONG).show();
        loader.setVisibility(View.GONE);
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }
}