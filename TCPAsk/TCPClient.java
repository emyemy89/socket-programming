package tcpclient;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.net.*;

public class TCPClient {

    private static int BUFFERSIZE =4096;

    public TCPClient() {

    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes)
            throws IOException {
        // create socket
            try{
                Socket socket=new Socket(hostname, port);
              //buffers for data, one small/fixed and one dynamic
            byte[] fixedSmall=new byte[BUFFERSIZE];
            ByteArrayOutputStream fromServer=new ByteArrayOutputStream();
              // send bytes on socket
            socket.getOutputStream().write(toServerBytes);
            while(true){
                //receive bytes on socket
                int fromServerLength=socket.getInputStream().read(fixedSmall);
                if(fromServerLength==-1){
                    break;
                } else {
                    fromServer.write(fixedSmall,0,fromServerLength);
                }
            }
            byte[] result= fromServer.toByteArray();
            socket.close();
            return result;
            } catch(IOException e){
                e.printStackTrace();
                return new byte[0];
            }
    }

    public byte[] askServer(String hostname, int port) throws IOException {
            try{Socket socket=new Socket(hostname, port);
            byte[] fixedSmall=new byte[BUFFERSIZE];
            ByteArrayOutputStream fromServer=new ByteArrayOutputStream();

                while(true){
                    int fromServerLength=socket.getInputStream().read(fixedSmall);


                    if(fromServerLength==-1){
                        break;
                    } else {
                        fromServer.write(fixedSmall,0,fromServerLength);
                    }

                }
            byte[] result= fromServer.toByteArray();
            socket.close();
            return result;
            }catch(IOException e){
                e.printStackTrace();
                return new byte[0];
            }
    }

}
