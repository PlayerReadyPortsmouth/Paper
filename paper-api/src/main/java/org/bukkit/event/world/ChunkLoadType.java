package org.bukkit.event.world;

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents the reason a chunk was loaded.
 */
public enum ChunkLoadType {

    /**
     * The chunk was loaded due to a player ticket.
     */
    PLAYER_TICKET,

    /**
     * The chunk was loaded for a teleport action.
     */
    TELEPORT,

    /**
     * The chunk was loaded as part of a command execution.
     */
    COMMAND,

    /**
     * The chunk was loaded by a plugin via the Bukkit API.
     */
    PLUGIN_API,

    /**
     * The chunk was loaded because it was newly generated.
     */
    WORLD_GEN,

    /**
     * The cause of the chunk load could not be determined.
     */
    @ApiStatus.Experimental
    UNKNOWN
}
