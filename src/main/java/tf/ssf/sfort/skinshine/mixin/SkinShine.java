package tf.ssf.sfort.skinshine.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ArmorFeatureRenderer.class, priority = 1202)
public class SkinShine{
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LivingEntity livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo info) {
		if (livingEntity instanceof PlayerEntity && Config.shouldHide(livingEntity.getHealth())
				|| livingEntity instanceof ClientPlayerEntity && Config.keepSelfHidden)
			info.cancel();
	}
	@Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
	public void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity livingEntity, EquipmentSlot equipmentSlot, int i, BipedEntityModel<LivingEntity> bipedEntityModel, CallbackInfo info) {
		if (livingEntity instanceof PlayerEntity && Config.listSlot.contains(equipmentSlot))
			info.cancel();
	}
}
