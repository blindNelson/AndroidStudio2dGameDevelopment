package com.example.androidstudio2dgamedevelopment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.gameLabels.GameOver;
import com.example.androidstudio2dgamedevelopment.gameLabels.Joystick;
import com.example.androidstudio2dgamedevelopment.gameLabels.Performance;
import com.example.androidstudio2dgamedevelopment.graphics.Sprite;
import com.example.androidstudio2dgamedevelopment.graphics.SpriteSheet;
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
    private final SpriteSheet spriteSheet;


    /*
    * Classe Game controla todos os objetos no jogo e é responsavel por autualizar
    * todos os estados e renderizar todos objetos na tela
    */

    private GameLoop gameLoop;
    private int numberOfSpellsToCast = 0;
    private GameOver gameOver;
    private Performance performance;
    private GameDisplay gameDisplay;

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        this.gameLoop = new GameLoop(this, surfaceHolder);

        // initialize game labels
        gameOver = new GameOver(getContext());
        performance = new Performance(getContext(), gameLoop);

        // initialize game objects
        joystick = new Joystick(100, 600, 70, 40);
        spriteSheet = new SpriteSheet(context);
        player = new Player(getContext(), joystick,2*500, 500, 32, spriteSheet.getPlayerSprite());

        //initialize game display and set around the player
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

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
        Log.d("Game.java", "surfaceCreated()");
        if(gameLoop.getState().equals(Thread.State.TERMINATED)) {
            gameLoop = new GameLoop(this, holder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("Game.java", "surfaceChanged()");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("Game.java", "surfaceDestroyed()");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        for(Enemy enemy : enemyList){
            enemy.draw(canvas, gameDisplay);
        }
        for(Spell spell : spellList){
            spell.draw(canvas, gameDisplay);
        }

        player.draw(canvas, gameDisplay);
        joystick.draw(canvas);

        // Draw gameover if the player is dead
        if(player.getHealthPoints()<=0){
            gameOver.draw(canvas);
        }
        performance.draw(canvas);

    }

    public void update() {

        //parar de atualizar o jogo se o Player morreu
        if(player.getHealthPoints()<=0){
            return;
        }

        //autualiza o jogo
        joystick.upadate();
        player.update();

        if(Enemy.readyToSpawn()){
            enemyList.add(new Enemy(getContext(), player, spriteSheet.getEnemySprite()));
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
        gameDisplay.update();
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
