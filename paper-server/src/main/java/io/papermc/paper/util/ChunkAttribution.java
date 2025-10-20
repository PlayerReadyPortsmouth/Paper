package io.papermc.paper.util;

import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.bukkit.event.world.ChunkLoadType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ChunkAttribution {

    private static final StackWalker STACK_WALKER = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);
    private static final ConcurrentMap<ServerLevel, LevelAttribution> LEVEL_DATA = new ConcurrentHashMap<>();

    private ChunkAttribution() {
    }

    public static void registerRequest(@NotNull final ServerLevel level, final int chunkX, final int chunkZ, @NotNull final ChunkLoadType type) {
        Objects.requireNonNull(type, "type");
        if (level.getChunkSource().getChunkAtIfLoadedImmediately(chunkX, chunkZ) != null) {
            return;
        }
        final Plugin plugin = resolvePlugin(level);
        final Initiator initiator = new Initiator(plugin, type);
        LEVEL_DATA.computeIfAbsent(level, unused -> new LevelAttribution()).record(ChunkPos.asLong(chunkX, chunkZ), initiator);
    }

    public static @Nullable Initiator consume(@NotNull final ServerLevel level, @NotNull final ChunkPos chunkPos) {
        final LevelAttribution attribution = LEVEL_DATA.get(level);
        if (attribution == null) {
            return null;
        }
        final Initiator initiator = attribution.consume(chunkPos.toLong());
        if (attribution.isEmpty()) {
            LEVEL_DATA.remove(level, attribution);
        }
        return initiator;
    }

    private static @Nullable Plugin resolvePlugin(final ServerLevel level) {
        if (!level.paperConfig().chunks.enableChunkLoadCauseStackWalk) {
            return null;
        }
        final Optional<Plugin> plugin = STACK_WALKER.walk(stream -> stream
            .map(StackFrame::getDeclaringClass)
            .map(Class::getClassLoader)
            .map(loader -> loader instanceof ConfiguredPluginClassLoader configured ? configured.getPlugin() : null)
            .filter(Objects::nonNull)
            .findFirst());
        return plugin.orElse(null);
    }

    public record Initiator(@Nullable Plugin plugin, @NotNull ChunkLoadType type) {
        public Initiator {
            Objects.requireNonNull(type, "type");
        }
    }

    private static final class LevelAttribution {
        private final Long2ObjectOpenHashMap<Initiator> causes = new Long2ObjectOpenHashMap<>();

        synchronized void record(final long key, final Initiator initiator) {
            this.causes.put(key, initiator);
        }

        synchronized Initiator consume(final long key) {
            return this.causes.remove(key);
        }

        synchronized boolean isEmpty() {
            return this.causes.isEmpty();
        }
    }
}
