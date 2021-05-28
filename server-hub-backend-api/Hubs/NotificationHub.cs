using Microsoft.AspNetCore.SignalR;
using Microsoft.Extensions.Logging;
using server_hub_backend_api.Models;
using server_hub_backend_api.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace server_hub_backend_api.Hubs
{
    public class NotificationHub : Hub
    {
        private readonly ILogger<NotificationHub> _logger;
        private readonly ReportService _reportService;

        public NotificationHub(ILogger<NotificationHub> logger, ReportService reportService)
        {
            _logger = logger;
            _reportService = reportService;
        }

        public async Task SendReport(ServerReport serverReport)
        {
            _logger.LogInformation("Got report from {0}.", serverReport.ServerName);

            await _reportService.ConsumeReport(serverReport);
        }

        public async Task ServerStopping(ServerReport serverReport)
        {
            _logger.LogInformation("{0} server stopping.", serverReport.ServerName);

            await _reportService.ServerStopping(serverReport);
        }
    }
}
