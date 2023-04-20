package com.forgefrontier.forgefrontier.tutorial;

import com.forgefrontier.forgefrontier.utils.JSONSerializable;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import org.bukkit.entity.Player;

public class TaskStatus implements JSONSerializable {

    private Player p;
    private TutorialTask task;
    private boolean completed;
    private int currentStepIndex;

    public TaskStatus(Player p, TutorialTask task, int stepIndex) {
        this.p = p;
        this.task = task;
        this.currentStepIndex = stepIndex;
        this.completed = this.currentStepIndex == this.task.getStepAmt();
    }

    public Player getPlayer() {
        return p;
    }

    public boolean isCompleted() {
        return completed;
    }

    public TaskStep getStep() {
        return this.task.getStep(this.currentStepIndex);
    }

    public void moveToNextStep() {
        this.currentStepIndex += 1;
        this.completed = this.currentStepIndex == this.task.getStepAmt();
    }

    @Override
    public JSONWrapper toJSON() {
        JSONWrapper wrapper = new JSONWrapper();
        wrapper.setInt("step", this.currentStepIndex);
        return wrapper;
    }

    public int getCurrentStepIndex() {
        return this.currentStepIndex;
    }
}
