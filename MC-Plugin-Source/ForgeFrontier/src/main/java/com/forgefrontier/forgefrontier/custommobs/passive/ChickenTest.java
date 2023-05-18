package com.forgefrontier.forgefrontier.custommobs.passive;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.custommobs.CustomMobInterface;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Map;

public class ChickenTest extends CraftChicken implements CustomMobInterface {
    Chicken baseEntity;
    private static Field attributeField;

    public ChickenTest(CraftServer server, Chicken entity) {
        super(server, entity);
        this.baseEntity = this.getHandle();
        baseEntity.goalSelector.removeAllGoals();
        this.registerGoals();
        this.registerAttributes();

    }



    protected void registerAttributes() {
        try {
            registerGenericAttribute(this, Attribute.GENERIC_ATTACK_DAMAGE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void registerGoals() {
        baseEntity.goalSelector.addGoal(8, new LookAtPlayerGoal(baseEntity, Player.class, 8.0F));
        baseEntity.goalSelector.addGoal(8, new RandomLookAroundGoal(baseEntity));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        baseEntity.goalSelector.addGoal(2, new MeleeAttackGoal(baseEntity, 1.0D, false));
        baseEntity.targetSelector.addGoal(2, new NearestAttackableTargetGoal(baseEntity, Player.class, true));
    }

    @Override
    public void doTick() {

    }

    @Override
    public void onDeath() {

    }

    @Override
    public void onSpawn() {

    }

    @Override
    public ItemStack[] rollDrops() {
        return null;
    }

    public static CraftEntity spawnCustomEntity(Location loc) {
        ServerLevel worldServer = ((CraftWorld) loc.getWorld()).getHandle();
        ChickenTest t = new ChickenTest((CraftServer) Bukkit.getServer(),
                new Chicken(EntityType.CHICKEN, worldServer));
        worldServer.addFreshEntity(t.getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM);
        t.teleport(loc);
        return t;
    }


    static {
        try {
            // attributes: Map<Attribute, AttributeInstance>
            attributeField = AttributeMap.class.getDeclaredField("b");
            attributeField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void registerGenericAttribute(org.bukkit.entity.Entity entity, Attribute attribute) throws IllegalAccessException {
        AttributeMap attributeMapBase = ((CraftLivingEntity) entity).getHandle().getAttributes();
        Map<net.minecraft.world.entity.ai.attributes.Attribute, AttributeInstance> map = (Map<net.minecraft.world.entity.ai.attributes.Attribute, AttributeInstance>) attributeField.get(attributeMapBase);
        net.minecraft.world.entity.ai.attributes.Attribute attributeBase = CraftAttributeMap.toMinecraft(attribute);
        AttributeInstance attrInst = new AttributeInstance(attributeBase, AttributeInstance::getAttribute);
        map.put(attributeBase, attrInst);
    }



}
