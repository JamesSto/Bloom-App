package cornell.hacks.bloom;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.WindowManager;

public class DrawCircle extends View {
    private int intensity;  // = int from 0 to 100 depending on the intensity of the signal.
    private Paint paint = new Paint();
    private int width = this.getWidth();
    private int height = this.getHeight();
    public DrawCircle(Context context){
        //Empty Constructor
        super(context);
        intensity = 0;
        paint.setAntiAlias(true);
    }

    public void setIntensity(int intensity){
        this.intensity = intensity;
    }


    @Override
    public void onDraw(Canvas canvas){
        setIntensity(100);
        int blueInt = this.getResources().getColor(R.color.blue_500);
        paint.setColor(blueInt);
        paint.setStyle(Paint.Style.FILL);
        Display disp = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        canvas.drawCircle(disp.getWidth()/2, (float) (disp.getHeight()/1.75), intensity, paint);

    }







}
