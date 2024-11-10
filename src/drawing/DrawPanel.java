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
    private Graphics2D g2d; // Graphics2D를 멤버 변수로 추가


    public DrawPanel() {
        // BufferedImage 생성 (패널의 크기와 동일한 크기)
        canvas = new BufferedImage(390, 870, BufferedImage.TYPE_INT_ARGB);
        setLayout(null);
        setBackground(new Color(0,0,0,0)); // alpah 값 0이면 투명화.
        setBounds(0,0,700,800);

//        setOpaque(true);
    }
    
    public void createGraphics() {
    	g2d = canvas.createGraphics(); // Graphics2D 객체 생성
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    public void disposeGraphics() {
    	g2d.dispose(); // 사용이 끝나면 Graphics2D 객체를 해제
    }

    public void addPenLine(PenLine penLine){
        penLineList.add(penLine);
    }


    public void addPoint(int x1, int y1){
        
    }
    
    public void tempEraserPoint(int x, int y, float width) {
    	Graphics2D g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.RED);
    	g2d.fillOval(x, y, Math.round(width), Math.round(width));
    	g2d.dispose(); // 리소스 해제
        repaint(); // 패널 다시 그리기
    }

    // 새로운 선을 추가하는 메서드
    public void addLine(int x1, int y1, int x2, int y2, float width) {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(width));
        
        g2d.setColor(Color.RED);
        g2d.drawLine(x1, y1, x2, y2);
        g2d.dispose(); // 리소스 해제
        repaint(); // 패널 다시 그리기
    }

    public void addPolyLine(int[] xList, int[] yList, int n, float width){
        g2d.setStroke(new BasicStroke(width));
        g2d.setColor(Color.BLUE);
        long startTime = System.nanoTime(); // 성능 측정 시작
        g2d.drawPolyline(xList,yList,n);
        long endTime = System.nanoTime(); // 성능 측정 완료
        //System.out.println("알짜시간 실행 시간: " + (endTime - startTime) + " ns"); // 성능 시간 출력
        repaint(100,100,1000,800);
    }
    
    public void reCanvas(float width) {
    	Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(width));
        g2d.setColor(Color.BLUE);
        Iterator<PenLine> iterator = penLineList.iterator();
    	while (iterator.hasNext()) {
    	    PenLine penLine = iterator.next();
    	    g2d.drawPolyline(
    	    		penLine.getXList().stream().mapToInt(Integer::intValue).toArray(),
            		penLine.getYList().stream().mapToInt(Integer::intValue).toArray(),
            		penLine.getXList().size());
    	}
        
        
        g2d.dispose(); // 리소스 해제
        repaint(); // 다시 그리기
    }
    
    public void eraseLine(int x, int y, float width) {
    	float minX = x - width;
    	float maxX = x + width;
    	float minY = y - width;
    	float maxY = y + width;
    	
    	Iterator<PenLine> iterator = penLineList.iterator();
    	while (iterator.hasNext()) {
    	    PenLine penLine = iterator.next();
    	    if (penLine.isBoxContains(minX, maxX, minY, maxY)) {
    	        if (penLine.isOverlapping(x, y, width)) {
    	            iterator.remove();
    	            reCanvas(penLine.getWidth());
    	        }
    	    }
    	}
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setLayout(null);
        setBackground(new Color(0,0,0,0)); // alpah 값 0이면 투명화.
        g.drawImage(canvas, 0, 0, null); // BufferedImage에 그린 내용을 패널에 표시
    }
}
