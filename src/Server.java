
import drawing.JPanelPaintExample;
import global.BtParser;
import global.MsgType;
import model.PenLine;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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


class ServerRunable implements Runnable{

    //UUID for SPP
    final UUID uuid = new UUID("0000110100001000800000805F9B34FB", false);
    final String CONNECTION_URL_FOR_SPP = "btspp://localhost:"
            + uuid +";name=SPP Server";

    private StreamConnectionNotifier mStreamConnectionNotifier = null;
    private StreamConnection mStreamConnection = null;
    private int count = 0;


    @Override
    public void run() {

        try {

            mStreamConnectionNotifier = (StreamConnectionNotifier) Connector
                    .open(CONNECTION_URL_FOR_SPP);

            log("Opened connection successful.");
        } catch (IOException e) {

            log("Could not open connection: " + e.getMessage());
            return;
        }


        log("Server is now running.");


        JPanelPaintExample jPanelPaintExample=new JPanelPaintExample();

//        jPanelPaintExample.addLine(130,130,200,230);
//        jPanelPaintExample.addLine(10,100,200,100);
//
//        jPanelPaintExample.addPolyLine(new int[]{10,50, 120,60},new int[]{10,70, 220,20},4);


        while(true){

            log("wait for client requests...");

            try {

                mStreamConnection = mStreamConnectionNotifier.acceptAndOpen();
            } catch (IOException e1) {

                log("Could not open connection: " + e1.getMessage() );
            }


            count++;
            log("현재 접속 중인 클라이언트 수: " + count);


            new Receiver(mStreamConnection, jPanelPaintExample).start();
        }

    }



    class Receiver extends Thread {

        private InputStream mInputStream = null;
        private OutputStream mOutputStream = null;
        private String mRemoteDeviceString = null;
        private StreamConnection mStreamConnection = null;
        JPanelPaintExample jPanelPaintExample;


        Receiver(StreamConnection streamConnection, JPanelPaintExample jPanelPaintExample){


            mStreamConnection = streamConnection;
            this.jPanelPaintExample=jPanelPaintExample;


            try {

                mInputStream = mStreamConnection.openInputStream();
                mOutputStream = mStreamConnection.openOutputStream();

                log("Open streams...");
            } catch (IOException e) {

                log("Couldn't open Stream: " + e.getMessage());

                Thread.currentThread().interrupt();
                return;
            }


            try {

                RemoteDevice remoteDevice
                        = RemoteDevice.getRemoteDevice(mStreamConnection);

                mRemoteDeviceString = remoteDevice.getBluetoothAddress();

                log("Remote device");
                log("address: "+ mRemoteDeviceString);

            } catch (IOException e1) {

                log("Found device, but couldn't connect to it: " + e1.getMessage());
                return;
            }

            log("Client is connected...");
        }


        @Override
        public void run() {
        //연결된 뒤의 동작

            int beforeX=0, beforeY=0;
            PenLine penLine = null;
            try {

                Reader mReader = new BufferedReader(new InputStreamReader
                        ( mInputStream, Charset.forName(StandardCharsets.UTF_8.name())));

                boolean isDisconnected = false;

                Sender("에코 서버에 접속하셨습니다.");
                Sender( "보내신 문자를 에코해드립니다.");



                while(true){

                    log("ready");


                    StringBuilder stringBuilder = new StringBuilder();
                    int c = 0;


                    while ( '\n' != (char)( c = mReader.read()) ) {

                        if ( c == -1){

                            log("Client has been disconnected");

                            count--;
                            log("현재 접속 중인 클라이언트 수: " + count);

                            isDisconnected = true;
                            Thread.currentThread().interrupt();

                            break;
                        }

                        stringBuilder.append((char) c);
                    }

                    if ( isDisconnected ) break;

                    String recvMessage = stringBuilder.toString(); //받은 문자
                    log( mRemoteDeviceString + ": " + recvMessage );


                    /**
                     * 모바일에서 "10 20" 이런 식으로 보내면 (0,0) (10,20)을 잇는 직선 생성하는 테스트 코드임.
                     */
                    if(BtParser.getMsgType(recvMessage).equals(MsgType.HEADER)){
                        penLine=new PenLine(); //새 선 객체 생성함.
                    }
                    else if(BtParser.getMsgType(recvMessage).equals(MsgType.END)){

                    }
                    else if(BtParser.getMsgType(recvMessage).equals(MsgType.POINT)){

                        penLine.addPoint(BtParser.getX(recvMessage), BtParser.getY(recvMessage));
                        int size=penLine.getXList().size();
                        jPanelPaintExample.addPolyLine(penLine.getXList().stream().mapToInt(Integer::intValue).toArray()
                                , penLine.getYList().stream().mapToInt(Integer::intValue).toArray()
                                , size);

                    }
//                    String[] parsedMsg=recvMessage.split("\r")[0].split(" "); //테스트용. 공백으로 분리
//
//                    int x=Integer.parseInt(parsedMsg[0]);
//                    int y=Integer.parseInt(parsedMsg[1]);
//
//                    jPanelPaintExample.addLine(beforeX,beforeY,x,y);
//                    beforeX=x;
//                    beforeY=y;

                    Sender(recvMessage);
                }

            } catch (IOException e) {

                log("Receiver closed" + e.getMessage());
            }
        }


        void Sender(String msg){

            PrintWriter printWriter = new PrintWriter(new BufferedWriter
                    (new OutputStreamWriter(mOutputStream,
                            Charset.forName(StandardCharsets.UTF_8.name()))));

            printWriter.write(msg+"\n");
            printWriter.flush();

            log( "Me : " + msg );
        }
    }


    private static void log(String msg) {

        System.out.println("["+(new Date()) + "] " + msg);
    }

}  