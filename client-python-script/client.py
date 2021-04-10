from signalrcore.hub_connection_builder import HubConnectionBuilder
import logging
import sys
from time import sleep
import threading

# LCD Lib
import board
import digitalio
import adafruit_character_lcd.character_lcd as characterlcd

# LCD Settings
lcd_columns = 16
lcd_rows = 2
lcd_rs = digitalio.DigitalInOut(board.D22)
lcd_en = digitalio.DigitalInOut(board.D17)
lcd_d4 = digitalio.DigitalInOut(board.D25)
lcd_d5 = digitalio.DigitalInOut(board.D24)
lcd_d6 = digitalio.DigitalInOut(board.D23)
lcd_d7 = digitalio.DigitalInOut(board.D18)

Server_URL = "http://192.168.1.38:49154/notifications"

server_list = None

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

def displayLoopThread(name):
    global server_list
    # Initialise the lcd class
    lcd = characterlcd.Character_LCD_Mono(lcd_rs, lcd_en, lcd_d4, lcd_d5, lcd_d6, lcd_d7, lcd_columns, lcd_rows)
    lcd.clear()

    a = 0
    while True:
        if server_list != None:
            firstKey = list(server_list.keys())[0]
            server = server_list[firstKey]
            lcd.message = "{0}\nTPS:{1} {2}".format(str(server["serverName"])[0:5].ljust(5),\
                str(server["tickInfo"]["meanTps"]).ljust(2), "{0}/{1}".format(server["playersOnline"], server["totalPlayerSlots"]))
            sleep(1)



hub = HubConnectionBuilder()\
    .with_url(Server_URL)\
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

# Start display thread
displayThread = threading.Thread(target=displayLoopThread, args=("Display Thread",))
displayThread.start()

while True:
    # Continue running
    pass

hub.stop()
sys.exit(0)