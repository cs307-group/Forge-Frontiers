package net.minecraft.tags;

import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.gameevent.GameEvent;

public class GameEventTags {

    public static final TagKey<GameEvent> VIBRATIONS = create("vibrations");
    public static final TagKey<GameEvent> IGNORE_VIBRATIONS_SNEAKING = create("ignore_vibrations_sneaking");

    public GameEventTags() {}

    private static TagKey<GameEvent> create(String s) {
        return TagKey.create(IRegistry.GAME_EVENT_REGISTRY, new MinecraftKey(s));
    }
}
