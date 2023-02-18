package com.forgefrontier.forgefrontier.shop;


import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ShopHolder extends BaseInventoryHolder {

    ShopHolder() {
        super(27);
        this.fillPanes();
    }

}
