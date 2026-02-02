package com.micahslife.ml_echoes.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;

public class ConfigHandler {

    private boolean canCraftWand = true;
    public boolean getCanCraftWand() { return canCraftWand; }
    public void setCanCraftWand(boolean canCraft) { this.canCraftWand = canCraft; }

    public static final BuilderCodec<ConfigHandler> CODEC =
            BuilderCodec.builder(ConfigHandler.class, ConfigHandler::new)
                    .append(new KeyedCodec<>("CanCraftWand", Codec.BOOLEAN),
                            (data, value) -> data.canCraftWand = value,
                            data -> data.canCraftWand)
                    .addValidator(Validators.nonNull())
                    .add()
                    .build();

    public ConfigHandler() {}

}
