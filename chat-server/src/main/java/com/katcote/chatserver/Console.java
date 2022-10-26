package com.katcote.chatserver;

import java.util.Locale;
import java.util.Scanner;

public class Console{

    static class serverConsole extends Thread {

        public void run(){

            Scanner consoleIn = new Scanner(System.in);
            boolean exitFlag = false;

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println();

            while (!exitFlag){

                System.out.print("\n>>>");
                String bufferCommand = consoleIn.nextLine();

                switch (bufferCommand.toLowerCase(Locale.ROOT)){
                    case "stop" -> {
                        exitFlag = true;
                        break;
                    }
                }

            }

            MainHandler.stopServer();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);

        }
    }
}
