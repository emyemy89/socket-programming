# ConcurrentHTTPAsk
  
- `ConcHTTPAsk.java` (multithreaded HTTP server)  
- `TCPClient.java` (handles TCP connections)  

## Description  
This is a multithreaded HTTP server that processes incoming HTTP requests and forwards them to a TCP server using `TCPClient.java`. Each request is handled in a separate thread using `MyRunnable`.  

## How to Run  
```sh
javac ConcHTTPAsk.java TCPClient.java  
java ConcHTTPAsk <port>

example:
curl "http://localhost:1234/ask?hostname=example.com&port=80&string=Hello&timeout=2000&limit=5000&shutdown=true"
