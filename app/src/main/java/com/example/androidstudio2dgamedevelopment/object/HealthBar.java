package com.example.androidstudio2dgamedevelopment.object;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.GameDisplay;
import com.example.androidstudio2dgamedevelopment.R;

public class HealthBar {

    private final Player player;
    private final Paint borderPaint, healthPaint;
    private int width, height, margin;

    /**
     * a barra de vida mostra a vida do personagem na tela
     */


    public HealthBar(Context context, Player player){
        this.player = player;
        this.width = 100;
        this.height = 20;
        this.margin = 2;

        this.borderPaint = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.healthBarBorder);
        borderPaint.setColor(borderColor);

        this.healthPaint = new Paint();
        int healthColor = ContextCompat.getColor(context, R.color.healthBarHealth);
        healthPaint.setColor(healthColor);
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay){
        float x = (float)player.getPositionX();
        float y = (float)player.getPositionY();
        float distanceToPlayer = 30;
        float healthPointsPercentage = (float)player.getHealthPoints()/(float)player.MAX_HEALTH_POINTS;

        //desenhar borda
        float borderLeft, borderTop, borderRight, borderBottom;
        borderLeft = x-width/2;
        borderRight = x+width/2;
        borderBottom = y - distanceToPlayer;
        borderTop = borderBottom - height;

        canvas.drawRect(
                (float) gameDisplay.gameToDisplayCoordinateX(borderLeft),
                (float) gameDisplay.gameToDisplayCoordinateY(borderTop),
                (float) gameDisplay.gameToDisplayCoordinateX(borderRight),
                (float) gameDisplay.gameToDisplayCoordinateY(borderBottom),
                borderPaint
        );

        //desenhar vida
        float healthLeft, healthTop, healthRight, healthBottom, healthWidth, healthHeight;
        healthWidth = width - 2*margin;
        healthHeight = height - 2*margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth*healthPointsPercentage;
        healthBottom = borderBottom - margin;
        healthTop = healthBottom - healthHeight;

        canvas.drawRect(
                (float) gameDisplay.gameToDisplayCoordinateX(healthLeft),
                (float) gameDisplay.gameToDisplayCoordinateY(healthTop),
                (float) gameDisplay.gameToDisplayCoordinateX(healthRight),
                (float) gameDisplay.gameToDisplayCoordinateY(healthBottom),
                healthPaint
        );
    }
}
