package dakrory.a7med.cargomarine.Models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import dakrory.a7med.cargomarine.R;
import dakrory.a7med.cargomarine.damageActivity;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

public class ObjectOFImageTag {
    ImageView imageview;
    int x,y;
    ViewGroup viewGroup;
    View currentselectedView;
    QuickAction quickAction;
    RelativeLayout.LayoutParams lp;
    Drawable drawable;
    Context activity;




    int indexOfElement=0;
    private static final int ID_DOWN = 2;
    private static final int ID_ERASE = 5;

    public ObjectOFImageTag(final Context activity, int x, int y, View currentViewRelative, Drawable drawable,int indexOfElement) {
        this.activity=activity;
        this.imageview = new ImageView(activity);
        this.x = x;
        this.y = y;
        this.indexOfElement=indexOfElement;
        this.viewGroup =(ViewGroup) currentViewRelative;
        this.lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.drawable = drawable;



        //Config default color
        QuickAction.setDefaultColor(ResourcesCompat.getColor(activity.getResources(), R.color.mdtp_transparent_black, null));
        QuickAction.setDefaultTextColor(Color.BLACK);

        ActionItem removeItem = new ActionItem(ID_DOWN, "Remove", R.drawable.ic_close_icon);
        ActionItem leaveItem = new ActionItem(ID_ERASE, "Cancel", R.drawable.ic_call_missed_outgoing_black_24dp);


        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
        //orientation
        quickAction = new QuickAction(activity, QuickAction.HORIZONTAL);
        quickAction.setColorRes(R.color.gray);
        quickAction.setTextColorRes(R.color.mdtp_white);
        quickAction.setTextColor(Color.BLACK);
        quickAction.addActionItem(removeItem);
        quickAction.addActionItem(leaveItem);



        //set listener for on dismiss event, this listener will be called only if QuickAction dialog
        // was dismissed
        //by clicking the area outside the dialog.
        quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
            @Override public void onDismiss() {
                Toast.makeText(activity, "Dismissed", Toast.LENGTH_SHORT).show();
            }
        });




        lp.setMargins(x, y, 0, 0);
        imageview.setLayoutParams(lp);
        imageview.setImageDrawable(drawable);
        Log.v("Ahmed",String.valueOf(x));
        Log.v("Ahmed",String.valueOf(this.drawable));
        imageview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ObjectOFImageTag.this.currentselectedView = view;
                ObjectOFImageTag.this.quickAction.show(view);
                return true;
            }
        });

        //Set listener for action item clicked
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override public void onItemClick(ActionItem item) {
                //here we can filter which action item was clicked with pos or actionId parameter
                String title = item.getTitle();
                Toast.makeText(activity, title+" selected", Toast.LENGTH_SHORT).show();

                if(title.equalsIgnoreCase("Remove")) {
                    ObjectOFImageTag.this.viewGroup.removeView(ObjectOFImageTag.this.currentselectedView);
                    damageActivity.allTagsJsonArray.remove(String.valueOf(ObjectOFImageTag.this.indexOfElement));
                }
            }
        });


    }

    public ImageView getImage() {




        return imageview;
    }
}
