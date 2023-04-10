package com.forgefrontier.forgefrontier.items.gear.upgradegems;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.*;
import com.forgefrontier.forgefrontier.items.gear.GearItemInstance;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SkillGem extends UniqueCustomItem {

    /**
     * CustomItem constructor
     * <p>
     * Defines the attributes of a base custom item
     * and registers the appropriate accumulators to set those attributes.
     *
     * @param code A string that is used to identify the type of CustomItem an item is
     */
    public SkillGem() {
        super("SkillGem");

        this.registerInstanceAccumulator((__, itemStack) -> {
            SkillGemInstance inst =  new SkillGemInstance(itemStack);
            if(itemStack == null)
                inst.skill = ForgeFrontier.getInstance().getGearItemManager().getRandomSkill();
            else
                inst.skill = ForgeFrontier.getInstance().getGearItemManager().getSkill((String) inst.getData().get("skill-id"));
            return inst;
        });

        this.registerItemStackAccumulator((inst, __) -> {
            SkillGemInstance gemInst = (SkillGemInstance) inst;
            ItemStackBuilder builder = new ItemStackBuilder(Material.AMETHYST_SHARD);
            builder.setDisplayName("&d&oSkill Upgrade Gem");
            if(gemInst.skill == null)
                return builder.addLoreLine("&bSkill&7: &fUnknown").build();
            builder.addLoreLine("&bType&7: &f&l" + gemInst.skill.getType().getFriendlyName());
            builder.addLoreLine("&bSkill&7: " + gemInst.skill.getName());
            builder.addLoreLine(gemInst.skill.getDescrption());
            builder.addLoreLine("");

            gemInst.getData().put("skill-id", gemInst.skill.getId());

            return builder.build();
        });
    }

    @Override
    public void onApply(InventoryClickEvent e, CustomItemInstance itemInstance, ItemStack appliedItem) {
        CustomItemInstance inst = CustomItemManager.asCustomItemInstance(appliedItem);

        if(inst == null)
            return;
        if(!(inst instanceof GearItemInstance gearInstance)) {
            return;
        }

        SkillGemInstance skillGemInstance = (SkillGemInstance) itemInstance;
        ForgeFrontier.getInstance().getLogger().info(skillGemInstance.toString());
        gearInstance.setSkill(skillGemInstance.getSkill());

        e.setCancelled(true);
        e.setCurrentItem(gearInstance.asItemStack());
        e.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
        ((Player) e.getWhoClicked()).updateInventory();
    }

}
