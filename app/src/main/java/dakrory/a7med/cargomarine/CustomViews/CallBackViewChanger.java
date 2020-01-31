package dakrory.a7med.cargomarine.CustomViews;

import android.widget.TextView;

import com.app.adprogressbarlib.AdCircleProgress;

public interface CallBackViewChanger {
    void setViewToPercentage(AdCircleProgress loader, TextView overlayView, TextView markView);
}
