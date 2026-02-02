package com.micahslife.ml_echoes.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

public class EchoStoreComponent implements Component<EntityStore> {

    private int color = 7051198;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static final BuilderCodec<EchoStoreComponent> CODEC =
            BuilderCodec.builder(EchoStoreComponent.class, EchoStoreComponent::new)
                    .append(new KeyedCodec<>("Color", Codec.INTEGER),
                            (data, value) -> data.color = value,
                            data -> data.color)
                    .addValidator(Validators.nonNull())
                    .add()
                    .build();

    public EchoStoreComponent() {}

    public EchoStoreComponent(EchoStoreComponent clone) {
        this.color = clone.color;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new EchoStoreComponent(this);
    }

}
