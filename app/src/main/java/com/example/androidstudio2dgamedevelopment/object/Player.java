package com.example.androidstudio2dgamedevelopment.object;

import android.content.Context;
import android.graphics.Canvas;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.GameLoop;
import com.example.androidstudio2dgamedevelopment.Joystick;
import com.example.androidstudio2dgamedevelopment.R;
import com.example.androidstudio2dgamedevelopment.Utils;

public class Player extends Circle{
    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    public static final int MAX_HEALTH_POINTS = 10;
    private final Joystick joystick;
    protected double directionX = 1;
    protected double directionY = 1;
    private HealthBar healthBar;
    private int healthPoints;

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);

        this.joystick = joystick;
        this.healthBar = new HealthBar(context, this);
        this.healthPoints = MAX_HEALTH_POINTS;


    }

    public void update() {
        //atualizar velocidade
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;

        //atualizar posição
        positionX += velocityX;
        positionY += velocityY;

        //atualizar direção
        if(velocityX != 0 || velocityY != 0){
            double distance = Utils.getDistanceBeetwenObjects(0, 0, velocityX, velocityY);
            directionX = velocityX/distance;
            directionY = velocityY/distance;
        }
    }

    public void setPosition(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public double getDirectionX() {
        return directionX;
    }
    public double getDirectionY() {
        return directionY;
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        healthBar.draw(canvas);
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        if(healthPoints>0)
            this.healthPoints = healthPoints;
    }
}
