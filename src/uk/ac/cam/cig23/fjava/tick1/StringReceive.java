package uk.ac.cam.cig23.fjava.tick1;

import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class StringReceive {

    public static void main(String args[]) {
        if(args.length != 2){
            System.err.println("This application requires two arguments: <machine> <port>");
            return;
        } else {
            try {
                Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                System.err.println("This application requires two arguments: <machine> <port>");
                return;
            }
        }

        Socket myReceiveSocket = null;

        try {
            String server = args[0];
            int port = Integer.parseInt(args[1]);
            myReceiveSocket = new Socket(server, port);

        } catch (java.io.IOException ex) {
            System.err.println("Cannot connect to "+args[0]+" on port "+args[1]);
            return;
        }

        while(true) {
            try {
                InputStream input = myReceiveSocket.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead = input.read(buffer);
                System.out.println(new String(buffer));
            } catch (java.io.IOException ex) {
                System.err.println("Cannot connect to " + args[0] + " on port " + args[1]);
                return;
            }
        }

    }
}
