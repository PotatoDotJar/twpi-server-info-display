from signalrcore.hub_connection_builder import HubConnectionBuilder
import logging
import sys

Server_URL = "http://192.168.1.31:49160/notifications"

def onReceivedReport(data):
    print("Report for " + str(type(data)))


hub = HubConnectionBuilder()\
    .with_url(Server_URL, options={"verify_ssl": False})\
    .configure_logging(logging.INFO)\
    .with_automatic_reconnect({
        "type": "interval",
        "keep_alive_interval": 10,
        "intervals": [1, 3, 5, 6, 7, 87, 3]
    }).build()

hub.on_open(lambda: print("SignalR connection opened"))
hub.on_close(lambda: print("SignalR connection closed"))

hub.on("OnReceivedReport", onReceivedReport)

hub.start()
message = None

while message != "exit()":
    message = input(">> ")

hub.stop()
sys.exit(0)