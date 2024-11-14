package drawing;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import model.Note;
import model.PenLine;

import java.awt.*;
import java.io.IOException;

public class NoteFrame extends JFrame {
    private final DrawPanel drawPanel;
    PdfPanel pdfPanel;

    public NoteFrame(Note note) throws IOException {
        setTitle("drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


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

        jLayeredPane.add(pdfPanel, JLayeredPane.DEFAULT_LAYER); // pdf를 밑에 배치
        jLayeredPane.add(drawPanel, JLayeredPane.PALETTE_LAYER); // 드로잉을 그 위에 배치

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

}

