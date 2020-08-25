package sf.ssf.sfort.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Environment(EnvType.CLIENT)
@Mixin(ArmorFeatureRenderer.class)
public class SkinShine{
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LivingEntity livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo info) {
		if (livingEntity instanceof ClientPlayerEntity && sf.ssf.sfort.SkinShine.keepSelfHidden)
			info.cancel();
		if (livingEntity instanceof PlayerEntity)
			if (sf.ssf.sfort.SkinShine.shouldHide(livingEntity.getHealth()))
				info.cancel();
	}
}
