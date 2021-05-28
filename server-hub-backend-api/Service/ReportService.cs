using Microsoft.AspNetCore.SignalR;
using Microsoft.Extensions.Logging;
using server_hub_backend_api.Hubs;
using server_hub_backend_api.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace server_hub_backend_api.Service
{
    public class ReportService
    {
        private readonly ILogger<ReportService> _logger;
        private readonly IHubContext<NotificationHub> _hubContext;

        // Data store
        private readonly Dictionary<string, ServerReport> currentServerReports;

        public ReportService(ILogger<ReportService> logger, IHubContext<NotificationHub> hubContext)
        {
            _logger = logger;
            _hubContext = hubContext;
            currentServerReports = new Dictionary<string, ServerReport>();
        }

        public async Task<bool> ConsumeReport(ServerReport report)
        {
            if (report != null && !string.IsNullOrEmpty(report.ServerName))
            {
                report.LastUpdated = DateTime.Now;

                if (!currentServerReports.ContainsKey(report.ServerName))
                {
                    // Add
                    currentServerReports.Add(report.ServerName, report);
                }
                else
                {
                    // Update
                    currentServerReports[report.ServerName] = report;
                }

                CleanupStaleServers();

                await _hubContext.Clients.All.SendAsync("OnReceivedReport", currentServerReports);

                return true;
            }

            return false;
        }

        public async Task<bool> ServerStopping(ServerReport report)
        {
            if (report != null && !string.IsNullOrEmpty(report.ServerName))
            {
                // Remove server from reports
                currentServerReports.Remove(report.ServerName);

                CleanupStaleServers();

                await _hubContext.Clients.All.SendAsync("OnReceivedReport", currentServerReports);

                return true;
            }

            return false;
        }

        public Dictionary<string, ServerReport> GetServerReports()
        {
            return currentServerReports;
        }

        private void CleanupStaleServers()
        {
            foreach (var kvServer in currentServerReports)
            {
                if((DateTime.Now - kvServer.Value.LastUpdated).TotalMinutes > 20)
                {
                    // If the server last report time was more than 20 min ago, remove it
                    currentServerReports.Remove(kvServer.Key);
                    _logger.LogInformation("Removed {0} from the server list. Last report was over 20min ago.", kvServer.Key);
                }
            }
        }
    }
}
