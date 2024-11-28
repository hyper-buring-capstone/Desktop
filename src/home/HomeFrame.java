package home;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import StateModel.StateModel;
import drawing.NoteFrame;
import service.Receiver;
import service.ServerService;
import lombok.Getter;
import model.Note;
import service.BluetoothServer;
import service.FileService;

import javax.microedition.io.StreamConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 초기 화면을 담당하는 프레임
 * 노트 선택, 환경설정, 노트 리스트 뷰어 등의 기능.
 */
public class HomeFrame extends JFrame implements BluetoothServer.ServerListener {

    @Getter
    NoteListPanel noteListPanel;
    private BluetoothServer bluetoothServer;
    private Thread serverThread;
    NoteFrame noteFrame;
    Receiver receiver;
    private StateModel state;

    JPanel loadingPanel;

    public HomeFrame(StateModel state){
    	this.state = state;
    	
        //자체 설정
        setTitle("drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        FileService.initDirectory(); //디렉토리 구조 초기화.

        //프로그래스 바.
        JProgressBar jpb=new JProgressBar();
//        jpb.setStringPainted(true);
//        jpb.setString("0%");
//        add(jpb, BorderLayout.SOUTH);
//        jpb.setIndeterminate(true);

        noteListPanel=new NoteListPanel(state, jpb,this); //노트 리스트 패널
        HomeBtnPanel homeBtnPanel=new HomeBtnPanel(noteListPanel); //상단 버튼 패널

        //스크롤 기능
        JScrollPane jScrollPane=new JScrollPane(noteListPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16); //스크롤바 속도 조정.



        setVisible(true);

        add(homeBtnPanel, BorderLayout.NORTH);
        add(jScrollPane, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                stopBluetoothServer();
            }
        });
        
        // Bluetooth 서버 시작
        startBluetoothServer();
        
        setVisible(true);
        
    }
    
    private void startBluetoothServer() {
        bluetoothServer = new BluetoothServer(this);
        serverThread = new Thread(bluetoothServer);
        serverThread.start();
    }
    
    private void stopBluetoothServer() {
        if (bluetoothServer != null) {
            bluetoothServer.stopServer();
        }
        if (serverThread != null) {
            try {
                serverThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public StateModel getStateModel() {
    	return state;
    }
    
    @Override
    public void onClientConnected(StreamConnection connection) {
        // 클라이언트 연결 시 처리할 로직
        receiver = new Receiver(connection, this);
        receiver.start();
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, Exception {

        UIManager.setLookAndFeel(new FlatLightLaf());
        UIManager.put("FileView.iconColor", Color.red); // 파일 아이콘 색상 변경
        StateModel state = new StateModel();
        new Thread(() -> {
            try {
                service.ServerService.startHttpServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        HomeFrame homeFrame=new HomeFrame(state);
    }

}
