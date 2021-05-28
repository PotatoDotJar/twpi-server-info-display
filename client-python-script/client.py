from signalrcore.hub_connection_builder import HubConnectionBuilder
import logging
import sys
from time import sleep
import threading
from lcd_handler import lcd 


Server_URL = "http://192.168.1.43:49156/notifications"
server_list = None

def onConnectionOpened():
    global lcd
    print("SignalR connection opened")
    lcd.message = "SignalR\ncon. opened"

def onConnectionClosed():
    global lcd
    print("SignalR connection closed")
    lcd.message = "SignalR\ncon. closed"

def onFailedToStartHub():
    global lcd
    print("SignalR failed to connect")
    lcd.message = "SignalR\ncon. failed"

def printServerDetails(server_list):
    for key in server_list:
        server_data = server_list[key]
        print("{0}:".format(key))
        print("\tPlayers: {0} of {1}".format(server_data["playersOnline"], server_data["totalPlayerSlots"]))
        print("\tAvg TPS: {0}".format(str(server_data["tickInfo"]["meanTps"])))
        print("\tAvg Ticktime: {0}".format(server_data["tickInfo"]["meanTickTime"]))
        print("\tDimensions:")
        for dimKey in server_data["tickInfo"]["dimensionTickInfo"]:
            dimension_data = dimKey
            print("\t\tDim {0}:".format(dimension_data["worldId"]))
            print("\t\t\tWorld TPS: {0}".format(dimension_data["worldTps"]))
            print("\t\t\tWorld Ticktime: {0}".format(dimension_data["worldTickTime"]))
    print("\n\n")

def onReceivedReport(data):
    # Will always be first element
    global server_list
    server_list = data[0]

def displayLoopThread():
    global server_list
    global lcd

    a = 0
    while True:
        if server_list != None:
            serverNames = list(server_list.keys())

            if(len(serverNames) > 0):
                server = server_list[serverNames[0]]
                lcd.clear()
                lcd.message = "{0}\nTPS:{1} {2}".format(str(server["serverName"])[0:5].ljust(5),\
                    str(server["tickInfo"]["meanTps"]).ljust(2), "{0}/{1}".format(server["playersOnline"], server["totalPlayerSlots"]))
            else:
                lcd.clear()
                lcd.message = "No servers\nconnected"
            sleep(1)

hub = HubConnectionBuilder()\
    .with_url(Server_URL)\
    .configure_logging(logging.INFO)\
    .with_automatic_reconnect({
        "type": "interval",
        "keep_alive_interval": 10,
        "intervals": [1, 3, 5, 6, 7, 87, 3]
    }).build()

# Handle on connect and disconnected
hub.on_open(onConnectionOpened)
hub.on_close(onConnectionClosed)

hub.on("OnReceivedReport", onReceivedReport)

try:
    hub.start()

    # Start display thread
    displayThread = threading.Thread(target=displayLoopThread)
    displayThread.start()


    while True:
        # Continue running
        pass
except ConnectionError:
    onFailedToStartHub()


hub.stop()
sys.exit(0)