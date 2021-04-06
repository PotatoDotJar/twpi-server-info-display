package pw.twpi.reportingforgemod.models;

public class ServerReport {

    private String ServerName;
    private TickInfo tickInfo;
    private int PlayersOnline;
    private int TotalPlayerSlots;

    public String getServerName() {
        return ServerName;
    }

    public void setServerName(String serverName) {
        ServerName = serverName;
    }

    public TickInfo getTickInfo() {
        return tickInfo;
    }

    public void setTickInfo(TickInfo tickInfo) {
        this.tickInfo = tickInfo;
    }

    public int getPlayersOnline() {
        return PlayersOnline;
    }

    public void setPlayersOnline(int playersOnline) {
        PlayersOnline = playersOnline;
    }

    public int getTotalPlayerSlots() {
        return TotalPlayerSlots;
    }

    public void setTotalPlayerSlots(int totalPlayerSlots) {
        TotalPlayerSlots = totalPlayerSlots;
    }
}
