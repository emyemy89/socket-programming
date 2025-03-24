import java.net.*;
import java.io.*;

public class ConcHTTPAsk {
    public static int BUFFERSIZE = 4096;

    public static void main( String[] args) throws IOException {

        int portNumber = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portNumber);



        //infinite loop
        while (true) {

            //when the client connects, ignore and send "hello"
            Socket clientSocket = serverSocket.accept();
            MyRunnable myRunnable=new MyRunnable(clientSocket);

            Thread clientTh=new Thread(myRunnable);
            clientTh.start();
        }
    }
}

class MyRunnable implements Runnable{
    private Socket clientSocket;
    public static int BUFFERSIZE = 4096;
    int fromClientLength;

    MyRunnable(Socket clientSocket){
        this.clientSocket=clientSocket;
    }


    String http200 =
            "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" + "\r\n" +
                    "Hello World!";
    String http400 =
            "HTTP/1.1 400 Bad Request\r\n" +
                    "Content-Type: text/plain\r\n" + "\r\n" +
                    "400 Bad Request";
    String http404 =
            "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: text/plain\r\n" + "\r\n" +
                    "404 Not Found";



    public void run() {
        try {
            byte[] fixedSmall = new byte[BUFFERSIZE];

            ByteArrayOutputStream fromClient = new ByteArrayOutputStream();

            fromClientLength = clientSocket.getInputStream().read(fixedSmall);

            String request;

            while (fromClientLength != -1) {
                // write input to 'fromClient' array
                fromClient.write(fixedSmall, 0, fromClientLength);
                request = fromClient.toString("UTF-8");
                if (request.contains("\r\n\r\n")) {
                    break;
                }
                fromClientLength = clientSocket.getInputStream().read(fixedSmall);
            }

            byte[] fromClientByte = fromClient.toByteArray();


            request = fromClient.toString("UTF-8");


            String[] lines = request.split("\r\n");


            // [0] has GET, [1] path, [2] http version
            String[] parts = lines[0].split(" ");


            if (!parts[0].equals("GET") || !parts[2].equals("HTTP/1.1")) {
                clientSocket.getOutputStream().write(http400.getBytes("UTF-8"));  // Return 400 Bad Request for invalid methods
                clientSocket.close();
                return;
            }

            if (!parts[1].startsWith("/ask")) {
                clientSocket.getOutputStream().write(http404.getBytes("UTF-8"));  // Return 404 for invalid paths
                clientSocket.close();
                return;
            }


            String[] querySplit = parts[1].split("\\?");


            if (querySplit.length < 2) {
                clientSocket.getOutputStream().write(http400.getBytes("UTF-8"));
                clientSocket.close();
                return;
            }

            String[] parameters = querySplit[1].split("&");


            Boolean shutdown = false;
            Integer limit = null;
            Integer timeout = null;
            String hostname = null;
            Integer port = null;
            String queryString = "";

            for (int i = 0; i < parameters.length; i++) {
                String[] values = parameters[i].split("=");

                //System.out.println("Parameter: " + values[0] + " = " + values[1]);//

                try {
                    if (values[0].equals("hostname")) {
                        hostname = values[1];
                    } else if (values[0].equals("limit")) {
                        limit = Integer.parseInt(values[1]);
                    } else if (values[0].equals("port")) {
                        port = Integer.parseInt(values[1]);
                    } else if (values[0].equals("timeout")) {
                        timeout = Integer.parseInt(values[1]);
                    } else if (values[0].equals("shutdown")) {
                        shutdown = Boolean.parseBoolean(values[1]);
                    } else if (values[0].equals("string")) {
                        queryString = values[1] + "\r\n";
                    }
                } catch (NumberFormatException e) {
                    clientSocket.getOutputStream().write(http400.getBytes("UTF-8"));  // If invalid number, return 400
                    clientSocket.close();
                    return;
                }
            }

            if (hostname == null || port == null) {
                clientSocket.getOutputStream().write(http400.getBytes("UTF-8"));
                clientSocket.close();
                return;
            }

            try {
                TCPClient tcpClient = new TCPClient(shutdown, timeout, limit);

                byte[] queryBytes = queryString.getBytes("UTF-8");

                //byte[] response=tcpClient.askServer(hostname, port,fromClientByte);
                byte[] response = tcpClient.askServer(hostname, port, queryBytes);

                String response1 = new String(response, "UTF-8");

                //System.out.println("Response from client: " + response1);//

                String httpResponse =
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: text/plain\r\n" +
                                "Content-Length: " + response1.length() + "\r\n" +
                                "\r\n" +
                                response1;

                //send the response
                clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            } catch (UnknownHostException e) {
                System.out.println("Error: Unknown host!");
                String errorMessage = "Error: Unknown host '" + hostname + "'\n";
                String httpResponse =
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: text/plain\r\n" +
                                "Content-Length: " + errorMessage.length() + "\r\n" +
                                "\r\n" +
                                errorMessage;

                clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                clientSocket.getOutputStream().flush();
            } catch (Exception e) {
                clientSocket.getOutputStream().write(http400.getBytes("UTF-8"));
            } finally {
                clientSocket.close();
            }
        }catch(IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
}

