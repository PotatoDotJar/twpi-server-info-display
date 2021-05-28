import sys
from lcd_handler import lcd 

try:
    val = ""
    while val != "QUIT":
        val = input("Enter input: ")
        val = val.replace("##", "\n")
        lcd.clear()
        lcd.message = val
except KeyboardInterrupt:
    lcd.clear()
sys.exit(0)