package com.forgefrontier.forgefrontier.tutorial;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.events.CustomCraftEvent;
import com.forgefrontier.forgefrontier.events.PurchaseEvent;
import com.forgefrontier.forgefrontier.utils.JSONWrapper;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class TutorialManager extends Manager implements Listener {

    private Map<String, TutorialTask> taskMap;

    private Map<UUID, Map<String, TaskStatus>> statuses;

    private Map<TaskStepType, List<TaskStatus>> nextStepsMap;

    public TutorialManager(ForgeFrontier plugin) {
        super(plugin);
        this.taskMap = new HashMap<>();
        this.statuses = new HashMap<>();
        this.nextStepsMap = new HashMap<>();
        for(TaskStepType type: TaskStepType.values()) {
            nextStepsMap.put(type, new ArrayList<>());
        }
    }

    @Override
    public void init() {

        int taskIndex = 0;
        ConfigurationSection section;
        while((section = this.plugin.getConfig("tutorial").getConfigurationSection("tasks." + taskIndex)) != null) {
            this.taskMap.put(section.getString("id"), new TutorialTask(section));
            taskIndex += 1;
        }
        for(Player p: Bukkit.getOnlinePlayers()) {
            this.addPlayer(p);
        }
    }

    @Override
    public void disable() {
        for(Player p: Bukkit.getOnlinePlayers()) {
            this.removePlayer(p);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void craftEvent(CraftItemEvent e) {
        if(e.isCancelled())
            return;
        String resultMaterialName = e.getRecipe().getResult().getType().toString();

        List<TaskStatus> statusList = nextStepsMap.get(TaskStepType.CRAFT);
        for(int i = 0; i < statusList.size(); i++) {
            TaskStatus status = statusList.get(i);
            if(status.getPlayer() != e.getWhoClicked())
                continue;
            ConfigurationSection data = status.getStep().getData();
            if(resultMaterialName.equals(data.get("material"))) {
                this.completeTaskStep(status);
                i -= 1;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void placeEvent(BlockPlaceEvent e) {
        if(e.isCancelled())
            return;
        String blockMaterialName = e.getBlock().getType().toString();

        List<TaskStatus> statusList = nextStepsMap.get(TaskStepType.PLACE);
        for(int i = 0; i < statusList.size(); i++) {
            TaskStatus status = statusList.get(i);
            if(status.getPlayer() != e.getPlayer())
                continue;
            ConfigurationSection data = status.getStep().getData();
            if(blockMaterialName.equals(data.get("block_material"))) {
                this.completeTaskStep(status);
                i -= 1;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void customCraft(CustomCraftEvent e) {
        if(e.getResult() == null)
            return;
        String itemType = e.getResult().getCode();

        List<TaskStatus> statusList = nextStepsMap.get(TaskStepType.CUSTOM_CRAFT);
        for(int i = 0; i < statusList.size(); i++) {
            TaskStatus status = statusList.get(i);
            if(status.getPlayer() != e.getPlayer())
                continue;
            ConfigurationSection data = status.getStep().getData();
            if(itemType.equals(data.get("item_type"))) {
                this.completeTaskStep(status);
                i -= 1;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void purchaseEvent(PurchaseEvent e) {

        List<TaskStatus> statusList = nextStepsMap.get(TaskStepType.PURCHASE);
        for(int i = 0; i < statusList.size(); i++) {
            TaskStatus status = statusList.get(i);
            if(status.getPlayer() != e.getPlayer())
                continue;
            ConfigurationSection data = status.getStep().getData();
            if(data.contains("purchase_type") && !data.getString("purchase_type").equals(e.getType().toString()))
                continue;
            if(data.contains("material") && !data.getString("material").equals(e.getItem().getType().toString()))
                continue;
            if(data.contains("item_type") && (e.getCustomItemInstance() == null || !data.getString("item_type").equals(e.getCustomItemInstance().getBaseItem().getCode())))
                continue;
            if(data.contains("item_data")) {
                Object obj = e.getCustomItemInstance().getData().get(data.getString("item_data.key"));
                if(!obj.equals(data.get("item_data.value"))) {
                    continue;
                }
            }
            this.completeTaskStep(status);
            i -= 1;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        this.addPlayer(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        this.removePlayer(e.getPlayer());
    }

    public void addPlayer(Player p) {
        Map<String, TaskStatus> statuses = new HashMap<>();
        this.statuses.put(p.getUniqueId(), statuses);

        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().importTutorialStatus(p.getUniqueId(), (state) -> {
            if(state != null) {
                for(String key: state.getStringKeys()) {
                    JSONWrapper wrapper = state.getJson(key);
                    TaskStatus status = new TaskStatus(p, this.taskMap.get(key), wrapper.getInt("step"));
                    statuses.put(key, status);
                    this.nextStepsMap.get(status.getStep().getType()).add(status);
                }
            }
            for(String key: taskMap.keySet()) {
                if(statuses.containsKey(key))
                    continue;
                TutorialTask task = taskMap.get(key);
                TaskStatus status = new TaskStatus(p, task, 0);
                statuses.put(key, status);
                this.nextStepsMap.get(status.getStep().getType()).add(status);
            };
        });

    }

    public void removePlayer(Player p) {
        Map<String, TaskStatus> statuses = this.statuses.get(p.getUniqueId());
        for(TaskStatus status: statuses.values()) {
            if(!status.isCompleted())
                this.nextStepsMap.get(status.getStep().getType()).remove(status);
        }
        this.statuses.remove(p.getUniqueId());

        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().updateTutorialStatus(p.getUniqueId(), statuses, (success) -> {});

    }

    private void completeTaskStep(TaskStatus status) {

        this.nextStepsMap.get(status.getStep().getType()).remove(status);

        TaskStep step = status.getStep();
        Player p = status.getPlayer();
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', step.getCompletionMessage()));
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        status.moveToNextStep();

        if(!status.isCompleted()) {
            TaskStep newStep = status.getStep();
            this.nextStepsMap.get(newStep.getType()).add(status);

            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eNext Step: " + newStep.getQuickDescription()));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', newStep.getFullDescription()));
        }

        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().updateTutorialStatus(p.getUniqueId(), this.statuses.get(p.getUniqueId()), (success) -> {});

    }

    public Map<String, TutorialTask> getTaskMap() {
        return taskMap;
    }

    public Map<UUID, Map<String, TaskStatus>> getStatuses() {
        return statuses;
    }

    public Map<String, TaskStatus> getStatuses(UUID uniqueId) {
        return statuses.get(uniqueId);
    }

    public Map<TaskStepType, List<TaskStatus>> getNextStepsMap() {
        return nextStepsMap;
    }
}
