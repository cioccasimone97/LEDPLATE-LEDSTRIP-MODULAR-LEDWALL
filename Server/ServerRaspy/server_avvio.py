#coding=utf-8

# -- IMPORT
from socket import *
from threading import Thread
from termcolor import cprint
import pygame
import signal

import threading, time, logo_primo_avvio, variables

from ThreadDisegnatori import th_disegna_GIF, th_disegna_IMG, LEDstrip
# -- FINE IMPORT


# variabili globali
vettore="" #inizializzo la variabile vettore


HOST = '' #DA LASCIARE COSI!!!
PORT = 5005
#BUFSIZ = 65536
BUFSIZ = 30000000 #da sistemare con FPS e proporzioni + 60s
ADDR = (HOST, PORT)
serversock = socket(AF_INET, SOCK_STREAM)
serversock.bind(ADDR)
serversock.listen(1)


#FUNZIONI
def first_run():
    global vettore, th_IMG

    PORT_FIRST =PORT+1
    ADDR_FIRST = (HOST, PORT_FIRST)
    serversock_first = socket(AF_INET, SOCK_STREAM)
    serversock_first.bind(ADDR_FIRST)
    serversock_first.listen(1)

    #--------------------------------------
    cprint("[SERVER AVVIO] - waiting on configuration (MAIN_SERVER_2)", 'blue')
    clientsock_first, ADDR_FIRST = serversock_first.accept()
    print('connected from:', ADDR_FIRST)
    data_first = clientsock_first.recv(1024).decode()  # dati ricevuti dal client
    clientsock_first.send(data_first.encode())
    cprint("CONFIG RICEVUTA", 'blue')

    vettore_first = data_first.split(",")
    # ----------------------------------------------

    #--- VISUALIZZO I DATI DI CONFIGURAZIONE
    variables.n_pan_riga=int(float(vettore_first[0]))
    variables.n_pan_colonna=int(float(vettore_first[1]))
    variables.led_pannello_riga=int(float(vettore_first[2]))
    variables.led_pannello_colonna=int(float(vettore_first[3]))

    righe=variables.n_pan_riga*variables.led_pannello_riga
    colonne=variables.n_pan_colonna*variables.led_pannello_colonna
    LEDstrip.LED_COUNT=righe*colonne; #led totali
    #---

    clientsock_first.close()
    serversock_first.close()
    #----------------------------

    LEDstrip.creaStrip()

    # al primo avvio carico l'IMG salvata
    variables.th_IMG_stop = False
    variables.th_GIF_stop = True

    vettore = logo_primo_avvio.dati.split(",")

    th_IMG = th_disegna_IMG.IMG(vettore)
    th_IMG.start()


#stoppo tutto
def stop_all():
    variables.th_IMG_stop = True
    variables.th_GIF_stop = True

def keyboardInterruptHandler(signal, frame):
    print("KeyboardInterrupt (ID: {}) has been caught. Cleaning up...".format(signal))
    stop_all()
    LEDstrip.clearAll()
    exit(0)

#------------------------------------------------------ FINE FUNZIONI


# AVVIO PROGRAMMA
if __name__ == "__main__":
    # ------------MAIN
    
    variables.clock=pygame.time.Clock() #variable.clock per FPS

    # ------------------------------------ PRIMO AVVIO
    first_run()
    # ------------------------------------ FINE PRIMO AVVIO
    

    signal.signal(signal.SIGINT, keyboardInterruptHandler)

    while 1:
        cprint("[SERVER AVVIO] - waiting on connection (MAIN_SERVER_2)", 'blue')
        clientsock, addr = serversock.accept()
        print('connected from:', addr)

        data = ""

        #attendo fino alla END di aver ricevuto tutto
        while 1:
            data_in = clientsock.recv(BUFSIZ)
            if data_in.decode('utf-8') == "END": break
            data = data + data_in.decode('utf-8')


        clientsock.send(data.encode())
        cprint("DATI RICEVUTI", 'blue')
        # cprint(("received data:", data),'blue')  # scrive a console i dati ricevuti

        # ----------------------------------------------- FERMO TUTTO E FACCIO PARTIRE IL NUOVO
        stop_all()
        time.sleep(1)  # wait 1sec

        # trasformo la string data in list (vettore)
        vettore = data.split(",")
        # INDICE che definisce il tipo di file ricevuto e determina un tipo di riproduzione
        cprint(("VETT[0]:", vettore[0]), 'blue')

        # lancio state machine
        tipo=int(float(vettore[0]))
        cprint(("TIPO: ", tipo), 'blue') # il primo elemento determina il TIPO

        # lancio il thread che disegnerà a schermo il tipo di file ricevuto
        if tipo == 0: #IMG
            variables.FPS=60; #set FPS
            variables.th_IMG_stop = False
            th_IMG = th_disegna_IMG.IMG(vettore)
            th_IMG.start()
        elif tipo >= 1: #GIF/VIDEO
            variables.FPS=tipo #FPS in funzione della GIF
            #variables.FPS=2; #set FPS
            variables.th_GIF_stop = False
            th_GIF = th_disegna_GIF.GIF(vettore)
            th_GIF.start()

        # ----------------------------------------------

        clientsock.close()
    serversock.close()
# ------------ FINE MAIN
