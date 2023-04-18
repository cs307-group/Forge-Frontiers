package com.forgefrontier.forgefrontier.mobs.slimes.hostile.slimeboss;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.mobs.CustomEntityManager;
import com.forgefrontier.forgefrontier.mobs.slimes.hostile.HostileSlimeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class SlimeBossEntity extends HostileSlimeEntity {

    int UNTIL_JUMP_TIMER_MAX = 100;
    int CHARGE_TIMER_MAX = 20;
    int JUMP_TIMER_MAX = 30;

    int untilJumpTimer;
    int chargeTimer;
    int jumpTimer;

    boolean charged;

    public SlimeBossEntity(EntityType<? extends Slime> entityTypes, Level world) {
        super(entityTypes, world);

        untilJumpTimer = UNTIL_JUMP_TIMER_MAX;
        jumpTimer = JUMP_TIMER_MAX;
        chargeTimer = 0;
        charged = false;
    }

    @Override
    public void customTick() {
        super.customTick();

        if (hasNearestPlayer()) {
            if (charged) { // Executing Jump Slam
                if (jumpTimer >= JUMP_TIMER_MAX / 2) {
                    this.teleportTo(loc.getX(), loc.getY() + 1, loc.getZ());
                    jumpTimer--;
                } else if (jumpTimer >= 0) {
                    this.teleportTo(loc.getX(), loc.getY() - 1, loc.getZ());
                    jumpTimer--;
                } else { // Deal Slam Damage
                    charged = false;
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
                        if (nearbyPlayer.isOnGround()) {
                            attack(nearbyPlayer, 20);
                        }
                    }

                    jumpTimer = JUMP_TIMER_MAX;
                }
            } else if (untilJumpTimer == 0 && chargeTimer == 0) { // Signals to start jump
                charged = true;
                untilJumpTimer = UNTIL_JUMP_TIMER_MAX;
            } else if (untilJumpTimer == 0) { // charging state
                chargeTimer--;
                setDoBehavior(false);
                setNoAi(true);
            } else { // Default behavior
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
        ForgeFrontier.getInstance().getCustomEntityManager().spawnEntity("SlimeBoss", loc);
    }
}
