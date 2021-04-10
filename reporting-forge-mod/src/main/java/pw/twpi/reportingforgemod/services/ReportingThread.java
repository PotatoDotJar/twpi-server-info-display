package pw.twpi.reportingforgemod.services;

import com.google.gson.Gson;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.server.ServerWorld;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
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

    public ReportingThread(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while(server.isRunning()) {
            LOGGER.debug("Reporting to server");

            SendReport(server);

            try {
                Thread.sleep(Config.REPORT_INTERVAL.get());
            } catch (InterruptedException ignored) { }
        }
    }

    private void SendReport(MinecraftServer server) {

        HttpClient httpClient = HttpClientBuilder.create().build();
        Gson gson = new Gson();

        try {
            HttpPost reportRequest = new HttpPost(Config.REPORTING_URL.get());

            // Build report
            ServerReport report = new ServerReport();
            report.setServerName(Config.SERVER_NAME.get());
            report.setTickInfo(getTickInfo(server));
            report.setPlayersOnline(server.getPlayerCount());
            report.setTotalPlayerSlots(server.getMaxPlayers());

            String jsonRequest = gson.toJson(report);
            reportRequest.setEntity(new StringEntity(jsonRequest));
            reportRequest.setHeader("Content-type", "application/json");

            httpClient.execute(reportRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
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
