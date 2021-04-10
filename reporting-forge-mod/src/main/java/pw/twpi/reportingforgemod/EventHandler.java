package pw.twpi.reportingforgemod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(Dist.DEDICATED_SERVER)
public class EventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void pickupItem(EntityItemPickupEvent event) {
        LOGGER.info("Item picked up...");
    }

    @SubscribeEvent
    public static void placeBlock(BlockEvent.EntityPlaceEvent event) {
        LOGGER.info("Block placed...");
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        event.setResult(Event.Result.DENY);
        LOGGER.info("Block broke...");
    }

}
