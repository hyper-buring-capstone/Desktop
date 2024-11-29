package drawing;

import service.BluetoothServer;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.swing.*;

import StateModel.StateModel;
import home.HomeFrame;
import home.LoadingFrame;
import lombok.Getter;
import model.EraserPoint;
import model.Note;
import model.PenLine;
import service.DrawService;
import service.ServerService;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class NoteFrame extends JFrame {
	@Getter
    DrawPanel drawPanel;
    PdfPanel pdfPanel;
    private StateModel state;
    HomeFrame homeFrame;

    public NoteFrame(StateModel state, Note note, HomeFrame homeFrame) throws IOException {
        this.state = state;
        setTitle("drawing");
        this.homeFrame=homeFrame;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // UI 초기화
        initUI(note);

        // 닫을 때 행동
        addWindowListener(windowAdapter);

    }

    private void initUI(Note note) throws IOException {
        // 기존 코드에 있던 UI 초기화 로직 그대로 유지
        // drawPanel 및 pdfPanel 초기화
        // LayeredPane, ScrollPane 설정
    	setTitle("drawing");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        //layeredPane 설정
        JLayeredPane jLayeredPane=new JLayeredPane();
        jLayeredPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLayeredPane.setLayout(new OverlayLayout(jLayeredPane));

        //PDF Panel 추가
        pdfPanel=new PdfPanel(state, note); // 새 pdf 패널 객체 생성

        // DrawPanel을 하나만 추가
        // DrawPanel 페이지 사이즈 설정
        drawPanel = new DrawPanel(note);
        drawPanel.setMaxPageNum(pdfPanel.getImageListSize());


        // 상단 버튼 레이아웃 추가
        NoteTopPanel noteTopPanel =new NoteTopPanel(state, pdfPanel, drawPanel);
        jLayeredPane.setAlignmentX(Component.CENTER_ALIGNMENT);


        jLayeredPane.add(pdfPanel, JLayeredPane.DEFAULT_LAYER); // pdf를 밑에 배치
        jLayeredPane.add(drawPanel, JLayeredPane.PALETTE_LAYER); // 드로잉을 그 위에 배치


        //스크롤 페인
        JScrollPane jScrollPane=new JScrollPane(jLayeredPane);
        jScrollPane.setVerticalScrollBarPolicy(jScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setHorizontalScrollBarPolicy(jScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16); //스크롤바 속도 조정.
//        jScrollPane.setMaximumSize(new Dimension(1000,8000));
//        jScrollPane.setPreferredSize(new Dimension(1000,8000));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                state.setNoteOpen(false);
                state.getReceiver().Sender("HEADER:NOTESTATE&&OFF");
                dispose();
                //homeFrame.setVisible(true);
            }
        });

        add(jScrollPane, BorderLayout.CENTER);
        add(noteTopPanel, BorderLayout.NORTH);

        setVisible(true);
        setTitle(note.getTitle());

        homeFrame.setVisible(false);
    }

    //윈도우 창 닫기 설정
    WindowAdapter windowAdapter=new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {


            homeFrame.setVisible(true);
            homeFrame.refreshNotes(); //오래 걸리는 작업. 대략 1초;


//            isRunning=false;
//            try {
//                mStreamConnectionNotifier.close();
//
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }


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
    	drawPanel.addPolyLine(xList, yList, n, width);
        repaint();
    }
    
    public void eraseLine(int x, int y, float width) {
    	drawPanel.eraseLine(x, y, width);
        repaint();
    }

    public void callAddPenLine(PenLine penLine) {
        drawPanel.addPenLine(penLine);
    }

}
