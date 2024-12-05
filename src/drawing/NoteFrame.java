package drawing;

import control.ControlPanel;
import service.BluetoothServer;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.imageio.ImageIO;
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
import service.FileService;
import service.ServerService;

import static global.Constants.APP_ICON_PATH;

import java.awt.*;
import java.awt.event.MouseWheelListener;
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

    Note note;

    ControlPanel controlPanel;

    NoteTopPanel noteTopPanel;

    public NoteFrame(StateModel state, Note note, HomeFrame homeFrame) throws IOException {
        this.state = state;
        setTitle("drawing");
        this.homeFrame=homeFrame;
        this.note=note;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // UI 초기화
        initUI(note);

        // 닫을 때 행동
        addWindowListener(windowAdapter);

    }

    public void setPageIndex(int pageIndex) {
        pdfPanel.setPageIndex(pageIndex);
        drawPanel.setPageIndex(pageIndex);
        noteTopPanel.setPageIndex(pageIndex);
        repaint();
    }

    private void initUI(Note note) throws IOException {
        // 기존 코드에 있던 UI 초기화 로직 그대로 유지
        // drawPanel 및 pdfPanel 초기화
        // LayeredPane, ScrollPane 설정
    	setTitle("drawing");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setExtendedState(MAXIMIZED_BOTH); //전체 화면
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        try{
            setIconImage(ImageIO.read(new File(APP_ICON_PATH)));
        }catch (IOException e){
            e.printStackTrace();
        }


        //layeredPane 설정
        JLayeredPane jLayeredPane=new JLayeredPane();
        jLayeredPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLayeredPane.setLayout(new OverlayLayout(jLayeredPane));

        //PDF Panel 추가
        pdfPanel=new PdfPanel(state, note); // 새 pdf 패널 객체 생성

        // DrawPanel을 하나만 추가
        // DrawPanel 페이지 사이즈 설정
        drawPanel = new DrawPanel(state, note);

        controlPanel=new ControlPanel(state, note);

        //statemodel 초기화
        state.setTotalPage(pdfPanel.getTotalPageNum());


        // 상단 버튼 레이아웃 추가
         noteTopPanel =new NoteTopPanel(state, pdfPanel, drawPanel);
        jLayeredPane.setAlignmentX(Component.CENTER_ALIGNMENT);


        jLayeredPane.add(pdfPanel, JLayeredPane.DEFAULT_LAYER); // pdf를 밑에 배치
        jLayeredPane.add(drawPanel, JLayeredPane.PALETTE_LAYER); // 드로잉을 그 위에 배치
        jLayeredPane.add(controlPanel, JLayeredPane.MODAL_LAYER); //같이 설치해도 되나? 테스트 필요.


        // 노트 페이지의 스크롤 페인
        JScrollPane jScrollPane=new JScrollPane(jLayeredPane);
        jScrollPane.setVerticalScrollBarPolicy(jScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setHorizontalScrollBarPolicy(jScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16); //스크롤바 속도 조정.
//        jScrollPane.setMaximumSize(new Dimension(1000,8000));
//        jScrollPane.setPreferredSize(new Dimension(1000,8000));



        // 노트 페이지 썸네일 리스트를 담은 스크롤 페인
        ThumbnailPanel thumbnailPanel=new ThumbnailPanel(state,note,this);
        JScrollPane thumbnailScrollPane=new JScrollPane(thumbnailPanel
                ,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        thumbnailScrollPane.getVerticalScrollBar().setUnitIncrement(16); //스크롤바 속도 조정.


        addWindowListener(windowAdapter); //창 닫을 때 행동.

        add(jScrollPane, BorderLayout.CENTER);
        add(noteTopPanel, BorderLayout.NORTH);
        add(thumbnailScrollPane, BorderLayout.WEST);

        //setVisible(true);
        setTitle(note.getTitle());

        homeFrame.setVisible(false);


        System.out.println(isDoubleBuffered());
    }

    //컨트롤 박스의 위치 설정
    public void setControlBoxLoc(int startX, int startY, int endX, int endY){
        controlPanel.setControlBoxLoc( startX,  startY,  endX,  endY);
        repaint();
    }

    //지우개 범위 위치 변경
    public void setEraserLoc(int x, int y, int diameter){
        controlPanel.setEraserLoc(x,y,diameter);
        //repaint();

    }
    
//    //마우스 휠 설정
//    MouseWheelListener mouseWheelListener = new MouseWheelListener() {
//        @Override
//        public void mouseWheelMoved(MouseWheelEvent e) {
//            wheelAccumulated[0] += e.getWheelRotation(); // 휠 회전량 누적
//            System.out.println("Accumulated: " + wheelAccumulated[0]);
//
//            // 임계값을 초과하면 페이지 변경
//            if (wheelAccumulated[0] >= threshold) {
//                currentPage[0]++;
//                wheelAccumulated[0] = 0; // 누적값 초기화
//                label.setText("Page: " + currentPage[0]);
//            } else if (wheelAccumulated[0] <= -threshold) {
//                currentPage[0] = Math.max(1, currentPage[0] - 1); // 페이지 최소값 1로 제한
//                wheelAccumulated[0] = 0; // 누적값 초기화
//                label.setText("Page: " + currentPage[0]);
//            }
//        }
//    };

    //윈도우 창 닫기 설정
    WindowAdapter windowAdapter=new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {

            //메모리 초기화
            pdfPanel.removeAllImages();
            removeAll();


            // 메타정보 변경(날짜) : 0..001초. 비용 낮음.
            long start=System.nanoTime();
            FileService.saveMeta(note);
            long end=System.nanoTime();
            System.out.println("메타변경:" +( end-start));


            homeFrame.setVisible(true);
            homeFrame.refreshNotes(); //오래 걸리는 작업. 대략 1초;

            state.setNoteOpen(false);
            if(state.getReceiver() != null) {
                state.getReceiver().Sender("HEADER:NOTESTATE&&OFF");
            }
            dispose();

//            isRunning=false;
//            try {
//                mStreamConnectionNotifier.close();
//
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }


        }
    };

    public void tempEnd() {
    	drawPanel.reCanvas();
    }



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

//    public void addLine(int x1, int y1, int x2, int y2, float width){
//        drawPanel.addLine(x1,y1,x2,y2,width);
//    }

    public void addPolyLine(int[] xList, int[] yList, int n, float width){
    	drawPanel.addPolyLine(xList, yList, n);
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
