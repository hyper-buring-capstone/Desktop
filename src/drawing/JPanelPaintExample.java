package drawing;

import javax.swing.*;

import model.PenLine;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JPanelPaintExample extends JFrame {
    private final DrawPanel drawPanel;
    PdfPanel pdfPanel;

    public JPanelPaintExample() throws IOException {
        setTitle("drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JLayeredPane jLayeredPane=new JLayeredPane();
        jLayeredPane.setSize(new Dimension(1000, 800));
        jLayeredPane.setLayout(null);

        //PDF Panel 추가
        File file=new File("C:\\Users\\kimdh\\Downloads\\1인가구_대응_관련_현황보고서.pdf");
        PDDocument document = Loader.loadPDF(file); //파일 document에 연결
        pdfPanel=new PdfPanel(document); // 새 pdf 패널 객체 생성

        // DrawPanel을 하나만 추가
        drawPanel = new DrawPanel();

        // 상단 버튼 레이아웃 추가
        TopLayeredPane topLayeredPane=new TopLayeredPane(pdfPanel);


        jLayeredPane.add(pdfPanel, Integer.valueOf(1)); // pdf를 밑에 배치
        jLayeredPane.add(drawPanel, Integer.valueOf(2)); // 드로잉을 그 위에 배치


        add(jLayeredPane, BorderLayout.CENTER);
        add(topLayeredPane, BorderLayout.NORTH);
        setVisible(true);

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
    }
    
    public void eraseLine(int x, int y, float width) {
    	drawPanel.eraseLine(x, y, width);
    }

    public void callAddPenLine(PenLine penLine) {
        drawPanel.addPenLine(penLine);
    }

}


