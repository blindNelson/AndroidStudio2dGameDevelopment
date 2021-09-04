package com.example.androidstudio2dgamedevelopment.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.androidstudio2dgamedevelopment.R;

public class SpriteSheet {
    private Bitmap bitmap;


    public SpriteSheet(Context context){
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_sheet, bitmapOptions);
    }

    public Sprite getPlayerSprite(){
        return new Sprite(this, new Rect(64, 0, 128, 64));
    }

    public Sprite getEnemySprite(){
        return new Sprite(this, new Rect(0, 0, 64, 64));
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
