package io.papermc.paper.util;

import io.papermc.paper.configuration.GlobalConfiguration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.bukkit.event.world.ChunkLoadType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ChunkAttribution {

    private static final Cause DEFAULT_CAUSE = new Cause(null, ChunkLoadType.UNKNOWN);
    private static final Map<ServerLevel, Map<Long, Cause>> PENDING_CAUSES = new ConcurrentHashMap<>();

    private ChunkAttribution() {
    }

    public static void recordChunkLoadCause(@NotNull final ServerLevel level, final int chunkX, final int chunkZ, @NotNull final ChunkLoadType type) {
        final Plugin plugin = currentInitiator();
        final Cause cause = new Cause(plugin, Objects.requireNonNull(type, "type"));
        final Map<Long, Cause> map = PENDING_CAUSES.computeIfAbsent(level, ignored -> new ConcurrentHashMap<>());
        map.putIfAbsent(ChunkPos.asLong(chunkX, chunkZ), cause);
    }

    @NotNull
    public static Cause consumeCause(@NotNull final ServerLevel level, final int chunkX, final int chunkZ) {
        final Map<Long, Cause> map = PENDING_CAUSES.get(level);
        if (map == null) {
            return DEFAULT_CAUSE;
        }

        final Cause cause = map.remove(ChunkPos.asLong(chunkX, chunkZ));
        if (map.isEmpty()) {
            PENDING_CAUSES.remove(level, map);
        }
        return cause == null ? DEFAULT_CAUSE : cause;
    }

    @Nullable
    public static Plugin currentInitiator() {
        if (!isStackWalkerEnabled()) {
            return null;
        }
        return StackWalkerUtil.getFirstPluginCaller();
    }

    private static boolean isStackWalkerEnabled() {
        final GlobalConfiguration configuration = GlobalConfiguration.get();
        return configuration != null
            && configuration.chunkLoadingAdvanced != null
            && configuration.chunkLoadingAdvanced.detectPluginInitiatorForChunkLoads;
    }

    public record Cause(@Nullable Plugin plugin, @NotNull ChunkLoadType type) {
    }
}
