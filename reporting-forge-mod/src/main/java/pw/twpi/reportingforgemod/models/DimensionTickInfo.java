package pw.twpi.reportingforgemod.models;

public class DimensionTickInfo {

    private int worldId;
    private double worldTickTime;
    private double worldTps;

    public DimensionTickInfo(int worldId, double worldTickTime, double worldTps) {
        this.worldId = worldId;
        this.worldTickTime = worldTickTime;
        this.worldTps = worldTps;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public double getWorldTickTime() {
        return worldTickTime;
    }

    public void setWorldTickTime(double worldTickTime) {
        this.worldTickTime = worldTickTime;
    }

    public double getWorldTps() {
        return worldTps;
    }

    public void setWorldTps(double worldTps) {
        this.worldTps = worldTps;
    }
}
