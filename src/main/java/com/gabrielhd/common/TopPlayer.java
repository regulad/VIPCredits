package com.gabrielhd.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TopPlayer {
    private final @Nullable String playerName;
    private final @NotNull UUID playerUuid;
    private final @NotNull Integer credits;
}
