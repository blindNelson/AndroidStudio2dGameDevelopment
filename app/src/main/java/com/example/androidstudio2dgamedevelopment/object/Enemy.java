package com.example.androidstudio2dgamedevelopment.object;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.GameLoop;
import com.example.androidstudio2dgamedevelopment.R;

public class Enemy extends Circle {


    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND*0.6;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private Player player;

    public Enemy(Context context, Player player , double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);

        this.player = player;

    }

    @Override
    public void update() {
        //calcular o vetor do inimigo para o player(em x e y)
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;

        //calcular a distância absoluta entre o inimigo e o player
        double distanceToPlayer = GameObject.getDistanceBeetwenObjects(this, player);

        //calcular direção do inimigo para o player
        double distanceX = distanceToPlayerX/distanceToPlayer;
        double distanceY  = distanceToPlayerY/distanceToPlayer;

        //salvar valores da velocidade e da distância para o player
        if(distanceToPlayer > 0){
            velocityX = distanceX*MAX_SPEED;
            velocityY = distanceY*MAX_SPEED;
        }
        else {
            velocityX = 0;
            velocityY = 0;
        }

        //atualizar posição do inimigo
        positionX+=velocityX;
        positionY+=velocityY;

    }
}