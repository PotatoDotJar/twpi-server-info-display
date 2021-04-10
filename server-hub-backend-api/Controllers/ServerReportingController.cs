﻿using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;
using Microsoft.Extensions.Logging;
using server_hub_backend_api.Hubs;
using server_hub_backend_api.Models;
using server_hub_backend_api.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace server_hub_backend_api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ServerReportingController : ControllerBase
    {
        private readonly ILogger<ServerReportingController> _logger;
        private readonly IHubContext<NotificationHub> _hubContext;

        private readonly Dictionary<string, ServerReport> currentServerReports;

        public ServerReportingController(ILogger<ServerReportingController> logger, IHubContext<NotificationHub> hubContext)
        {
            _logger = logger;
            _hubContext = hubContext;
            currentServerReports = new Dictionary<string, ServerReport>();
        }

        [HttpGet]
        public StatusResponse IsWorking()
        {
            return new StatusResponse() { Success = true, Message = "All good." };
        }

        [HttpPost("SendReport")]
        public async Task<StatusResponse> SendReport(ServerReport serverReport)
        {
            if(serverReport != null && !string.IsNullOrEmpty(serverReport.ServerName))
            {
                if(!currentServerReports.ContainsKey(serverReport.ServerName))
                {
                    // Add
                    currentServerReports.Add(serverReport.ServerName, serverReport);
                }
                else
                {
                    // Update
                    currentServerReports[serverReport.ServerName] = serverReport;
                }

                await _hubContext.Clients.All.SendAsync("OnReceivedReport", currentServerReports);
                return new StatusResponse() { Success = true, Message = "Response recorded." };
            }

            return new StatusResponse() { Success = false, Message = "Invalid payload." };
        }

    }
}
