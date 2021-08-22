package com.example.androidstudio2dgamedevelopment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;





public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;

    /*
    * Classe Game controla todos os objetos no jogo e é responsavel por autualizar
    * todos os estados e renderizar todos objetos na tela
    */

    private GameLoop gameLoop;

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        player = new Player(getContext(), 500, 500, 30);
        this.gameLoop = new GameLoop(this, surfaceHolder);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                player.setPosition(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                player.setPosition(event.getX(), event.getY());
                return true;

        }

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void drawUPS(Canvas canvas){
        String avarageUPS = Double.toString(gameLoop.getAvarageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(super.getContext(), R.color.red);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + avarageUPS,100, 80, paint);
    }

    public void drawFPS(Canvas canvas){
        String avarageFPS = Double.toString(gameLoop.getAvarageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(super.getContext(), R.color.red);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + avarageFPS,100, 150, paint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);
        player.draw(canvas);
    }

    public void update() {
        //autualiza o jogo
        player.update();
    }
}
