# Socket Programming  

This repository contains multiple Java-based implementations of an HTTP server that forwards requests to a TCP server, handling client communication with different concurrency models.  

## Repository Structure  

- `TCPAsk/` - A simple TCP client that sends queries to a remote TCP server and retrieves responses.  
- `TCPAsk-Connections/` - An extended version of `TCPAsk` with additional handling for connections.  
- `HTTPAsk/` - A single-threaded HTTP server that processes `/ask` requests and forwards them to a TCP server.  
- `ConcHTTPAsk/` - A multi-threaded version of `HTTPAsk`, allowing concurrent request handling.  

## Overview  

The project provides an HTTP server that accepts GET requests to `/ask`, extracts parameters such as `hostname`, `port`, and `string`, and forwards them to a TCP server via `TCPAsk`. The response from the TCP server is then returned as an HTTP response.  

## How to Build and Run  

### 1. Compile the Code  
```sh
javac TCPAsk/*.java TCPAsk-Connections/*.java HTTPAsk/*.java ConcHTTPAsk/*.java

 Single-threaded (HTTPAsk)
java HTTPAsk.HTTPAsk <port>

Multi-threaded (ConcHTTPAsk)
java ConcHTTPAsk.ConcHTTPAsk <port>

Example using curl:
curl "http://localhost:1234/ask?hostname=example.com&port=80&string=Hello&timeout=2000&limit=5000&shutdown=true"
