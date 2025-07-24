package com.gaura.mining_quakes.config;

import java.util.function.Function;

public enum EasingFunction {

    LINEAR("Linear", t -> 1.0F),
    EASE_IN("Ease In", t -> (float) Math.sin((t * Math.PI) / 2)),
    EASE_OUT("Ease Out", t -> (float) Math.cos((t * Math.PI) / 2)),
    EASE_IN_OUT("Ease In Out", t -> t < 0.5f ? (float) Math.sin((t * Math.PI) / 2) : (float) Math.cos((t * Math.PI) / 2)),
    EASE_OUT_IN("Ease Out In", t -> t < 0.5f ? (float) Math.cos((t * Math.PI) / 2) : (float) Math.sin((t * Math.PI) / 2));

    private final String name;
    private final Function<Float, Float> function;

    EasingFunction(String name, Function<Float, Float> function) {

        this.name = name;
        this.function = function;
    }

    @Override
    public String toString() {

        return name;
    }

    public float apply(float t) {

        return function.apply(clamp(t));
    }

    private float clamp(float t) {

        return Math.max(0.0F, Math.min(1.0F, t));
    }
}
