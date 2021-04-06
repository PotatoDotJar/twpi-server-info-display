using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using MQTTnet;
using MQTTnet.Protocol;
using MQTTnet.Server;
using server_hub_backend_api.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace server_hub_backend_api.Service
{
    public class MQTTService
    {
        private readonly ILogger<MQTTService> _logger;
        private IMqttServer mqttServer;
        private readonly IConfiguration _configuration;

        public MQTTService(
            ILogger<MQTTService> logger,
            IConfiguration configuration)
        {
            _logger = logger;
            _configuration = configuration;

            var optionsBuilder = new MqttServerOptionsBuilder()
                .WithDefaultEndpointPort(1883)
                .WithDefaultCommunicationTimeout(TimeSpan.FromSeconds(5))
                .WithConnectionValidator((context) => ValidateConnections(context));


            // Start a MQTT server.
            mqttServer = new MqttFactory().CreateMqttServer();
            mqttServer.StartAsync(optionsBuilder.Build()).Wait();

            mqttServer.UseClientConnectedHandler((a) => OnClientConnect(a));
            mqttServer.UseClientDisconnectedHandler((a) => OnClientDisconnect(a));

            _logger.LogInformation("Started MQTT Server");
        }

        // When a client connects
        private void OnClientConnect(MqttServerClientConnectedEventArgs evnt)
        {
            _logger.LogInformation("Client \"{0}\" Connected", evnt.ClientId);
        }

        // When a client disconnects
        private void OnClientDisconnect(MqttServerClientDisconnectedEventArgs evnt)
        {
            _logger.LogInformation("Client \"{0}\" Disconnected", evnt.ClientId);
        }

        // Validate connection
        private void ValidateConnections(MqttConnectionValidatorContext context)
        {
            context.ReasonCode = MqttConnectReasonCode.Success;
        }
    }
}
