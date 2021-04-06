package pw.twpi.reportingforgemod;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;
import java.util.UUID;

public class Config {
    public static final String CATEGORY_GENERAL = "general";

    private static final ForgeConfigSpec.Builder SERVER_BUILDER
            = new ForgeConfigSpec.Builder();

    // Only one common config
    public static ForgeConfigSpec SERVER_CONFIG;

    // General Settings
    public static ForgeConfigSpec.ConfigValue<String> REPORTING_URL;
    public static ForgeConfigSpec.ConfigValue<String> SERVER_NAME;
    public static ForgeConfigSpec.IntValue REPORT_INTERVAL;

    static {
        // General Settings
        SERVER_BUILDER.comment("General configuration").push(CATEGORY_GENERAL);
        setupGeneralConfig();
        SERVER_BUILDER.pop();


        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    private static void setupGeneralConfig() {
        REPORTING_URL = SERVER_BUILDER.comment("Full server URL for the reporting endpoint.")
                .define("reportingUrl", "");
        SERVER_NAME = SERVER_BUILDER.comment("Server name reported. This should be unique.")
                .define("serverName", UUID.randomUUID().toString());
        REPORT_INTERVAL = SERVER_BUILDER.comment("Interval to report in seconds.")
                .defineInRange("reportInterval", 10, 1, Integer.MAX_VALUE);
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ModConfigEvent configEvent) {
    }
}
