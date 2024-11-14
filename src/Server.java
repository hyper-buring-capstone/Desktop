

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import java.util.Date;


public class Server{

    public static void main(String[] args){



        log("Local Bluetooth device...\n");

        LocalDevice local = null;
        try {

            local = LocalDevice.getLocalDevice();
        } catch (BluetoothStateException e2) {

        }

        log( "address: " + local.getBluetoothAddress() );
        log( "name: " + local.getFriendlyName() );


        Runnable r = new ServerRunable();
        Thread thread = new Thread(r);
        thread.start();

    }


    private static void log(String msg) {

        System.out.println("["+(new Date()) + "] " + msg);
    }

}


