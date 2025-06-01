package net.engineerofchaos.firesupport;

import net.engineerofchaos.firesupport.item.ModItemGroups;
import net.engineerofchaos.firesupport.item.ModItems;
import net.fabricmc.api.ModInitializer;

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
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
	}
}