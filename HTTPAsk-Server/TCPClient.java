//package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    private static int BUFFERSIZE =4096;

    private boolean shutdown;
    private Integer timeout;
    private Integer limit;

    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown=shutdown;
        this.timeout=timeout;
        this.limit=limit;
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        try {
            Socket socket = new Socket(hostname, port);
            int currentSize;
            int canWrite;


            //buffers for data, one small/fixed and one dynamic
            byte[] fixedSmall = new byte[BUFFERSIZE];
            ByteArrayOutputStream fromServer = new ByteArrayOutputStream();

            // send bytes on socket
            socket.getOutputStream().write(toServerBytes);

            if(shutdown){
                socket.shutdownOutput();
            }

            if (timeout!=null){
                socket.setSoTimeout(timeout);
            }else{
                socket.setSoTimeout(0);
            }

            while (true) {
                try {
                    //receive bytes on socket

                    int fromServerLength = socket.getInputStream().read(fixedSmall);

                    currentSize = fromServer.size();


                    //server closes the connection
                    if (fromServerLength == -1) {
                        break;
                    }

                    //reached limit
                    if (limit != null && currentSize + fromServerLength >= limit) {
                        canWrite=limit - currentSize;
                        if(canWrite<0){
                            canWrite=0;
                        }
                        //write only what fits
                        fromServer.write(fixedSmall, 0, canWrite);
                        System.out.println("Limit reached");
                        break;
                    }

                    // else do this until done
                    fromServer.write(fixedSmall, 0, fromServerLength);
                }catch(SocketTimeoutException e){
                    System.out.println("Timeout reached");
                    break;
                }


            }

            byte[] result = fromServer.toByteArray();

            socket.close();

            return result;
        }catch(IOException e){
            e.printStackTrace();
            return new byte[0];
        }


    }
}
