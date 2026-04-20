package com.gaura.mining_and_placing_animations.animation.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AnimationModel {

    public final float duration;
    public final float[] pivotPoint;
    public final List<Keyframe> keyframes;

    private AnimationModel(float duration, float[] pivotPoint, List<Keyframe> keyframes) {

        this.duration = duration;
        this.pivotPoint = pivotPoint;
        this.keyframes = keyframes;
    }

    public static AnimationModel fromJson(JsonObject jsonObject) {

        float duration = jsonObject.get("duration").getAsFloat();

        JsonArray pivot = jsonObject.getAsJsonArray("pivot_point");
        float[] pivotPoint = new float[]{ pivot.get(0).getAsFloat(), pivot.get(1).getAsFloat(), pivot.get(2).getAsFloat() };

        List<Keyframe> keyframes = new ArrayList<>();

        for (JsonElement jsonElement : jsonObject.getAsJsonArray("keyframes")) {

            keyframes.add(Keyframe.fromJson(jsonElement.getAsJsonObject()));
        }

        keyframes.sort(Comparator.comparingDouble(Keyframe::time));

        return new AnimationModel(duration, pivotPoint, keyframes);
    }

    public void apply(PoseStack poseStack, float progress) {

        float currentTime = progress * this.duration;

        Keyframe from = keyframes.getFirst();
        Keyframe to = keyframes.getLast();

        for (int i = 0; i < keyframes.size() - 1; i++) {

            if (currentTime >= keyframes.get(i).time() && currentTime <= keyframes.get(i + 1).time()) {

                from = keyframes.get(i);
                to = keyframes.get(i + 1);

                break;
            }
        }

        float alpha = (to.time() == from.time()) ? 1.0F : (currentTime - from.time()) / (to.time() - from.time());

        float tx = Mth.lerp(alpha, from.translation()[0], to.translation()[0]);
        float ty = Mth.lerp(alpha, from.translation()[1], to.translation()[1]);
        float tz = Mth.lerp(alpha, from.translation()[2], to.translation()[2]);

        float rx = Mth.lerp(alpha, from.rotation()[0], to.rotation()[0]);
        float ry = Mth.lerp(alpha, from.rotation()[1], to.rotation()[1]);
        float rz = Mth.lerp(alpha, from.rotation()[2], to.rotation()[2]);

        float sx = Mth.lerp(alpha, from.scale()[0], to.scale()[0]);
        float sy = Mth.lerp(alpha, from.scale()[1], to.scale()[1]);
        float sz = Mth.lerp(alpha, from.scale()[2], to.scale()[2]);

        float px = pivotPoint[0];
        float py = pivotPoint[1];
        float pz = pivotPoint[2];

        poseStack.translate(px + tx, py + ty, pz + tz);
        poseStack.mulPose(Axis.XP.rotationDegrees(rx));
        poseStack.mulPose(Axis.YP.rotationDegrees(ry));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rz));
        poseStack.scale(sx, sy, sz);
        poseStack.translate(-px, -py, -pz);
    }
}
