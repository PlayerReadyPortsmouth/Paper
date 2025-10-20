package org.bukkit.event.world;

/**
 * Represents the initiator that caused a chunk to load.
 */
public enum ChunkLoadType {

    /**
     * The chunk was loaded because a player ticket requested it (e.g. proximity).
     */
    PLAYER_TICKET,

    /**
     * The chunk was loaded due to an entity or player teleportation.
     */
    TELEPORT,

    /**
     * The chunk was loaded as a result of a command execution.
     */
    COMMAND,

    /**
     * The chunk was explicitly loaded by a plugin via the API.
     */
    PLUGIN_API,

    /**
     * The chunk was generated or loaded by the world generator.
     */
    WORLD_GEN,

    /**
     * The chunk was loaded for an unidentified reason.
     */
    UNKNOWN
}
