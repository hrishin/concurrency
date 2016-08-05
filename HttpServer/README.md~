# Multithreaded Web Server

# Problem
HTTP echo server that accepts connections in a main thread, hands connections to an available worker thread from a thread pool, parses and processes the POST request in the worker
thread and sends a 200 OK response with the same content-type header and payload from the request. In other words, echo back the content to the client.

Main server thread is something like this:
   - listen on a port specified on the command line
   - while (true) {
        - accept a new connection

        - pass the new connection off to a thread from the thread
          pool. If no threads are available, the accept thread should
          block until a worker thread is available.
     }


# Solution:

This solution utilize the core java concepts like Socket, Multithreading/concorrency using ExecutorService and custome Threadpool implementation, 
throttling requests using Semaphore, unit test cases using junit, object mocking using mockito.
