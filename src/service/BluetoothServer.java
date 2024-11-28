package service;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;
import java.util.Date;

public class BluetoothServer implements Runnable {
    private final UUID uuid = new UUID("0000110100001000800000805F9B34FB", false);
    private final String CONNECTION_URL_FOR_SPP = "btspp://localhost:" + uuid + ";name=SPP Server";
    private StreamConnectionNotifier mStreamConnectionNotifier;
    private boolean isRunning = true;
    private ServerListener serverListener;

    public BluetoothServer(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    @Override
    public void run() {
        try {
            mStreamConnectionNotifier = (StreamConnectionNotifier) Connector.open(CONNECTION_URL_FOR_SPP);
            IOService.log("Bluetooth server started.");
        } catch (IOException e) {
        	IOService.log("Could not start server: " + e.getMessage());
            return;
        }

        while (isRunning) {
            try {
            	IOService.log("Waiting for client connection...");
                StreamConnection connection = mStreamConnectionNotifier.acceptAndOpen();
                IOService.log("Client connected.");
                if (serverListener != null) {
                    serverListener.onClientConnected(connection);
                }
            } catch (IOException e) {
            	IOService.log("Error accepting connection: " + e.getMessage());
                break;
            }
        }

        stopServer();
    }

    public void stopServer() {
        isRunning = false;
        if (mStreamConnectionNotifier != null) {
            try {
                mStreamConnectionNotifier.close();
                IOService.log("Bluetooth server stopped.");
            } catch (IOException e) {
            	IOService.log("Error stopping server: " + e.getMessage());
            }
        }
    }

    public interface ServerListener {
        void onClientConnected(StreamConnection connection);
    }
}
