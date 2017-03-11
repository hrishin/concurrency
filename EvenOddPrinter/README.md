# Thread Synchronisation - Print numbers in sequence using EVEN, ODD thread 
# Problem: 
Print numbers in sequnce from 0-N and perform addition of numbers from 0-N/2. One thread prints Even numbers other one prints Odd numbers and last one add the number in current iteration. Everyt hread will synchronise the execution in even, odd and addition activity sequence.

e.g. input N = 3

[0][1][A:0][2][3][A:1]

# Solution: 

Use wait() & notifiy() mechnism to cordniate activities between threads.
Main thread is waiting for all thread to finish the execution.


