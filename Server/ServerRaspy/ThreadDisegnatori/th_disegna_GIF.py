#coding=utf-8

# -- IMPORT
from termcolor import cprint
import threading
from threading import Thread
import LEDstrip
import variables
from neopixel import *
# -- FINE IMPORT


class GIF(Thread):
    def __init__(self, vettore):
        Thread.__init__(self)
        self.vett = vettore

    def run(self):
        self.vett = self.vett[1:]  # tolgo il tipo al vettore

        while variables.th_GIF_stop == False:
            cprint((threading.current_thread().getName(), "DRAWING GIF"), 'green')
            # cheack thread attivi
            cprint(("[th_disegna_IMG] - NUMERO THREAD ATTIVI: ",threading.active_count()), 'green')

            n=len(self.vett)/LEDstrip.LED_COUNT

            #invio a LED
            for img in range(int(n)):
                for i in range(LEDstrip.LED_COUNT):
                    # RGB = (R*65536)+(G*256)+B , (when R is RED, G is GREEN and B is BLUE)

                    # ricavo l'RGB del colore
                    RGBint = int(float(self.vett[(img * LEDstrip.LED_COUNT) + i]))  # DA NOTARE SE CONTA DA 1 O 0
                    Blue = RGBint & 255
                    Green = (RGBint >> 8) & 255
                    Red = (RGBint >> 16) & 255

                    LEDstrip.strip.setPixelColor(i, Color(Red, Green, Blue))

                    if variables.th_GIF_stop != False:
                        return

                LEDstrip.strip.show()
                variables.clock.tick(variables.FPS)
                cprint((threading.current_thread().getName(), " | ", variables.clock.get_fps()), 'green')