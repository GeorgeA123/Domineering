#!/usr/bin/python2

from __future__ import print_function

import os
import sys
import subprocess
import select
import time

if len(sys.argv) != 3:
    sys.exit("ERROR: Please specify two arguments")

command1 = sys.argv[1]
command2 = sys.argv[2]

player1 = subprocess.Popen(command1, bufsize=1, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True, universal_newlines=True)
poll1 = select.poll()
poll1.register(player1.stdout, select.POLLIN | select.POLLERR | select.POLLHUP)
player2 = subprocess.Popen(command2, bufsize=1, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True, universal_newlines=True)
poll2 = select.poll()
poll2.register(player2.stdout, select.POLLIN | select.POLLERR | select.POLLHUP)

# TODO FIXME: both players should be able to play the last move (not just player 2)

tomove = 1

while True:
    # wait max 5 seconds for player 1
    # (r, _, _) = select.select([p1in], [], [], 5)
    # if len(r) == 0:
    #     if player1.poll() != None:
    #         print("Player 1 exited")
    #         break
    #     print("Player 1 did not produce a move!")
    #     sys.exit()
    r = poll1.poll(5000)
    if len(r) == 0:
        print("ERROR: Player 1 timed out!")
        player1.terminate()
        player2.terminate()
        sys.exit(1)
    (_, event) = r[0]
    if not event & select.POLLIN:
        break
    # get the first player's move, and forward it to the second player
    move1 = player1.stdout.readline()
    player2.stdin.write(move1)
    player2.stdin.flush()
    tomove = 2
    print("> "+move1, end='')
    # wait max 5 seconds for player 2
    r = poll2.poll(5000)
    if len(r) == 0:
        print("ERROR: Player 2 timed out!")
        player1.terminate()
        player2.terminate()
        sys.exit(1)
    (_, event) = r[0]
    if not event & select.POLLIN:
        break
    move2 = player2.stdout.readline()
    player1.stdin.write(move2)
    player1.stdin.flush()
    tomove = 1
    print("< "+move2, end='')

# give players a fraction of a second to realize their winning positions
time.sleep(0.2)

if tomove == 1 and player1.poll() != None and player2.poll() == None:
    player2.terminate()
    print("ERROR: Player 2 is awaiting a move by player 1, but player 1 exited. Did player 1 exit early or did 2 not realize a winning position?")
    sys.exit(1)
elif tomove == 2 and player2.poll() != None and player1.poll() == None:
    player1.terminate()
    print("ERROR: Player 1 is awaiting a move by player 2, but player 2 exited. Did player 2 exit early or did 1 not realize a winning position?")
    sys.exit(1)
elif tomove == 1:
    print("Both players exited, and it was player 1's turn. So player 2 won!")
elif tomove == 2:
    print("Both players exited, and it was player 2's turn. So player 1 won!")
