package global;

import drawing.NoteFrame;
import home.HomeFrame;
import model.EraserPoint;
import model.PenLine;
import service.DrawService;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Date;

public class ServerRunable implements Runnable{

    //UUID for SPP
    final UUID uuid = new UUID("0000110100001000800000805F9B34FB", false);
    final String CONNECTION_URL_FOR_SPP = "btspp://localhost:"
            + uuid +";name=SPP Server";

    private StreamConnectionNotifier mStreamConnectionNotifier = null;
    private StreamConnection mStreamConnection = null;
    private int count = 0;

    NoteFrame noteFrame;
    public ServerRunable(NoteFrame noteFrame){
        this.noteFrame=noteFrame;
    }

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




        while(true){

            log("wait for client requests...");

            try {

                mStreamConnection = mStreamConnectionNotifier.acceptAndOpen();
            } catch (IOException e1) {

                log("Could not open connection: " + e1.getMessage() );
            }


            count++;
            log("현재 접속 중인 클라이언트 수: " + count);


            //접속해야 실행
            new Receiver(mStreamConnection, noteFrame).start();
        }

    }



    class Receiver extends Thread {

        private InputStream mInputStream = null;
        private OutputStream mOutputStream = null;
        private String mRemoteDeviceString = null;
        private StreamConnection mStreamConnection = null;
        NoteFrame noteFrame;


        Receiver(StreamConnection streamConnection, NoteFrame noteFrame){


            mStreamConnection = streamConnection;
            this.noteFrame = noteFrame;


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


            PenLine penLine = null;
            EraserPoint eraserPoint = null;
            DrawService drawService=new DrawService(penLine, eraserPoint, noteFrame, false);
            try {

                Reader mReader = new BufferedReader(new InputStreamReader
                        ( mInputStream, Charset.forName(StandardCharsets.UTF_8.name())));

                boolean isDisconnected = false;

                Sender("에코 서버에 접속하셨습니다.");
                Sender( "보내신 문자를 에코해드립니다.");



                while(true){
                    long startTime = System.nanoTime(); // 성능 측정 시작

                    //log("ready");


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
                    //log( mRemoteDeviceString + ": " + recvMessage );


                    /**
                     * 모바일에서 "10 20" 이런 식으로 보내면 (0,0) (10,20)을 잇는 직선 생성하는 테스트 코드임.
                     */
                    drawService.drawProcess(recvMessage, noteFrame);

                    //Sender(recvMessage);
                    long endTime = System.nanoTime(); // 성능 측정 완료
                    // System.out.println("최종 실행 시간: " + (endTime - startTime) + " ns"); // 성능 시간 출력

                }

            } catch (IOException e) {

                log("Receiver closed" + e.getMessage());
            }
        }


        void Sender(String msg){
            long startTime = System.nanoTime(); // 성능 측정 시작

            PrintWriter printWriter = new PrintWriter(new BufferedWriter
                    (new OutputStreamWriter(mOutputStream,
                            Charset.forName(StandardCharsets.UTF_8.name()))));

            printWriter.write(msg+"\n");
            printWriter.flush();

            log( "Me : " + msg );
            long endTime = System.nanoTime(); // 성능 측정 완료
            System.out.println("Sender 실행 시간: " + (endTime - startTime) + " ns"); // 성능 시간 출력

        }
    }


    private static void log(String msg) {
        long startTime = System.nanoTime(); // 성능 측정 시작


        System.out.println("["+(new Date()) + "] " + msg);
        long endTime = System.nanoTime(); // 성능 측정 완료
        System.out.println("로그 실행 시간: " + (endTime - startTime) + " ns"); // 성능 시간 출력

    }

}
