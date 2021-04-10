/*
 * Copyright 2020 Andrew Rice <acr31@cam.ac.uk>, Alastair Beresford <arb33@cam.ac.uk>, C.I. Griffiths
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.cam.cig23.fjava.tick1;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class StringChat {
  public static void main(String[] args) {
      String server = null;
      int port = 0;

      if(args.length != 2){
          System.err.println("This application requires two arguments: <machine> <port>");
          return;
      } else {
          try {
              server = args[0];
              port = Integer.parseInt(args[1]);
          } catch (NumberFormatException ex) {
              System.err.println("This application requires two arguments: <machine> <port>");
              return;
          }
      }


      //as the threads execute separately one could try and change the value of Socket and the other
      //would not know. This means they would be operating on different sockets which makes the app
      //worthless. Final stops them from changing it just for themselves.
      final Socket s;
      try {
        s = new Socket(server, port);
      } catch (java.io.IOException ex) {
          System.err.println("Cannot connect to "+args[0]+" on port "+args[1]);
          return;
      }
      Thread output =
        new Thread() {
          @Override
          public void run() {

              try {
                  while(true) {
                      InputStream input = s.getInputStream();
                      byte[] buffer = new byte[1024];
                      int bytesRead = input.read(buffer);
                      System.out.println(new String(buffer));
                  }
              } catch (java.io.IOException ex) {
                  System.err.println("Cannot connect to " + args[0] + " on port " + args[1]);
                  return;
              }

          }
        };
      output.setDaemon(true);
      output.start();
      BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

      while (true) {

      //
      // Hint: call "r.readLine()" to read a new line of input from the user.
      //      this call blocks until a user has written a complete line of text
      //      and presses the enter key.
          try {
              String str = r.readLine();
              byte[] b = str.getBytes();
              OutputStream input = s.getOutputStream();
              input.write(b);
          } catch (java.io.IOException ex) {
              System.err.println("An error has occured whilst reading");
          }
      }
  }
}
