package drawing;

import model.PenLine;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.util.List;

class DrawPanel extends JPanel {
    private final BufferedImage canvas;
    private List<PenLine> penLineList=new ArrayList<>();


    public DrawPanel() {
        // BufferedImage 생성 (패널의 크기와 동일한 크기)
        canvas = new BufferedImage(390, 870, BufferedImage.TYPE_INT_ARGB);
        setBackground(Color.WHITE);
    }

    public void addPenLine(PenLine penLine){
        penLineList.add(penLine);
    }


    public void addPoint(int x1, int y1){

    }

    // 새로운 선을 추가하는 메서드
    public void addLine(int x1, int y1, int x2, int y2) {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.RED);
        g2d.drawLine(x1, y1, x2, y2);
        g2d.dispose(); // 리소스 해제
        repaint(); // 패널 다시 그리기
    }

    public void addPolyLine(int[] xList, int[] yList, int n){
        Graphics2D g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 선의 굵기 설정 (예: 5픽셀)
        g2d.setStroke(new BasicStroke(5));
        
        g2d.setColor(Color.BLUE);
        g2d.drawPolyline(xList,yList,n);
        g2d.dispose();
        repaint();

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, null); // BufferedImage에 그린 내용을 패널에 표시
    }
}
