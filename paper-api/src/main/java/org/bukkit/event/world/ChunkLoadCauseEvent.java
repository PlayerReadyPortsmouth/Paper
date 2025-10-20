package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a chunk is loaded and the initiating cause is known.
 */
public class ChunkLoadCauseEvent extends ChunkEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Plugin plugin;
    private final ChunkLoadType type;

    public ChunkLoadCauseEvent(@NotNull final Chunk chunk, @Nullable final Plugin plugin, @NotNull final ChunkLoadType type) {
        super(chunk);
        this.plugin = plugin;
        this.type = type;
    }

    @Nullable
    public Plugin getPlugin() {
        return this.plugin;
    }

    @NotNull
    public ChunkLoadType getType() {
        return this.type;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}
