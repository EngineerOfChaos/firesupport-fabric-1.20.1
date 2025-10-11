package net.engineerofchaos.firesupport;

import net.engineerofchaos.firesupport.block.ModBlocks;
import net.engineerofchaos.firesupport.block.entity.ModBlockEntities;
import net.engineerofchaos.firesupport.entity.ModEntities;
import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.engineerofchaos.firesupport.entity.damage.ModDamageTypes;
import net.engineerofchaos.firesupport.item.ModItemGroups;
import net.engineerofchaos.firesupport.item.ModItems;
import net.engineerofchaos.firesupport.network.FireSupportNetworkingConstants;
import net.engineerofchaos.firesupport.screen.ModScreenHandlers;
import net.engineerofchaos.firesupport.shellcomponent.ShellComponentUtil;
import net.engineerofchaos.firesupport.shellcomponent.ShellComponents;
import net.engineerofchaos.firesupport.util.ModRegistries;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FireSupport implements ModInitializer {
	public static final String MOD_ID = "firesupport";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Begin Fire Support Initialisation...");

		ModRegistries.registerModRegistries();
		ShellComponents.registerShellComponents();
		ShellComponents.registerExclusivities();
		ShellComponents.registerDependencies();

		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModEntities.registerModEntities();
		ModDamageTypes.registerModDamageTypes();

		ModBlockEntities.registerBlockEntities();

		ModScreenHandlers.registerScreenHandlers();

		ServerPlayNetworking.registerGlobalReceiver(FireSupportNetworkingConstants.C2S_BULLET_VELOCITY_REQUEST_PACKET_ID,
				((server, player, handler, buf, responseSender) -> {

					int targetBulletID = buf.readInt();

					server.execute(() -> {
						// do stuff here

						BulletEntity targetBullet = (BulletEntity) player.getWorld().getEntityById(targetBulletID);
						if (targetBullet != null) {
							Vec3d velocity = targetBullet.getVelocity();
							Vec3d initialPos = targetBullet.getLaunchPos();

							PacketByteBuf responseBuf = PacketByteBufs.create();
							responseBuf.writeVector3f(velocity.toVector3f());
							responseBuf.writeVector3f(initialPos.toVector3f());
							responseBuf.writeInt(targetBulletID);

							responseSender.sendPacket(FireSupportNetworkingConstants.S2C_BULLET_VELOCITY_PACKET_ID, responseBuf);
						} else {
							FireSupport.LOGGER.info("Server: No target bullet found matching ID!");
						}
					});

				}));
	}
}