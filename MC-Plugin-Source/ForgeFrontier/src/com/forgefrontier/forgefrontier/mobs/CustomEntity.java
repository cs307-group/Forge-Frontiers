package com.forgefrontier.forgefrontier.mobs;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Interface to handle all custom actions of custom mobs in Forge Frontier
 */
public interface CustomEntity extends Entity {

    /** defines the behaviour of the entity */
    public void executeBehavior();

    /** sets the health and max health of the entity */
    public void setHealth(double health);

    /** spawns the custom entity in the world at the specified location */
    public Entity spawnCustomEntity(Location loc);

    @NotNull
    @Override
    Location getLocation();

    @Nullable
    @Override
    Location getLocation(@Nullable Location loc);

    @Override
    void setVelocity(@NotNull Vector velocity);

    @NotNull
    @Override
    Vector getVelocity();

    @Override
    double getHeight();

    @Override
    double getWidth();

    @NotNull
    @Override
    BoundingBox getBoundingBox();

    @Override
    boolean isOnGround();

    @Override
    boolean isInWater();

    @NotNull
    @Override
    World getWorld();

    @Override
    void setRotation(float yaw, float pitch);

    @Override
    boolean teleport(@NotNull Location location);

    @Override
    boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause cause);

    @Override
    boolean teleport(@NotNull Entity destination);

    @Override
    boolean teleport(@NotNull Entity destination, @NotNull PlayerTeleportEvent.TeleportCause cause);

    @NotNull
    @Override
    List<Entity> getNearbyEntities(double x, double y, double z);

    @Override
    int getEntityId();

    @Override
    int getFireTicks();

    @Override
    int getMaxFireTicks();

    @Override
    void setFireTicks(int ticks);

    @Override
    void setVisualFire(boolean fire);

    @Override
    boolean isVisualFire();

    @Override
    int getFreezeTicks();

    @Override
    int getMaxFreezeTicks();

    @Override
    void setFreezeTicks(int ticks);

    @Override
    boolean isFrozen();

    @Override
    void remove();

    @Override
    boolean isDead();

    @Override
    boolean isValid();

    @NotNull
    @Override
    Server getServer();

    @Override
    boolean isPersistent();

    @Override
    void setPersistent(boolean persistent);

    @Nullable
    @Override
    Entity getPassenger();

    @Override
    boolean setPassenger(@NotNull Entity passenger);

    @NotNull
    @Override
    List<Entity> getPassengers();

    @Override
    boolean addPassenger(@NotNull Entity passenger);

    @Override
    boolean removePassenger(@NotNull Entity passenger);

    @Override
    boolean isEmpty();

    @Override
    boolean eject();

    @Override
    float getFallDistance();

    @Override
    void setFallDistance(float distance);

    @Override
    void setLastDamageCause(@Nullable EntityDamageEvent event);

    @Nullable
    @Override
    EntityDamageEvent getLastDamageCause();

    @NotNull
    @Override
    UUID getUniqueId();

    @Override
    int getTicksLived();

    @Override
    void setTicksLived(int value);

    @Override
    void playEffect(@NotNull EntityEffect type);

    @NotNull
    @Override
    EntityType getType();

    @Override
    boolean isInsideVehicle();

    @Override
    boolean leaveVehicle();

    @Nullable
    @Override
    Entity getVehicle();

    @Override
    void setCustomNameVisible(boolean flag);

    @Override
    boolean isCustomNameVisible();

    @Override
    void setGlowing(boolean flag);

    @Override
    boolean isGlowing();

    @Override
    void setInvulnerable(boolean flag);

    @Override
    boolean isInvulnerable();

    @Override
    boolean isSilent();

    @Override
    void setSilent(boolean flag);

    @Override
    boolean hasGravity();

    @Override
    void setGravity(boolean gravity);

    @Override
    int getPortalCooldown();

    @Override
    void setPortalCooldown(int cooldown);

    @NotNull
    @Override
    Set<String> getScoreboardTags();

    @Override
    boolean addScoreboardTag(@NotNull String tag);

    @Override
    boolean removeScoreboardTag(@NotNull String tag);

    @NotNull
    @Override
    PistonMoveReaction getPistonMoveReaction();

    @NotNull
    @Override
    BlockFace getFacing();

    @NotNull
    @Override
    Pose getPose();

    @NotNull
    @Override
    SpawnCategory getSpawnCategory();

    @NotNull
    @Override
    Spigot spigot();

    @Nullable
    @Override
    String getCustomName();

    @Override
    void setCustomName(@Nullable String name);

    @Override
    void sendMessage(@NotNull String message);

    @Override
    void sendMessage(@NotNull String... messages);

    @Override
    void sendMessage(@Nullable UUID sender, @NotNull String message);

    @Override
    void sendMessage(@Nullable UUID sender, @NotNull String... messages);

    @NotNull
    @Override
    String getName();

    @Override
    void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue);

    @NotNull
    @Override
    List<MetadataValue> getMetadata(@NotNull String metadataKey);

    @Override
    boolean hasMetadata(@NotNull String metadataKey);

    @Override
    void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin);

    @Override
    boolean isPermissionSet(@NotNull String name);

    @Override
    boolean isPermissionSet(@NotNull Permission perm);

    @Override
    boolean hasPermission(@NotNull String name);

    @Override
    boolean hasPermission(@NotNull Permission perm);

    @NotNull
    @Override
    PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value);

    @NotNull
    @Override
    PermissionAttachment addAttachment(@NotNull Plugin plugin);

    @Nullable
    @Override
    PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks);

    @Nullable
    @Override
    PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks);

    @Override
    void removeAttachment(@NotNull PermissionAttachment attachment);

    @Override
    void recalculatePermissions();

    @NotNull
    @Override
    Set<PermissionAttachmentInfo> getEffectivePermissions();

    @Override
    boolean isOp();

    @Override
    void setOp(boolean value);

    @NotNull
    @Override
    PersistentDataContainer getPersistentDataContainer();
}
