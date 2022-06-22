package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Helpers.Font;
import com.mygdx.game.Helpers.GameInfo;

public class Board extends Group {
    Group group;
    Table[] table =new Table[100];

    public Board(float height,float x,float y) {
        group= new Group();
        Label.LabelStyle style= new Label.LabelStyle();
        style.font= getNormalFont(GameInfo.WIDTH * 0.01f);
        style.fontColor= Color.BROWN;

        int labelNo=0;
        int tableNo=0;
        float locY= height / 40f;

        for(int j=0;j<10;j++) {

            if (j% 2 == 0) {
                float locX=Gdx.graphics.getWidth()/20f;
                for (int i = 0; i < 10; i++) {
                    table[tableNo] = new Table();
                    labelNo = labelNo + 1;
                    Label label = new Label(" " + labelNo, style);
                    label.setAlignment(Align.topLeft);

                    Image cellImg= new Image(new Texture("cell.png"));
                    if(labelNo%2!=0) {
                        cellImg.setColor(Color.valueOf("#dff964"));
                    }
                    else{
                        cellImg.setColor(Color.valueOf("#b5e72c"));
                    }
                    table[tableNo].stack(cellImg, label).width(GameInfo.WIDTH / 10f).height(height / 20f);

                    table[tableNo].setPosition(locX, locY);
                    locX += Gdx.graphics.getWidth() / 10f;
                    group.addActor(table[tableNo]);
                    tableNo++;
//                    table[tableNo].pack();
                }
            } else {
                float locX=Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/20f;
                for (int i = 0; i < 10; i++) {
                    table[tableNo] = new Table();
                    labelNo = labelNo + 1;
                    Label label = new Label(" " + labelNo, style);
                    label.setAlignment(Align.topLeft);
                    Image cellImg= new Image(new Texture("cell.png"));
                    if(labelNo%2!=0) {
                        cellImg.setColor(Color.valueOf("#dff964"));
                    }
                    else{
                        cellImg.setColor(Color.valueOf("#b5e72c"));
                    }
                    table[tableNo].stack(cellImg, label).width(Gdx.graphics.getWidth() / 10f).height(height / 20f);
                    table[tableNo].setPosition(locX, locY);
                    locX -= Gdx.graphics.getWidth() / 10f;
                    group.addActor(table[tableNo]);
                    tableNo++;
//                    table[tableNo].pack();
                }
            }
            locY=locY+height / 20f;

        }

        group.setPosition(x,y );

    }


    private BitmapFont getNormalFont(float size) {
        Texture texture = new Texture(Gdx.files.internal
                ("Fonts/MyFont.png"), true);

        DistanceFieldFont font = new Font(Gdx.files.internal("Fonts/MyFont.fnt"),
                new TextureRegion(texture), size);
        return font;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        group.draw(batch,parentAlpha);
    }
}
