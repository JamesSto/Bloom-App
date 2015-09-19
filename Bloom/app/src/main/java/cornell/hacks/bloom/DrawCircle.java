package cornell.hacks.bloom;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DrawCircle extends View {
    private int intensity;  // = int from 0 to 100 depending on the intensity of the signal.

    public DrawCircle(Context context){
        //Empty Constructor
        super(context);
        intensity = 0;
        init();
    }

    public void setIntensity(int intensity){
        this.intensity = intensity;
    }

    //Initialize the paint
    private void init(){
        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    }





}
