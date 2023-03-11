package com.forgefrontier.forgefrontier.bazaarshop;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.gui.BaseInventoryHolder;
import org.bukkit.ChatColor;

public class BazaarGUI extends BaseInventoryHolder {
    private BazaarManager bazaarMgr;
    public BazaarGUI() {
        super(54, "" + ChatColor.GOLD + ChatColor.BOLD + "Bazaar");
        this.fillPanes();
        this.bazaarMgr = ForgeFrontier.getInstance().getBazaarManager();
    }


    public void updateGUI() {


    }


}
