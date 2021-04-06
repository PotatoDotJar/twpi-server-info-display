package pw.twpi.reportingforgemod.models;

import java.util.List;

public class TickInfo {

    private double meanTickTime;
    private double meanTps;

    private List<DimensionTickInfo> dimensionTickInfo;

    public TickInfo(double meanTickTime, double meanTps, List<DimensionTickInfo> dimensionTickInfo) {
        this.meanTickTime = meanTickTime;
        this.meanTps = meanTps;
        this.dimensionTickInfo = dimensionTickInfo;
    }

    public double getMeanTickTime() {
        return meanTickTime;
    }

    public void setMeanTickTime(double meanTickTime) {
        this.meanTickTime = meanTickTime;
    }

    public double getMeanTps() {
        return meanTps;
    }

    public void setMeanTps(double meanTps) {
        this.meanTps = meanTps;
    }

    public List<DimensionTickInfo> getDimensionTickInfo() {
        return dimensionTickInfo;
    }

    public void setDimensionTickInfo(List<DimensionTickInfo> dimensionTickInfo) {
        this.dimensionTickInfo = dimensionTickInfo;
    }
}
