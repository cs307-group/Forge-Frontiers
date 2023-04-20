package com.forgefrontier.forgefrontier.tutorial;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class TutorialInventoryHolder extends BaseInventoryHolder {


    public TutorialInventoryHolder(Player p) {
        super(54, "Tutorial");
        this.fillPanes();
        Map<String, TutorialTask> taskMap = ForgeFrontier.getInstance().getTutorialManager().getTaskMap();
        Map<String, TaskStatus> statuses = ForgeFrontier.getInstance().getTutorialManager().getStatuses(p.getUniqueId());
        for(int i = 0; i < 7 * 4; i++) {
            int x = i % 7;
            int y = i / 7;
            int slot = x + 1 + (y + 1) * 9;
            this.setItem(slot, new ItemStack(Material.AIR));

        }

        int i = 0;
        for(String taskId: taskMap.keySet()) {
            TutorialTask task = taskMap.get(taskId);
            TaskStatus status = statuses.get(taskId);
            ItemStackBuilder builder = new ItemStackBuilder(task.getItemMaterial())
                .setDisplayName(task.getName())
                .addLoreLine(task.getDescription());
            int stepNumber = 0;
            for(TaskStep step: task.getSteps()) {
                if(stepNumber == status.getCurrentStepIndex()) {
                    builder.addLoreLine("&f&l>> &7Step &6" + (stepNumber + 1) + "&e: " + step.getQuickDescription() + " &f&l<<");
                } else {
                    builder.addLoreLine("&7Step &e" + (stepNumber + 1) + "&7: &8" + step.getQuickDescription());
                }
                stepNumber += 1;
            }
            if(status.isCompleted()) {
                builder.addLoreLine("&a&lTask Completed");
            }
            int x = i % 7;
            int y = i / 7;
            int slot = x + 1 + (y + 1) * 9;
            this.setItem(slot, builder.build());
            this.addHandler(slot, (e) -> {
                e.getWhoClicked().closeInventory();
                msg(e.getWhoClicked(), "&eSelected Task: " + task.getName());
                msg(e.getWhoClicked(), task.getDescription());
                if(status.isCompleted()) {
                    msg(e.getWhoClicked(), "&a&lThis task has been completed.");
                    return;
                }
                if(status.getCurrentStepIndex() > 0) {
                    TaskStep lastStep = task.getStep(status.getCurrentStepIndex() - 1);
                    msg(e.getWhoClicked(), "");
                    msg(e.getWhoClicked(), "&ePrevious Step: &eStep " + (status.getCurrentStepIndex()) + " - " + lastStep.getQuickDescription());
                    msg(e.getWhoClicked(), "&f" + lastStep.getFullDescription());
                }
                msg(e.getWhoClicked(), "");
                TaskStep currentStep = task.getStep(status.getCurrentStepIndex());
                msg(e.getWhoClicked(), "&f&l>> &6Current Step: &eStep " + (status.getCurrentStepIndex() + 1) + " - " + currentStep.getQuickDescription());
                msg(e.getWhoClicked(), "&f" + currentStep.getFullDescription());
            });
            i += 1;
        }
    }

    private void msg(HumanEntity p, String msg) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

}
