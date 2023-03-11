package com.forgefrontier.forgefrontier.generators.materials;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CoinMaterial extends CustomMaterial {

    public CoinMaterial() {
        super(
            new ItemStackBuilder(Material.SUNFLOWER)
                .setDisplayName("&eCoins")
                .addLoreLine("&7Coins shown inside the /bal command.")
                .setAmount(1)
                .build(),
            "Coins"
        );
    }

    @Override
    public int collect(Player p, int amt) {
        ForgeFrontier.getEconomy().depositPlayer(p, amt);
        return 0;
    }

    @Override
    public boolean hasBalance(Player p, int amt) {
        return ForgeFrontier.getEconomy().has(p, amt);
    }

    @Override
    public void take(Player p, int amt) {
        ForgeFrontier.getEconomy().withdrawPlayer(p, amt);
    }

}
