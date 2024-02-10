package com.enderio.core.common.util;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.UsernameCache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

public class UserIdent {

    private static final @NotNull String NONE_MARKER = "none";
    private final @Nullable UUID uuid;
    private final @NotNull UUID uuid_offline;
    private final @NotNull String playerName;

    public @NotNull String getPlayerName() {
        if (uuid != null) {
            String lastKnownName = UsernameCache.getLastKnownUsername(uuid);
            if (lastKnownName != null) {
                return lastKnownName;
            }
        }
        return playerName;
    }

    public @NotNull UUID getUUID() {
        return uuid != null ? uuid : uuid_offline;
    }

    public @NotNull String getUUIDString() {
        return uuid != null ? uuid + "" : NONE_MARKER;
    }

    public @NotNull GameProfile getAsGameProfile() {
        return new GameProfile(getUUID(), getPlayerName());
    }

    /**
     * Create a UserIdent from a UUID object and a name. Use this when reading stored data, it will check for username
     * changes, implement them and write a log
     * message.
     */
    public static @NotNull UserIdent create(@Nullable UUID uuid, @Nullable String playerName) {
        if (uuid != null) {
            if (NOBODY.equals(uuid)) {
                return NOBODY;
            }
            if (playerName != null) {
                String lastKnownName = UsernameCache.getLastKnownUsername(uuid);
                if (lastKnownName != null && !lastKnownName.equals(playerName)) {
                    Log.warn("The user with the UUID " + uuid + " changed name from '" + playerName + "' to '" +
                            lastKnownName + "'");
                    return new UserIdent(uuid, lastKnownName);
                }
            }
            return new UserIdent(uuid, playerName);
        } else if (playerName != null) {
            return new UserIdent(null, playerName);
        } else {
            return NOBODY;
        }
    }

    /**
     * Create a UserIdent from a UUID string and a name. Use this when reading stored data, it will check for username
     * changes, implement them and write a log
     * message.
     */
    public static @NotNull UserIdent create(@NotNull String suuid, @Nullable String playerName) {
        if (NONE_MARKER.equals(suuid)) {
            return new UserIdent(null, playerName);
        }
        try {
            UUID uuid = UUID.fromString(suuid);
            if (NOBODY.equals(uuid)) {
                return NOBODY;
            }
            return create(uuid, playerName);
        } catch (IllegalArgumentException e) {
            return NOBODY;
        }
    }

    /**
     * Create a UserIdent from a legacy string. The string can either be a UUID or a player name. Use this when reading
     * legacy data or user configured values.
     */
    public static @NotNull UserIdent create(@Nullable String legacyData) {
        UUID uuid = PlayerUtil.getPlayerUIDUnstable(legacyData);
        if (uuid != null) {
            return new UserIdent(uuid, legacyData);
        } else if (legacyData != null) {
            return new UserIdent(null, legacyData);
        } else {
            return NOBODY;
        }
    }

    /**
     * Create a UserIdent from a GameProfile. Use this when creating a UserIdent for a currently active player.
     */
    public static @NotNull UserIdent create(@Nullable GameProfile gameProfile) {
        if (gameProfile != null && (gameProfile.getId() != null || gameProfile.getName() != null)) {
            if (gameProfile.getId() != null && gameProfile.getName() != null &&
                    gameProfile.getId().equals(offlineUUID(gameProfile.getName()))) {
                return new UserIdent(null, gameProfile.getName());
            } else {
                return new UserIdent(gameProfile.getId(), gameProfile.getName());
            }
        } else {
            return NOBODY;
        }
    }

    private static @NotNull UUID offlineUUID(@Nullable String playerName) {
        return NullHelper.notnullJ(UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(Charsets.UTF_8)),
                "UUID.nameUUIDFromBytes()");
    }

    UserIdent(@Nullable UUID uuid, @Nullable String playerName) {
        this.uuid = uuid;
        this.uuid_offline = offlineUUID(playerName);
        this.playerName = playerName != null ? playerName : "[" + uuid + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + playerName.hashCode();
        final UUID uuidNullChecked = uuid;
        result = prime * result + ((uuidNullChecked == null) ? 0 : uuidNullChecked.hashCode());
        return result;
    }

    /**
     * Please note that a UserIdent will successfully equal against GameProfiles and UUIDs.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UserIdent) {
            return equals((UserIdent) obj);
        } else if (obj instanceof GameProfile) {
            return equals((GameProfile) obj);
        } else if (obj instanceof UUID) {
            return equals((UUID) obj);
        }
        return false;
    }

    public boolean equals(UserIdent other) {
        if (this.uuid != null && other.uuid != null) {
            return this.uuid.equals(other.uuid);
        }
        return this.uuid_offline.equals(other.uuid_offline);
    }

    public boolean equals(UUID other) {
        return other.equals(uuid) || other.equals(uuid_offline);
    }

    public boolean equals(GameProfile other) {
        UUID other_uuid = other.getId();
        if (this.uuid != null && other_uuid != null) {
            return this.uuid.equals(other_uuid);
        }
        UUID uuid_offline_other = offlineUUID(other.getName());
        return uuid_offline_other.equals(this.uuid) || this.uuid_offline.equals(uuid_offline_other);
    }

    public void saveToNbt(@NotNull NBTTagCompound nbt, @NotNull String prefix) {
        if (uuid != null) {
            nbt.setString(prefix + ".uuid", uuid.toString());
        }
        nbt.setString(prefix + ".login", playerName);
    }

    public static boolean existsInNbt(@NotNull NBTTagCompound nbt, @NotNull String prefix) {
        return nbt.hasKey(prefix + ".uuid") || nbt.hasKey(prefix + ".login");
    }

    public static @NotNull UserIdent readfromNbt(@NotNull NBTTagCompound nbt, @NotNull String prefix) {
        String suuid = nbt.getString(prefix + ".uuid");
        String login = NullHelper.untrusted(nbt.getString(prefix + ".login"), "NBTTagCompound.getString()");
        if (Nobody.NOBODY_MARKER.equals(suuid)) {
            return NOBODY;
        }
        try {
            UUID uuid = UUID.fromString(suuid);
            return create(uuid, login);
        } catch (IllegalArgumentException e) {
            if (login.isEmpty()) {
                return NOBODY;
            } else {
                return new UserIdent(null, login);
            }
        }
    }

    @Override
    public String toString() {
        return "User [uuid=" + (uuid != null ? uuid : "(unknown)") + ", name=" + playerName + "]";
    }

    public static final @NotNull Nobody NOBODY = new Nobody();

    private static class Nobody extends UserIdent {

        private static final @NotNull String NOBODY_MARKER = "nobody";

        Nobody() {
            super(null, "[unknown player]");
        }

        @Override
        public boolean equals(UserIdent other) {
            return this == other;
        }

        @Override
        public void saveToNbt(@NotNull NBTTagCompound nbt, @NotNull String prefix) {
            nbt.setString(prefix + ".uuid", NOBODY_MARKER);
        }
    }
}
