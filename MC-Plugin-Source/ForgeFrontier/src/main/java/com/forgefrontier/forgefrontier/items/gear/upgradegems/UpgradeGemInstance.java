package com.forgefrontier.forgefrontier.items.gear.upgradegems;

import com.forgefrontier.forgefrontier.items.UniqueCustomItem.UniqueCustomItemInstance;
import com.forgefrontier.forgefrontier.items.gear.quality.Quality;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * A class representing the UpgradeGems, which can be applied to special gear in their inventory. Once applied to an
 * item the gem can not be removed
 */
public class UpgradeGemInstance extends UniqueCustomItemInstance {

    /** holds the data related to the values of the gem */
    GemValues gemValues;

    HashMap<String, String> gemData = new HashMap<String, String>();

    /**
     * Instance constructor
     *
     * @param itemStack the itemstack representing the custom item
     */
    public UpgradeGemInstance(ItemStack itemStack) {
        super(itemStack);
        this.gemValues = new GemValues();
    }

    /**
     * Constructs instance based off of string data
     *
     * @param data the string representation of the object
     */
    public UpgradeGemInstance(ItemStack itemStack, String data) {
        super(itemStack);
        this.gemValues = new GemValues(data);
    }

    /**
     * Only callable after the attributes of gemValues has been set
     */
    public void setGemData() {
        gemData.put("gem-values", gemValues.toString());
    }

    /**
     * Only callable after the value of gemData has been set
     */
    public void setAttributes() {
        String gemValuesData = gemData.get("gem-values");
        this.gemValues = new GemValues(gemValuesData);
    }

    /**
     * @return the quality of the instance
     */
    public Quality getQuality() {
        return gemValues.quality;
    }

    @Override
    public String toString() {
        return "{" + gemValues.quality.toString() + ":" + gemValues.stat.toString() + ":" + gemValues.gemType.toString() + "}";
    }
}
