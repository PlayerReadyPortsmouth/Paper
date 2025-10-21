package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a chunk is loaded to provide information about what caused the load.
 */
public class ChunkLoadCauseEvent extends ChunkEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Plugin plugin;
    private final ChunkLoadType type;

    public ChunkLoadCauseEvent(@NotNull final Chunk chunk, @Nullable final Plugin plugin, @NotNull final ChunkLoadType type) {
        super(chunk);
        this.plugin = plugin;
        this.type = type;
    }

    /**
     * Gets the plugin responsible for the chunk load, if one could be determined.
     *
     * @return the initiating plugin, or {@code null} if none could be detected
     */
    @Nullable
    public Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Gets the cause of the chunk load.
     *
     * @return the chunk load type
     */
    @NotNull
    public ChunkLoadType getType() {
        return this.type;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
