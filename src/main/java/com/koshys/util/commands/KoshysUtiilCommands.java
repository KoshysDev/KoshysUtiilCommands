package com.koshys.util.commands;

import com.koshys.util.commands.Commands.PlayedCommand;
import com.koshys.util.commands.Commands.RandomTeleportCommand;
import com.koshys.util.commands.Commands.SexCommand;
import com.koshys.util.commands.Commands.TimePlayedCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KoshysUtiilCommands implements ModInitializer {
	public static final String MODID = "koshysutiilcommands";
	public static final Logger LOGGER = LoggerFactory.getLogger("koshysutiilcommands");

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			RandomTeleportCommand.register(dispatcher);
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			TimePlayedCommand.register(dispatcher);
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			PlayedCommand.register(dispatcher);
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			SexCommand.register(dispatcher);
		});
	}
}