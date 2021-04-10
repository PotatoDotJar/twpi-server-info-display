using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace server_hub_backend_api.Models
{
    public class ServerReport
    {
        public string ServerName { get; set; }
        public TickInfo TickInfo { get; set; }
        public int PlayersOnline { get; set; }
        public int TotalPlayerSlots { get; set; }
        public DateTime LastUpdated { get; set; }
    }

    public class TickInfo
    {
        public double MeanTickTime { get; set; }
        public double MeanTps { get; set; }
        public List<DimensionTickInfo> DimensionTickInfo { get; set; }
    }

    public class DimensionTickInfo
    {
        public int WorldId { get; set; }
        public double WorldTickTime { get; set; }
        public double WorldTps { get; set; }
    }
}
