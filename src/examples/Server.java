//package examples;
//
//import drawing.NoteFrame;
//import model.EraserPoint;
//import model.PenLine;
//import service.DrawService;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.*;
//import java.io.IOException;
//
//import javax.bluetooth.BluetoothStateException;
//import javax.bluetooth.LocalDevice;
//import javax.bluetooth.RemoteDevice;
//import javax.bluetooth.UUID;
//import javax.microedition.io.Connector;
//import javax.microedition.io.StreamConnection;
//import javax.microedition.io.StreamConnectionNotifier;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.io.Reader;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.Date;
//
//public class Server{
//
//    public static void main(String[] args){
//
//
//
//        log("Local Bluetooth device...\n");
//
//        LocalDevice local = null;
//        try {
//
//            local = LocalDevice.getLocalDevice();
//        } catch (BluetoothStateException e2) {
//
//        }
//
//        log( "address: " + local.getBluetoothAddress() );
//        log( "name: " + local.getFriendlyName() );
//
//
//        Runnable r = new ServerRunnable();
//        Thread thread = new Thread(r);
//        thread.start();
//
//    }
//
//
//    private static void log(String msg) {
//
//        System.out.println("["+(new Date()) + "] " + msg);
//    }
//
//}
//
//
//class ServerRunnable implements Runnable{
//
//    //UUID for SPP
//    final UUID uuid = new UUID("0000110100001000800000805F9B34FB", false);
//    final String CONNECTION_URL_FOR_SPP = "btspp://localhost:"
//            + uuid +";name=SPP Server";
//
//    private StreamConnectionNotifier mStreamConnectionNotifier = null;
//    private StreamConnection mStreamConnection = null;
//    private int count = 0;
//
//
//    @Override
//    public void run() {
//
//        try {
//
//            mStreamConnectionNotifier = (StreamConnectionNotifier) Connector
//                    .open(CONNECTION_URL_FOR_SPP);
//
//            log("Opened connection successful.");
//        } catch (IOException e) {
//
//            log("Could not open connection: " + e.getMessage());
//            return;
//        }
//
//
//        log("examples.Server is now running.");
//
//
//        NoteFrame NoteFrame = null;
////        try {
////            //NoteFrame = new NoteFrame();
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
//
////        jPanelPaintExample.addLine(130,130,200,230);
////        jPanelPaintExample.addLine(10,100,200,100);
////
////        jPanelPaintExample.addPolyLine(new int[]{10,50, 120,60},new int[]{10,70, 220,20},4);
//
//
//        while(true){
//
//            log("wait for client requests...");
//
//            try {
//
//                mStreamConnection = mStreamConnectionNotifier.acceptAndOpen();
//            } catch (IOException e1) {
//
//                log("Could not open connection: " + e1.getMessage() );
//            }
//
//
//            count++;
//            log("현재 접속 중인 클라이언트 수: " + count);
//
//
//            new Receiver(mStreamConnection, NoteFrame).start();
//        }
//
//    }
//
//
//
//    class Receiver extends Thread {
//
//        private InputStream mInputStream = null;
//        private OutputStream mOutputStream = null;
//        private String mRemoteDeviceString = null;
//        private StreamConnection mStreamConnection = null;
//        NoteFrame noteFrame;
//
//
//        Receiver(StreamConnection streamConnection, NoteFrame noteFrame){
//
//
//            mStreamConnection = streamConnection;
//            this.noteFrame = noteFrame;
//
//
//            try {
//
//                mInputStream = mStreamConnection.openInputStream();
//                mOutputStream = mStreamConnection.openOutputStream();
//
//                log("Open streams...");
//            } catch (IOException e) {
//
//                log("Couldn't open Stream: " + e.getMessage());
//
//                Thread.currentThread().interrupt();
//                return;
//            }
//
//
//            try {
//
//                RemoteDevice remoteDevice
//                        = RemoteDevice.getRemoteDevice(mStreamConnection);
//
//                mRemoteDeviceString = remoteDevice.getBluetoothAddress();
//
//                log("Remote device");
//                log("address: "+ mRemoteDeviceString);
//
//            } catch (IOException e1) {
//
//                log("Found device, but couldn't connect to it: " + e1.getMessage());
//                return;
//            }
//
//            log("Client is connected...");
//        }
//
//
//        @Override
//        public void run() {
//        //연결된 뒤의 동작
//
//            int beforeX=0, beforeY=0;
//            PenLine penLine = null;
//            EraserPoint eraserPoint = null;
//            DrawService drawService=new DrawService(penLine, eraserPoint, noteFrame, false);
//
//            // 임시 파일 전송 코드
//            String filePath = "C:\\drawing\\data\\hw5\\images\\0.jpg";
//
//            File file = new File(filePath);
//
//            if (!file.exists()) {
//                System.err.println("파일이 존재하지 않습니다: " + filePath);
//                return;
//            }
//
//            try (FileInputStream fileInputStream = new FileInputStream(file)) {
////                byte[] buffer = new byte[1024];
////                int bytesRead;
////                int byteSize=fileInputStream.available();
////                System.out.println("available:" + byteSize); //224308
////
////                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
////                	Sender(buffer);
////                }
////
////
////                System.out.println("두번째 시도");
////                byte[] buffer2 = new byte[fileInputStream.available()+1];
////
////
////                System.out.println("남은 바이트 수:" + fileInputStream.available()); //224308
////                System.out.println("초기 바이트 수" + byteSize); //224308
////
////                while ((bytesRead = fileInputStream.read(buffer2)) != -1) {
////                    Sender(buffer2);
////                }
////
////                System.out.println("available:" + fileInputStream.available()); //224308
////
////
////                System.out.println("PDF 파일 전송 완료2: " + filePath);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            // 임시 파일 전송 코드 끝
//            try (FileInputStream fileInputStream = new FileInputStream(file)) {
//
//
//
////                System.out.println("두번째 시도");
////                int available = fileInputStream.available();
////                byte[] buffer2 = new byte[available];
////
////
////                System.out.println("available:" + fileInputStream.available()); //224308
////                int bytesRead;
////                while ((bytesRead = fileInputStream.read(buffer2)) != -1) {
////                    Sender(buffer2);
////                }
////
////                System.out.println("available:" + available); //224308
////
////                System.out.println("PDF 파일 전송 완료2: " + filePath);
//
//                /**
//                 * 그냥 1024칸짜리 바이트 배열 보내기
//                 */
//
//                int BYTE_SIZE=10000;
//                byte[] byteArr=new byte[BYTE_SIZE];
//                for(int i=0; i<BYTE_SIZE; i++){
//                    byteArr[i]=(byte)(i%100);
//                }
//
//                Sender(byteArr);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//
//
//
//            //Sender("END");
//
//            try {
//
//                Reader mReader = new BufferedReader(new InputStreamReader
//                        ( mInputStream, Charset.forName(StandardCharsets.UTF_8.name())));
//
//                boolean isDisconnected = false;
//
//               // Sender("에코 서버에 접속하셨습니다.");
//               // Sender( "보내신 문자를 에코해드립니다.");
//
//
//
//                while(true){
//                	long startTime = System.nanoTime(); // 성능 측정 시작
//
//                    //log("ready");
//
//
//                    StringBuilder stringBuilder = new StringBuilder();
//                    int c = 0;
//
//
//                    while ( '\n' != (char)( c = mReader.read()) ) {
//
//                        if ( c == -1){
//
//                            log("Client has been disconnected");
//
//                            count--;
//                            log("현재 접속 중인 클라이언트 수: " + count);
//
//                            isDisconnected = true;
//                            Thread.currentThread().interrupt();
//
//                            break;
//                        }
//
//                        stringBuilder.append((char) c);
//                    }
//
//                    if ( isDisconnected ) break;
//
//                    String recvMessage = stringBuilder.toString(); //받은 문자
//                    //log( mRemoteDeviceString + ": " + recvMessage );
//
//
//                    /**
//                     * 모바일에서 "10 20" 이런 식으로 보내면 (0,0) (10,20)을 잇는 직선 생성하는 테스트 코드임.
//                     */
//                    drawService.drawProcess(recvMessage, noteFrame);
//
//                    //Sender(recvMessage);
//                    long endTime = System.nanoTime(); // 성능 측정 완료
//                   // System.out.println("최종 실행 시간: " + (endTime - startTime) + " ns"); // 성능 시간 출력
//
//                }
//
//            } catch (IOException e) {
//
//                log("Receiver closed" + e.getMessage());
//            }
//        }
//
//
//        void Sender(String msg){
//        	long startTime = System.nanoTime(); // 성능 측정 시작
//
//            PrintWriter printWriter = new PrintWriter(new BufferedWriter
//                    (new OutputStreamWriter(mOutputStream,
//                            Charset.forName(StandardCharsets.UTF_8.name()))));
//
//            printWriter.write(msg+"\n");
//            printWriter.flush();
//
//            log( "Me : " + msg );
//            long endTime = System.nanoTime(); // 성능 측정 완료
//            System.out.println("Sender 실행 시간: " + (endTime - startTime) + " ns"); // 성능 시간 출력
//
//        }
//
//        // byte 전송
//        void Sender(byte[] msg){
//        	try {
//        		mOutputStream.write(msg);
//                mOutputStream.flush(); // 버퍼 비우기
//                System.out.println(Arrays.toString(msg));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//
//    private static void log(String msg) {
//    	long startTime = System.nanoTime(); // 성능 측정 시작
//
//
//        System.out.println("["+(new Date()) + "] " + msg);
//        long endTime = System.nanoTime(); // 성능 측정 완료
//        System.out.println("로그 실행 시간: " + (endTime - startTime) + " ns"); // 성능 시간 출력
//
//    }
//
//}  