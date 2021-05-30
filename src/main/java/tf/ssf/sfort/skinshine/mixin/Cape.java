package tf.ssf.sfort.skinshine.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityModel.class)
public class Cape<T extends LivingEntity> {
	@Shadow
	private final ModelPart cape;

	public Cape(ModelPart cape) {
		this.cape = cape;
	}

	@Inject(method = "setAngles",at=@At("TAIL"))
	public void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
		if (livingEntity instanceof PlayerEntity && Config.shouldHide(livingEntity.getHealth()) || livingEntity instanceof ClientPlayerEntity && Config.keepSelfHidden)
			if (livingEntity.isInSneakingPose()) {
				this.cape.pivotZ = 1.4F;
				this.cape.pivotY = 1.85F;
			} else {
				this.cape.pivotZ = 0.0F;
				this.cape.pivotY = 0.0F;
			} }
}
