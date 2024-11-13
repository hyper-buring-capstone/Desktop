package drawing;

import javax.swing.*;

import drawing.button.NextPageBtn;
import model.PenLine;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RootFrame extends JFrame {
    private final DrawPanel drawPanel;
    PdfPanel pdfPanel;

    public RootFrame() throws IOException {
        setTitle("drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JLayeredPane jLayeredPane=new JLayeredPane();
        jLayeredPane.setSize(new Dimension(1000, 800));
        jLayeredPane.setLayout(null);

        //PDF Panel 추가
        File file=new File("C:\\Users\\PC\\Desktop\\1주차_강의자료.pdf");
        PDDocument document = Loader.loadPDF(file); //파일 document에 연결
        pdfPanel=new PdfPanel(document); // 새 pdf 패널 객체 생성

        // DrawPanel을 하나만 추가
        drawPanel = new DrawPanel();

        // 상단 버튼 레이아웃 추가
        TopLayeredPane topLayeredPane=new TopLayeredPane(pdfPanel);


        jLayeredPane.add(pdfPanel, JLayeredPane.DEFAULT_LAYER); // pdf를 밑에 배치
        jLayeredPane.add(drawPanel, JLayeredPane.PALETTE_LAYER); // 드로잉을 그 위에 배치


        add(jLayeredPane, BorderLayout.CENTER);
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

