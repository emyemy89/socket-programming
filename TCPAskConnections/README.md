# TCPAsk with Add-ons  

## Files  
 
- `TCPClient.java`  
- `TCPAsk.java`  

## Overview  
Same TCP client as before, but now with extra features:  
- Can shut down output after sending data.  
- Supports setting a timeout.  
- Limits how much data is received.  

## Usage  
```sh
java TCPAsk <hostname> <port> [optional data] [--shutdown] [--timeout <ms>] [--limit <bytes>]

example:
java TCPAsk example.com 80 "GET / HTTP/1.1\nHost: example.com\n\n" --shutdown --timeout 5000 --limit 1024

