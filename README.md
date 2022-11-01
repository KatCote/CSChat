<p align="center">
      <img src="https://i.ibb.co/rZF2rhn/CSChat3.png" width="726">
</p>

<p align="center">
   <img src="https://img.shields.io/badge/Patch-v1.3.4-success" alt="Patch">
   <img src="https://img.shields.io/badge/Java-openjdk--19-orange" alt="Java">
   <img src="https://img.shields.io/badge/License-MIT-red" alt="License">
</p>

## About

Client-Server Chat platform with encryption based on Netty.
Project in progress.

**Works on AES encryption.**

## Documentation

Project run on Java 19 and contains all the necessary libraries.

`Config.ini` - Contains a ip, port, client window size and default theme changes.

`.idea` - Intellij IDEA run files and Libs.

`src` - Resourses for both sides (Client-Server).

`chat-server` - server platform at configs port, ByteBuffer message Client-Server data exchange.
Has console for CMD or IDE.

`chat-client` - client platform with "/" commads to server. Has GUI with Dark CSS Style (it is not CMD).

Client commands: 

`/changename` - Change client name to NotNull (or space at all) for all clients and on a server.

`/exit` - Exit chat from both sides.

`/motd` - Set MOTD for new clients

Server commands:

`stop` - Stop the server with close all clients.

## Developers

- [KatCote](https://github.com/KatCote)
- [Veslo](https://github.com/vadiek)

## License

The CSChat Project is distribution under MIT License.
