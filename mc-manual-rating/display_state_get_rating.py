#!/usr/bin/env python

import time
import os
import os.path
from os import path
from socketIO_client import SocketIO, LoggingNamespace

# get the path of the script
script_path = os.path.dirname(os.path.abspath( __file__ ))
# set script path as current directory -
os.chdir(script_path)

status = ""
artist = ""
album = ""
title = ""

def on_connect():
    print('connect')
    return 'connected'

def on_push_state(*args):
    global status, artist, album, title
    if artist == args[0]['artist'] and artist == args[0]['artist'] and artist == args[0]['artist'] :
        return 'hallo'

    status = args[0]['status']
    artist = args[0]['artist']
    album = args[0]['album']
    title = args[0]['title']
    print((status + '-' + artist + '-' + album + '-' + title).encode('ascii', 'ignore'))

    return 'hallo'

socketIO = SocketIO('192.168.1.138', 3000)
socketIO.on('connect', on_connect)
status = 'unknown'


def main():
    while True:
        #mit socket verbinden
        socketIO.on('pushState', on_push_state)
        socketIO.wait_for_callbacks(seconds=1)
        result = socketIO.emit('getState', on_push_state)

        time.sleep(2)

if __name__ == '__main__':
    try:
        main()
        socketIO.wait()
        #socketIO.wait_for_callbacks(seconds=1)
    except KeyboardInterrupt:

        pass
