package com.sengame.diablohell.uitools;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class SnapScrollPaneX extends ScrollPane {
    private final Table table;
    private boolean wasPanning;
    public Actor focusedActor;
    public boolean hasSnapRunnables;
    public Array<RunnableAction> runWhenActorNotSnappedOn, runWhenActorSnappedOn;
    public SnapScrollPaneX(Table table) {
        super(table);
        this.table = table;
        focusedActor = table.getChildren().get(0);
        runWhenActorNotSnappedOn = new Array<>();
        runWhenActorSnappedOn = new Array<>();
        setup();
    }
    public SnapScrollPaneX(Table table, Skin skin) {
        super(table, skin);
        this.table = table;
        focusedActor = table.getChildren().get(0);
        runWhenActorNotSnappedOn = new Array<>();
        runWhenActorSnappedOn = new Array<>();
        setup();
    }
    private void setup() {
        setFlingTime(0.1f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (wasPanning && !isPanning() && !isFlinging()) {
            scrollToNearestItem();
        }
        wasPanning = isPanning() || isFlinging();
    }
    private void scrollToNearestItem() {
        int numItems = table.getChildren().size;
        if (numItems <= 1) {
            return;
        }

        float currentScroll = getScrollPercentX();

        float itemSnapInterval = 1.0f / (float)(numItems - 1);

        int nearestItemIndex = MathUtils.round(currentScroll / itemSnapInterval);

        nearestItemIndex = MathUtils.clamp(nearestItemIndex, 0, numItems - 1);

        float snapTarget = nearestItemIndex * itemSnapInterval;

        focusedActor = table.getChildren().get((int) (snapTarget*(table.getChildren().size-1)));
        if (hasSnapRunnables){
            applyRunnables();
        }

        setScrollPercentX(snapTarget);
    }
    public void applyRunnables(){
        hasSnapRunnables = true;
        for (Actor actor : table.getChildren()){
            if (actor!=focusedActor){
                runWhenActorNotSnappedOn.get(table.getChildren().indexOf(actor,false)).run();
            }
        }
        runWhenActorSnappedOn.get(table.getChildren().indexOf(focusedActor,false)).run();
    }
}

