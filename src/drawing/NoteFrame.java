package drawing;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


import model.EraserPoint;
import model.Note;
import model.PenLine;
import service.DrawService;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class NoteFrame extends JFrame implements Runnable{
    private final DrawPanel drawPanel;
    PdfPanel pdfPanel;

    public NoteFrame(Note note) throws IOException {
        setTitle("drawing");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //윈도우 닫기 설정
        addWindowListener(windowAdapter);


        //layeredPane 설정
        JLayeredPane jLayeredPane=new JLayeredPane();
//        jLayeredPane.setMaximumSize(new Dimension(1000, 8000));
//        jLayeredPane.setPreferredSize(new Dimension(1000, 8000));
       // jLayeredPane.setLayout(new FlowLayout());
        jLayeredPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLayeredPane.setBorder(new TitledBorder(new LineBorder(Color.red,3),"jlayredPane")); //디버깅용
        jLayeredPane.setLayout(new OverlayLayout(jLayeredPane));

        //PDF Panel 추가
        pdfPanel=new PdfPanel(note); // 새 pdf 패널 객체 생성

        // DrawPanel을 하나만 추가]
        // DrawPanel 페이지 사이즈 설정
        drawPanel = new DrawPanel(note);
        drawPanel.setMaxPageNum(pdfPanel.getImageListSize());


        // 상단 버튼 레이아웃 추가
        TopLayeredPane topLayeredPane=new TopLayeredPane(pdfPanel, drawPanel);
        jLayeredPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        jLayeredPane.add(drawPanel, JLayeredPane.PALETTE_LAYER);
        jLayeredPane.add(pdfPanel, JLayeredPane.DEFAULT_LAYER); // pdf를 밑에 배치
        // 드로잉을 그 위에 배치
        jLayeredPane.revalidate();

        //스크롤 페인
        JScrollPane jScrollPane=new JScrollPane(jLayeredPane);
        jScrollPane.setVerticalScrollBarPolicy(jScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setHorizontalScrollBarPolicy(jScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16); //스크롤바 속도 조정.
//        jScrollPane.setMaximumSize(new Dimension(1000,8000));
//        jScrollPane.setPreferredSize(new Dimension(1000,8000));


        add(jScrollPane, BorderLayout.CENTER);
        add(topLayeredPane, BorderLayout.NORTH);
        setVisible(true);



    }

    //윈도우 창 닫기 설정
    WindowAdapter windowAdapter=new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
            isRunning=false;
            try {
                mStreamConnectionNotifier.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        }
    };

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }

    public void createGraphics() {
    	drawPanel.createGraphics();
    }
    
    public void disposeGraphics() {
    	drawPanel.disposeGraphics();
    }

    public void addLine(int x1, int y1, int x2, int y2, float width){

        drawPanel.addLine(x1,y1,x2,y2,width);
    }

    public void addPolyLine(int[] xList, int[] yList, int n, float width){
    	long startTime = System.nanoTime(); // 성능 측정 시작
        drawPanel.addPolyLine(xList, yList, n, width);
        long endTime = System.nanoTime(); // 성능 측정 완료
        //System.out.println("real time 실행 시간: " + (endTime - startTime) + " ns"); // 성능 시간 출력

        repaint(); //트러불 8번 관련 해결
    }
    
    public void eraseLine(int x, int y, float width) {
    	drawPanel.eraseLine(x, y, width);
        repaint();
    }

    public void callAddPenLine(PenLine penLine) {
        drawPanel.addPenLine(penLine);
    }

    //UUID for SPP
    final UUID uuid = new UUID("0000110100001000800000805F9B34FB", false);
    final String CONNECTION_URL_FOR_SPP = "btspp://localhost:"
            + uuid +";name=SPP examples.Server";

    private StreamConnectionNotifier mStreamConnectionNotifier = null;
    private StreamConnection mStreamConnection = null;
    private int count = 0;

    private boolean isRunning=true;


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


        log("examples.Server is now running.");




        while(isRunning){

            log("wait for client requests...");

            try {

                mStreamConnection = mStreamConnectionNotifier.acceptAndOpen();
            } catch (IOException e1) {

                log("Could not open connection: " + e1.getMessage() );
            }


            count++;
            log("현재 접속 중인 클라이언트 수: " + count);


            //접속해야 실행
            new Receiver(mStreamConnection, this).start();
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



                while(isRunning){
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


