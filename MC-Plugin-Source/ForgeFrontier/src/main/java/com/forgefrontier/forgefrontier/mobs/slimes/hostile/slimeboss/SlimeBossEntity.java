package com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.CustomEntityManager;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.HostileSlimeEntity;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo.SlimeBossSmallEntity;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo.SlimeBossSwiftEntity;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss.phasetwo.SlimeBossTimerEntity;
import com.forgefrontier.forgefrontier.particles.FFParticle;
import com.forgefrontier.forgefrontier.particles.gameparticles.MobParticles;
import com.forgefrontier.forgefrontier.particles.particlespawner.SimpleRepeatParticleSpawner;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class SlimeBossEntity extends HostileSlimeEntity {


    public static String CODE = "SlimeBoss";
    int UNTIL_JUMP_TIMER_MAX = 100;
    int CHARGE_TIMER_MAX = 20;
    int JUMP_TIMER_MAX = 30;
    boolean existed;

    int untilJumpTimer;
    int chargeTimer;
    int jumpTimer;

    boolean charged;
    boolean jumpInit = false;
    boolean jumpComplete = false;

    public SlimeBossEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);

        untilJumpTimer = UNTIL_JUMP_TIMER_MAX;
        jumpTimer = JUMP_TIMER_MAX;
        chargeTimer = 0;
        charged = false;
        existed = true;
    }

    @Override
    public void customTick() {
        super.customTick();

        if (hasNearestPlayer()) {
            if (charged) { // Executing Jump Slam

//                if (jumpTimer >= JUMP_TIMER_MAX / 2) {
//                    this.teleportTo(loc.getX(), loc.getY() + 1, loc.getZ());
//                    jumpTimer--;
//                } else if (jumpTimer >= 0) {
//                    this.teleportTo(loc.getX(), loc.getY() - 1, loc.getZ());
//                    jumpTimer--;
//                }
                // Using velocity based jump instead of teleportation (roof bug)
                if (!jumpInit) {
                    this.getBukkitEntity().setVelocity(new Vector(0,1.5,0));
                    jumpInit = true;
                    jumpComplete = false;
                } else if (!jumpComplete &&
                        this.getBukkitEntity().getLocation().getBlock()
                                .getRelative(BlockFace.DOWN).getType() != Material.AIR
                        && Math.abs(this.getBukkitEntity().getVelocity().getY()) <= 0.1) {
                    jumpComplete = true;
                    new SimpleRepeatParticleSpawner(() -> MobParticles.SLIME_JUMP_LAND.playParticleMob(this),
                            3, 20).run();

                }
                else if (jumpComplete){ // Deal Slam Damage

                    charged = false;
                    jumpComplete = false;
                    jumpInit = false;
                    setDoBehavior(true);
                    setNoAi(false);// get the location of the entity
                    Location location = this.getBukkitEntity().getLocation();

                    // get all entities within a 10-block radius of the entity
                    Collection<Entity> nearbyEntities = world.getNearbyEntities(location, 10, 10, 10);

                    // filter the list to include only players
                    List<Player> nearbyPlayers = nearbyEntities.stream()
                            .filter(e -> e instanceof Player)
                            .map(e -> (Player) e)
                            .collect(Collectors.toList());


                    for (Player nearbyPlayer : nearbyPlayers) {
                        if (nearbyPlayer.getLocation().getBlock()
                                .getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                            attack(nearbyPlayer, 20);
                            MobParticles.HIT_FLASH.playAtPlayer(nearbyPlayer);
                        }
                    }

                    jumpTimer = JUMP_TIMER_MAX;
                }
            } else if (untilJumpTimer == 0 && chargeTimer == 0) { // Signals to start jump
                charged = true;
                setNoAi(false);     // TEST
                untilJumpTimer = UNTIL_JUMP_TIMER_MAX;
            } else if (untilJumpTimer == 0 &&
                    this.getBukkitEntity().getLocation()
                            .getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                // CHARGE WAIT
                new SimpleRepeatParticleSpawner(() -> MobParticles.SLIME_JUMP_CHARGE.playParticleMob(this),
                        2, 10).run();
                // CHARGE WAIT
                new SimpleRepeatParticleSpawner(() -> MobParticles.SLIME_JUMP_CHARGE2.playParticleMob(this),
                        2, 10).run();


                chargeTimer--;
                setDoBehavior(false);
                setNoAi(true);
            } else if (untilJumpTimer > 0) { // Default behavior
                untilJumpTimer--;
                if (untilJumpTimer == 0) { // signals to begin charging
                    chargeTimer = CHARGE_TIMER_MAX;
                }
            }


            /*
            System.out.println("ChargeTimer: " + chargeTimer + " | UntilJumpTimer: " + untilJumpTimer +
                    " | Charged: " + charged + " | JumpTimer: " + jumpTimer);
            */
        }
    }

    public void onDeath() {
        if (existed) {
            CraftEntity craftEntityTimer = ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity("SlimeBossTimer", loc);
            CraftEntity craftEntityA = ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity("SlimeBossSmall", loc);
            CraftEntity craftEntityB = ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity("SlimeBossSmall", loc);
            CraftEntity craftEntitySwift = ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity("SlimeBossSwift", loc);
            if (craftEntityA.getHandle() instanceof SlimeBossSmallEntity smallEntityA &&
                    craftEntityB.getHandle() instanceof SlimeBossSmallEntity smallEntityB &&
                    craftEntitySwift.getHandle() instanceof SlimeBossSwiftEntity swiftEntity) {
                smallEntityB.offsetUntilJump();
                if (craftEntityTimer.getHandle() instanceof SlimeBossTimerEntity timerEntity) {
                    timerEntity.setBounceA(smallEntityA);
                    timerEntity.setBounceB(smallEntityB);
                    timerEntity.setSwift(swiftEntity);
                }
            }
            existed = false;
        }
    }

    public void offsetUntilJump() {
        this.untilJumpTimer += UNTIL_JUMP_TIMER_MAX / 2;
    }
}
