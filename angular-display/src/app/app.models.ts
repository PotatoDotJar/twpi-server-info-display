export interface ServerReport {
    serverName: string;
    tickInfo: TickInfo;
    playersOnline: number;
    totalPlayerSlots: number;
    lastUpdated: Date;
}

export interface TickInfo {
    meanTickTime: number;
    meanTps: number;
    dimensionTickInfo: Array<DimensionTickInfo>;
}

export interface DimensionTickInfo {
    worldId: number;
    worldTickTime: number;
    worldTps: number;
}