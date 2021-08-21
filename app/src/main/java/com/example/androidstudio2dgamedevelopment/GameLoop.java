package com.example.androidstudio2dgamedevelopment;

import android.graphics.Canvas;
import android.view.SurfaceHolder;



public class GameLoop extends Thread {

    private static final double MAX_UPS = 30.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;
    private boolean isRunning = false;
    private SurfaceHolder surfaceHolder;
    private Game game;
    private double avarageUPS;
    private double avarageFPS;


    public GameLoop(Game game, SurfaceHolder surfaceHolder) {

        this.game = game;
        this.surfaceHolder = surfaceHolder;

    }



    public void startLoop() {
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();

        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        //game loop
        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        while (isRunning){


            //tentar renderizar e autualizar objetos
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    game.update();
                    game.draw(canvas);
                }
            }
            catch(IllegalArgumentException e){
                e.printStackTrace();
            }
            finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

            updateCount++;
            frameCount++;

            //'Pause game loop to not exceed target UPS'

            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long)(updateCount*UPS_PERIOD-elapsedTime);
            if(sleepTime > 0){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //pular frames para acompanhar o tergeto

            while(sleepTime < 0 && updateCount < MAX_UPS-1){
                game.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long)(updateCount*UPS_PERIOD-elapsedTime);
            }

            //calcular a taxa de UPS e FPS
            elapsedTime = System.currentTimeMillis() - startTime;
            if(elapsedTime >= 1000){
                avarageUPS = updateCount / (1E-3 * elapsedTime);
                avarageFPS = frameCount / (1E-3 * elapsedTime);
                updateCount=0;
                frameCount=0;
                startTime = System.currentTimeMillis();
            }

        }
    }



    public double getAvarageUPS() {
        return avarageUPS;
    }

    public double getAvarageFPS() {
        return avarageFPS;
    }

}
