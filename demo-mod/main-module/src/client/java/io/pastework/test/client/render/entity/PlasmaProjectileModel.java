package io.pastework.test.client.render.entity;


import com.mojang.math.Constants;
import io.pastework.test.common.PasteworkTest;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class PlasmaProjectileModel extends EntityModel<PlasmaProjectileRenderState>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
        Identifier.fromNamespaceAndPath(PasteworkTest.MOD_ID, "plasma_projectile"),
        "main"
    );

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
        PasteworkTest.MOD_ID,
        "textures/entity/plasma_projectile.png"
    );

    private final ModelPart energy0;
    private final ModelPart core;
    private final ModelPart energy1;

    public PlasmaProjectileModel(ModelPart root)
    {
        super(root);
        this.energy0 = root.getChild("energy0");
        this.core = root.getChild("core");
        this.energy1 = root.getChild("energy1");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition energy0 = partdefinition.addOrReplaceChild(
            "energy0",
            CubeListBuilder.create(),
            PartPose.offset(-2.0F, 22.0F, 2.0F)
        );

        PartDefinition cube_r1 = energy0.addOrReplaceChild(
            "cube_r1",
            CubeListBuilder.create()
                .texOffs(0, 8)
                .addBox(-6.0F, -2.0F, 0.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(-1.0F, 3.0F, -3.0F, 0.0F, 0.0F, 1.5708F)
        );

        PartDefinition core = partdefinition.addOrReplaceChild(
            "core",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-2.0F, 22.0F, 2.0F)
        );

        PartDefinition energy1 = partdefinition.addOrReplaceChild(
            "energy1",
            CubeListBuilder.create()
                .texOffs(0, 8)
                .addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-2.0F, 22.0F, 2.0F)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(PlasmaProjectileRenderState renderState)
    {
        super.setupAnim(renderState);
        final float rot = renderState.ageInTicks * 45f * Constants.DEG_TO_RAD;
        core.yRot += rot / 2;
        energy0.xRot -= rot;
        energy1.yRot -= rot;

        pulseModelScale(renderState, core);
        pulseModelScale(renderState, energy0);
        pulseModelScale(renderState, energy1);
    }

    private void pulseModelScale(PlasmaProjectileRenderState renderState, ModelPart part)
    {
        final float PULSE_SPEED = 50.0f;
        float x = renderState.ageInTicks * PULSE_SPEED;
        float pulse =  (float) Math.sin((x * Constants.DEG_TO_RAD) / 2);
        float scale = 1.0f + (pulse / 4);

        part.xScale = scale;
        part.yScale = scale;
        part.zScale = scale;
    }
}