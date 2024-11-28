package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

import StateModel.StateModel;
import drawing.NoteFrame;
import home.HomeFrame;
import model.EraserPoint;
import model.PenLine;

public class Receiver extends Thread {
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;
    private StreamConnection mStreamConnection = null;
    private String mRemoteDeviceString = null;
    private HomeFrame homeFrame;
    private StateModel state;
    DrawService drawService;

    public Receiver(StreamConnection streamConnection, HomeFrame homeFrame) {
        mStreamConnection = streamConnection;
        this.homeFrame = homeFrame;
        this.state = homeFrame.getStateModel();

        try {
            mInputStream = mStreamConnection.openInputStream();
            mOutputStream = mStreamConnection.openOutputStream();
            
            IOService.log("Open streams...");
        } catch (IOException e) {
            IOService.log("Couldn't open Stream: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        
        try {

            RemoteDevice remoteDevice
                    = RemoteDevice.getRemoteDevice(mStreamConnection);

            mRemoteDeviceString = remoteDevice.getBluetoothAddress();

            IOService.log("Remote device");
            IOService.log("address: "+ mRemoteDeviceString);

        } catch (IOException e1) {

        	IOService.log("Found device, but couldn't connect to it: " + e1.getMessage());
            return;
        }

        IOService.log("Client is connected...");
    }
    
    @Override
    public void run() {
    	PenLine penLine = null;
    	EraserPoint eraserPoint = null;
        // 데이터를 읽는 작업을 별도의 스레드로 실행
        Thread readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (homeFrame) {
                        // NoteFrame이 없으면 대기 상태로 들어감
                        while (!Receiver.this.state.getNoteOpen()) {
                            try {
                            	System.out.println("나 노트프레임 줘!!!!!!!!!!!!!!");
                                homeFrame.wait();
                            	System.out.println("풀려났다 크하하하하하하하하하하하하하하!!!!!!!!!!!!!!");
                            } catch (InterruptedException e) {
                                IOService.log("Read thread interrupted: " + e.getMessage());
                                return;
                            }
                        }

                        // NoteFrame이 존재하면 작업을 진행
                        drawService = new DrawService(penLine, eraserPoint, Receiver.this.state.getNoteFrame(), false);
                    }

                    try {
                        Reader reader = new BufferedReader(new InputStreamReader(
                                mInputStream, StandardCharsets.UTF_8));
                        while (true) {
                            StringBuilder stringBuilder = new StringBuilder();
                            int c;

                            while ((c = reader.read()) != '\n') {
                                if (c == -1) {
                                    IOService.log("Client disconnected.");
                                    return;
                                }
                                stringBuilder.append((char) c);
                            }

                            // 메시지 처리 로직
                            String recvMessage = stringBuilder.toString(); // 받은 문자
                            drawService.drawProcess(recvMessage, Receiver.this.state.getNoteFrame());
                        }
                    } catch (IOException e) {
                        IOService.log("Error in Receiver: " + e.getMessage());
                    }
                }
            }
        });

        readThread.start();

        // NoteFrame 상태를 지속적으로 확인하고 필요 시 readThread를 깨움
        while (true) {
            synchronized (homeFrame) {
                if (Receiver.this.state.getNoteOpen()) {
                    homeFrame.notify(); // 대기 중인 readThread를 깨움
                }
            }

            try {
                Thread.sleep(100); // 불필요한 CPU 사용 방지
            } catch (InterruptedException e) {
                IOService.log("Main thread interrupted: " + e.getMessage());
                break;
            }
        }
    }

    
    public void disconnection() {
    	mStreamConnection = null;
    }

    void sender(String message) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(mOutputStream, Charset.forName(StandardCharsets.UTF_8.name()))))) {
            writer.write(message + "\n");
            writer.flush();
        }
    }
}