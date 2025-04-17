@echo off
echo Compiling and running the notification client...
cd %~dp0
mvn compile exec:java -Dexec.mainClass="com.library.socket.NotificationTerminalClient" 