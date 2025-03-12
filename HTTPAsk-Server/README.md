# HTTPAsk Server  

## Files   
- `HTTPAsk.java`  
- `TCPClient.java`  

## Overview  
This is a simple HTTP server that processes `GET /ask` requests and acts as a bridge to the TCP client from before.  
- Listens for HTTP requests.  
- Extracts query parameters.  
- Uses `TCPClient` to forward the request to another server.  
- Returns the response as an HTTP response.  

## Usage  
Start the server:  
```sh
java HTTPAsk <port>

example:
curl "http://localhost:1234/ask?hostname=example.com&port=80&string=Hello&shutdown=true&timeout=5000&limit=1024"
