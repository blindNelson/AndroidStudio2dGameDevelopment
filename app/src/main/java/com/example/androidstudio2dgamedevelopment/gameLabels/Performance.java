package com.example.androidstudio2dgamedevelopment.gameLabels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.GameLoop;
import com.example.androidstudio2dgamedevelopment.R;

public class Performance {
    GameLoop gameLoop;
    public Context context;

    public Performance(Context context, GameLoop gameLoop){
        this.context = context;
        this.gameLoop = gameLoop;
    }

    public void draw(Canvas canvas) {
        drawUPS(canvas);
        drawFPS(canvas);
    }

    public void drawUPS(Canvas canvas){
        String avarageUPS = String.format("%,.2f",gameLoop.getAvarageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.red);
        paint.setColor(color);
        paint.setTextSize(25);
        canvas.drawText("UPS: " + avarageUPS,50, 50, paint);
    }

    public void drawFPS(Canvas canvas){
        String avarageFPS = String.format("%,.2f",gameLoop.getAvarageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.red);
        paint.setColor(color);
        paint.setTextSize(25);
        canvas.drawText("FPS: " + avarageFPS,50, 100, paint);
    }
}
