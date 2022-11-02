package com.katcote.chatserver;

import java.util.Locale;
import java.util.Scanner;

public class Console{

    static class serverConsole extends Thread {

        private static void csCommand(String command){
            switch (command.toLowerCase().split(" ")[0]) {
                case "stop" -> {
                    MainHandler.stopServer();
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }

                case "motd" -> {
                    if (command.substring(4).isBlank()) {
                        System.out.println(ServerApplication.MOTD);
                        break;
                    }
                    if (command.substring(5).toLowerCase(Locale.ROOT).equals("default")){
                        System.out.println("DEFAULT MOTD");
                        ServerApplication.MOTD = ServerApplication.MOTD_DEF;
                        MainHandler.sysMessage("[SERVER_MSG]" + "Default MOTD is set\n");
                        break;
                    }
                    System.out.println("MOTD: " + ServerApplication.MOTD + " -> " + command.substring(5));
                    ServerApplication.MOTD = command.substring(5);
                    MainHandler.sysMessage("[SERVER_MSG]" + "New MOTD: " + ServerApplication.MOTD + "\n");
                }

                case "clist" -> {
                    if (MainHandler.cList().size() == 0){break;}
                    for (int i = 0; i < MainHandler.cList().size(); i++){
                        System.out.println(i + " | " + MainHandler.cList().get(i));
                    }
                }
            }
        }

        public void run(){

            Scanner consoleIn = new Scanner(System.in);
            boolean exitFlag = false;

            while (!exitFlag){
                System.out.print("\n>>>");
                String bufferCommand = consoleIn.nextLine();
                System.out.println();
                csCommand(bufferCommand);
            }

        }
    }
}
