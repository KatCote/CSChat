<p align="center">
      <img src="https://i.ibb.co/GCq8mHx/CSChat3.png" width="726">
</p>

<p align="center">
   <img src="https://img.shields.io/badge/Patch-v1.4.1-success" alt="Patch">
   <img src="https://img.shields.io/badge/Java-openjdk--19-orange" alt="Java">
   <img src="https://img.shields.io/badge/License-MIT-red" alt="License">
</p>

## About

Client-Server Chat platform with encryption based on Netty.
Project in progress.

**Works on AES encryption.**

## Documentation

Project run on Java 19 and contains all the necessary libraries.

`Config.ini` - Contains a ip, port, client window size and default theme changes

`Settings.ini` - Contains a User settings for Client

`.idea` - Intellij IDEA run files and Libs

`src` - Resourses for both sides (Client-Server)

`chat-server` - server platform at configs port, ByteBuffer message Client-Server data exchange
Has console for CMD or IDE

`chat-client` - client platform with "/" commads to server. Has GUI with switchable theme

Client commands: 

`/changename` - Change client name to NotNull (or space at all) for all clients and on a server

`/exit` - Exit chat from both sides

`/motd` - Set MOTD for new clients or show MOTD

`/help` - Show page with all commands

Server commands:

`stop` - Stop the server with close all clients.

`motd` - Set MOTD for new clients

`clist` - List of all clients is this session (online) with IP

`help` - Show page with all commands

If You want to use custom CSS Stylesheet you may put it in resources folder and write *Title*.css like current theme to Settings.ini

## Developers

- [KatCote](https://github.com/KatCote)
- [Veslo](https://github.com/vadiek)

## License

The CSChat Project is distribution under MIT License.
