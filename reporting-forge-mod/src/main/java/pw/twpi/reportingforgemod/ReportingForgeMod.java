package pw.twpi.reportingforgemod;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pw.twpi.reportingforgemod.services.ReportingThread;

@Mod("reportingforgemod")
public class ReportingForgeMod
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static ReportingThread reportingThread;

    public ReportingForgeMod() {
        // Register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // Load config
        Config.loadConfig(Config.SERVER_CONFIG, FMLPaths.CONFIGDIR.get().resolve("reportingforgemod.toml"));

        StartSyncThread(event.getServer());
    }

    @SubscribeEvent
    public void onServerStop(final FMLServerStoppingEvent event) {
        LOGGER.info("Server stopping!");
        if(reportingThread != null) {
            reportingThread.serverStopping();
        }
    }

    public void StartSyncThread(MinecraftServer server) {
        reportingThread = new ReportingThread(server);
        Thread reporting = new Thread(reportingThread);
        reporting.start();
        LOGGER.info("Reporting Thread Started!");
    }
}
