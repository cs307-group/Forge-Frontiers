package com.forgefrontier.forgefrontier.listeners;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatManager extends Manager implements Listener {

    public enum ChatFormat {
        NORMAL("&f%player%&8: &7%message%"),
        VIP("&a[VIP] &f%player%&8: &f%message%"),
        ELITE("&9[Elite] &f%player%&8: &f%message%"),
        LEGEND("&d[Legend] &f%player%&8: &f%message%");

        String name;
        ChatFormat(String name) {
            this.name = name;
        }
        public String value() {
            return this.name;
        }
    }

    Map<UUID, String> playerChatFormats;
    Map<UUID, String> playerNicknamesFormats;

    public ChatManager(ForgeFrontier plugin) {
        super(plugin);
        this.playerChatFormats = new HashMap<>();
        this.playerNicknamesFormats = new HashMap<>();
    }

    @Override
    public void init() {
        for(Player p: Bukkit.getOnlinePlayers()) {
            playerChatFormats.put(p.getUniqueId(), "&f%player%&8: &7%message%");
        }
    }

    @Override
    public void disable() {

    }

    public void putFormat(UUID uniqueId, ChatFormat chatFormat) {
        this.playerChatFormats.put(uniqueId, chatFormat.value());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        String format = playerChatFormats.get(e.getPlayer().getUniqueId());
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%player%", e.getPlayer().getName());//e.getPlayer().getName());
        format = format.replace("%message%", "%2$s");//e.getMessage());
        e.setFormat(format);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        playerChatFormats.put(e.getPlayer().getUniqueId(), ChatFormat.NORMAL.value());

        UUID uuid = e.getPlayer().getUniqueId();
        ForgeFrontier.getInstance().getDatabaseManager().getPlayerDB().getRanks(e.getPlayer().getUniqueId(), (purchasedRanks) -> {
            if (purchasedRanks.contains("Legend Rank")) {
                playerChatFormats.put(uuid, ChatFormat.LEGEND.value());
                return;
            }
            if (purchasedRanks.contains("Elite Rank")) {
                playerChatFormats.put(uuid, ChatFormat.ELITE.value());
                return;
            }
            if (purchasedRanks.contains("VIP Rank")) {
                playerChatFormats.put(uuid, ChatFormat.VIP.value());
                return;
            }
        });
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        this.playerChatFormats.remove(e.getPlayer().getUniqueId());
        this.playerNicknamesFormats.remove(e.getPlayer().getUniqueId());
    }

}
