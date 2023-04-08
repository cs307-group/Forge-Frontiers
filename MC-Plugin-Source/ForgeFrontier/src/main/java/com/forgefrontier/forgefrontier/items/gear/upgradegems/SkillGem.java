package com.forgefrontier.forgefrontier.items.gear.upgradegems;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.items.CustomItem;
import com.forgefrontier.forgefrontier.items.CustomItemInstance;
import com.forgefrontier.forgefrontier.items.CustomItemManager;
import com.forgefrontier.forgefrontier.items.ItemStackBuilder;
import com.forgefrontier.forgefrontier.items.gear.GearItemInstance;
import com.forgefrontier.forgefrontier.items.gear.skills.Skill;
import com.forgefrontier.forgefrontier.player.FFPlayer;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SkillGem extends CustomItem {

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
            return builder.build();
        });
    }

    @Override
    public void onApply(InventoryClickEvent e, CustomItemInstance itemInstance, ItemStack appliedItem) {
        // TODO: Be able to apply gems
        System.out.println("Skill Gem!");
        CustomItemInstance inst = CustomItemManager.asCustomItemInstance(appliedItem);
        System.out.println(appliedItem + " " + inst);
        if(inst == null)
            return;
        if(!(inst instanceof GearItemInstance gearInstance)) {
            return;
        }
        System.out.println("Work magic!");
        SkillGemInstance skillGemInstnace = (SkillGemInstance) itemInstance;
        gearInstance.setSkill(skillGemInstnace.getSkill());
        e.setCancelled(true);
        e.getClickedInventory().setItem(e.getSlot(), gearInstance.asItemStack());
        e.getCursor().setAmount(0);
        //e.setCursor(new ItemStack(Material.AIR));
        //e.setCancelled(true);
    }

}
