# TWPI Desk Server Info Display

# Requirements

- LCD display
  - Server Name
  - Player Count
  - TPS
  - More?
- Any server with the mod running should be shown on the Raspberry Pi display.

# Technologies

- Java
- Minecraft Forge
- .NET Core WEB API + SignalR Server (No longer using MQTT)
- Raspberry Pi SignalR Client in Python

![](docs/structure.png)

# Implementation

The final product will be a display for our server status&#39;. Kind of like a desk clock, but with an LCD display for realtime player count, server status, tps, etc.

Some examples of what this might look like:
![](docs/epaper-display-example.png)
[Image Source](https://www.raspberrypi.org/blog/build-an-e-paper-to-do-list-with-raspberry-pi/)