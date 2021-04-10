using Microsoft.AspNetCore.Http;
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
        private readonly ReportService _reportService;

        public ServerReportingController(ILogger<ServerReportingController> logger, ReportService reportService)
        {
            _logger = logger;
            _reportService = reportService;
        }

        [HttpGet]
        public StatusResponse IsWorking()
        {
            return new StatusResponse() { Success = true, Message = "All good." };
        }

        [HttpPost("SendReport")]
        public async Task<StatusResponse> SendReport(ServerReport serverReport)
        {
            var res = _reportService.ConsumeReport(serverReport);

            if(await res)
            {
                return new StatusResponse() { Success = true, Message = "Response recorded." };
            } 

            return new StatusResponse() { Success = false, Message = "Invalid report." };
        }

    }
}
