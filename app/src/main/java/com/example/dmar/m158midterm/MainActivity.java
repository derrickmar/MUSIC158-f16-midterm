package com.example.dmar.m158midterm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    /* These two variables hold the IP address and port number.
    * You should change them to the appropriate address and port.
    */
    private String myIP = "192.168.1.173";
    private int myPort = 7400;

    // This is used to send messages
    private OSCPortOut oscPortOut;

    // This thread will contain all the code that pertains to OSC
    private Thread oscThread = new Thread() {
        @Override
        public void run() {
          /* The first part of the run() method initializes the OSCPortOut for sending messages.
           *
           * For more advanced apps, where you want to change the address during runtime, you will want
           * to have this section in a different thread, but since we won't be changing addresses here,
           * we only have to initialize the address once.
           */

            Log.d("MESSAGE", "running thread??");

            try {
                // Connect to some IP address and port
                oscPortOut = new OSCPortOut(InetAddress.getByName(myIP), myPort);
            } catch(UnknownHostException e) {
                // Error handling when your IP isn't found
                return;
            } catch(Exception e) {
                // Error handling for any other errors
                return;
            }


          /* The second part of the run() method loops infinitely and sends messages every 500
           * milliseconds.
           */
            while (true) {
                if (oscPortOut != null) {
                    // Creating the message
                    Object args[] = new Object[2];
                    args[0] = new Integer(3);
                    args[1] = "hello";

                  /* The version of JavaOSC from the Maven Repository is slightly different from the one
                   * from the download link on the main website at the time of writing this tutorial.
                   *
                   * The Maven Repository version (used here), takes a Collection, which is why we need
                   * Arrays.asList(thingsToSend).
                   *
                   * If you're using the downloadable version for some reason, you should switch the
                   * commented and uncommented lines for message below
                   */
                    OSCMessage message = new OSCMessage("/", Arrays.asList(args));

                    try {
                        // Send the messages
                        oscPortOut.send(message);

                        // Pause for half a second
                        Log.d("MESSAGE", "hello world");
                        sleep(1000);
                    } catch (Exception e) {
                        Log.d("ERROR?", "hello world");
                        // Error handling for some error
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start the thread that sends messages
        oscThread.start();
    }
}
