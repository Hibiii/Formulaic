package net.fabricmc.example.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import net.fabricmc.example.PrototypeCarEntity;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerControlCarMixin extends PlayerEntity {
	public PlayerControlCarMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	@Shadow
	private boolean riding;
	@Shadow
	public Input input;
	
	@Inject(at = @At("TAIL"), method = "tickRiding()V")
	public void checkForCar (CallbackInfo ci) {
		if(this.getVehicle() instanceof PrototypeCarEntity) {
			PrototypeCarEntity car = (PrototypeCarEntity)this.getVehicle();
			car.setInputs(this.input.pressingLeft, this.input.pressingRight, this.input.pressingForward, this.input.pressingBack);
			this.riding |= (this.input.pressingForward || this.input.pressingRight || this.input.pressingLeft || this.input.pressingBack);
		}
	}
}
