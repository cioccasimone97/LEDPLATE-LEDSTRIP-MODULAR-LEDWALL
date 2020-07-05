#coding=utf-8

# -- IMPORT
from termcolor import cprint
from threading import Thread
import threading
import socket
import variables

import LEDstrip
from neopixel import *
# -- FINE IMPORT

class IMG (Thread):
    def __init__(self,vettore):
        Thread.__init__(self)
        self.vett=vettore
    
    def run(self):
        self.vett=self.vett[1:] #tolgo il tipo al vettore
        
        while variables.th_IMG_stop == False:
            cprint((threading.current_thread().getName(), "DRAWING IMG"), 'yellow')

            # cheack thread attivi
            cprint(("[th_disegna_IMG] - NUMERO THREAD ATTIVI: ",threading.active_count()), 'yellow')

            

            #invio a LED
            for i in range(LEDstrip.LED_COUNT):
                # RGB = (R*65536)+(G*256)+B , (when R is RED, G is GREEN and B is BLUE)

                #ricavo l'RGB del colore
                RGBint=int(float(self.vett[i]))
                Blue = RGBint & 255
                Green = (RGBint >> 8) & 255
                Red = (RGBint >> 16) & 255

                LEDstrip.strip.setPixelColor(i, Color(Red,Green,Blue))
            LEDstrip.strip.show()

            cprint((threading.current_thread().getName(), " | ", variables.clock.get_fps()), 'yellow')
            variables.clock.tick(variables.FPS)
