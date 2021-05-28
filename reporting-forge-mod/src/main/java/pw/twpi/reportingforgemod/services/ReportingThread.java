package pw.twpi.reportingforgemod.services;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pw.twpi.reportingforgemod.Config;
import pw.twpi.reportingforgemod.models.DimensionTickInfo;
import pw.twpi.reportingforgemod.models.ServerReport;
import pw.twpi.reportingforgemod.models.TickInfo;

import java.util.ArrayList;
import java.util.List;

public class ReportingThread implements Runnable {

    private final MinecraftServer server;
    private static final Logger LOGGER = LogManager.getLogger();
    private final HubConnection hubConnection;

    public ReportingThread(MinecraftServer server) {
        this.server = server;
        this.hubConnection = HubConnectionBuilder.create(Config.REPORTING_URL.get()).build();
        this.hubConnection.start();
    }

    public void serverStopping() {
        if(this.hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
            ServerReport report = new ServerReport();
            report.setServerName(Config.SERVER_NAME.get());

            LOGGER.debug("Sending server stop to " + Config.REPORTING_URL.get());
            this.hubConnection.send("ServerStopping", report);
        }
    }

    @Override
    public void run() {
        while(server.isRunning()) {
            ServerReport report = GetReport(server);
            SendReport(report);

            try {
                Thread.sleep(Config.REPORT_INTERVAL.get());
            } catch (InterruptedException ignored) { }
        }
    }

    public void SendReport(ServerReport report) {
        if(this.hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
            //LOGGER.debug("Sending report to " + Config.REPORTING_URL.get());
            this.hubConnection.send("SendReport", report);
        } else {
            this.hubConnection.start();
        }
    }

    private ServerReport GetReport(MinecraftServer server) {
        // Build report
        ServerReport report = new ServerReport();
        report.setServerName(Config.SERVER_NAME.get());
        report.setTickInfo(getTickInfo(server));
        report.setPlayersOnline(server.getPlayerCount());
        report.setTotalPlayerSlots(server.getMaxPlayers());

        return report;
    }

    private TickInfo getTickInfo(MinecraftServer server) {
        List<DimensionTickInfo> dimensionTickInfoList = new ArrayList<>();

        for (ServerWorld dim : server.getAllLevels()) {
            dimensionTickInfoList.add(getDimensionTickInfo(server, dim));
        }

        double meanTickTime = mean(server.tickTimes) * 1.0E-6D;
        double meanTPS = Math.min(1000.0 / meanTickTime, 20);

        return new TickInfo(meanTickTime, meanTPS, dimensionTickInfoList);
    }

    private static DimensionTickInfo getDimensionTickInfo(MinecraftServer server, ServerWorld dim)
    {
        long[] times = server.getTickTime(dim.dimension());

        if (times == null) // Null means the world is unloaded. Not invalid. That's taken car of by DimensionArgument itself.
            times = new long[]{0};

        final Registry<DimensionType> reg = server.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        double worldTickTime = mean(times) * 1.0E-6D;
        double worldTPS = Math.min(1000.0 / worldTickTime, 20);

        return new DimensionTickInfo(reg.getId(dim.dimensionType()), worldTickTime, worldTPS);
    }

    private static long mean(long[] values)
    {
        long sum = 0L;
        for (long v : values)
            sum += v;
        return sum / values.length;
    }
}
