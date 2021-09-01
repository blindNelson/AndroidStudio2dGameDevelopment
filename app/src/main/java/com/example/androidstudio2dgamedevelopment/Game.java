package com.example.androidstudio2dgamedevelopment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.object.Circle;
import com.example.androidstudio2dgamedevelopment.object.Enemy;
import com.example.androidstudio2dgamedevelopment.object.Player;
import com.example.androidstudio2dgamedevelopment.object.Spell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Spell> spellList = new ArrayList<Spell>();
    private int joystickPointerID =0;


    /*
    * Classe Game controla todos os objetos no jogo e é responsavel por autualizar
    * todos os estados e renderizar todos objetos na tela
    */

    private GameLoop gameLoop;
    private int numberOfSpellsToCast = 0;

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        joystick = new Joystick(100, 600, 70, 40);
        player = new Player(getContext(), joystick,2*500, 500, 30);
        this.gameLoop = new GameLoop(this, surfaceHolder);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(joystick.getIsPressed()) {
                    // o joystick já estava ativado --> novo feitiço
                    numberOfSpellsToCast++;
                    spellList.add(new Spell(getContext(),player));
                }
                else if(joystick.isPressed(event.getX(), event.getY())){
                    // o joystick não estava ativado --> setIsPressed(true)
                    joystickPointerID = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                }
                else{
                    // o joystick não foi ativado --> novo feitiço
                    spellList.add(new Spell(getContext(),player));
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                // joystick ja foi ativado e agora está se movendo
                if(joystick.getIsPressed()){
                    joystick.setActuator(event.getX(), event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerID == event.getPointerId(event.getActionIndex())){
                    // joystick foi desativado --> setIsPressed(false) e reseta o atuador
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
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

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for(Enemy enemy : enemyList){
            enemy.draw(canvas);
        }
        for(Spell spell : spellList){
            spell.draw(canvas);
        }

        player.draw(canvas);
        joystick.draw(canvas);

        drawUPS(canvas);
        drawFPS(canvas);
        drawPLP(canvas);

    }

    public void update() {
        //autualiza o jogo
        joystick.upadate();
        player.update();

        if(Enemy.readyToSpawn()){
            enemyList.add(new Enemy(getContext(), player));
        }

        while (numberOfSpellsToCast > 0){
            spellList.add(new Spell(getContext(), player));
            numberOfSpellsToCast--;
        }

        for (Enemy enemy : enemyList){
            enemy.update();
        }
        for (Spell spell : spellList){
            spell.update();
        }

        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while (iteratorEnemy.hasNext()){
            Circle enemy = iteratorEnemy.next();
            if(Circle.isColliding(enemy, player)){
                iteratorEnemy.remove();
                player.setHealthPoints(player.getHealthPoints() - 1);
                continue;
            }

            Iterator<Spell> iteratorSpell = spellList.iterator();
            while (iteratorSpell.hasNext()) {
                Circle spell = iteratorSpell.next();
                if(Circle.isColliding(spell, enemy)){
                    iteratorSpell.remove();
                    iteratorEnemy.remove();
                    break;
                }
            }
        }

    }

    public void drawUPS(Canvas canvas){
        String avarageUPS = String.format("%,.2f",gameLoop.getAvarageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(super.getContext(), R.color.red);
        paint.setColor(color);
        paint.setTextSize(25);
        canvas.drawText("UPS: " + avarageUPS,50, 50, paint);
    }

    public void drawFPS(Canvas canvas){
        String avarageFPS = String.format("%,.2f",gameLoop.getAvarageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(super.getContext(), R.color.red);
        paint.setColor(color);
        paint.setTextSize(25);
        canvas.drawText("FPS: " + avarageFPS,50, 100, paint);
    }

    public void drawPLP(Canvas canvas){
        String avaragePLP = String.format("%,.2f",player.getHealthPoints());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(super.getContext(), R.color.red);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("PLP: " + avaragePLP,100, 150, paint);
    }
}
