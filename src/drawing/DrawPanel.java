package drawing;

import lombok.Getter;
import lombok.Setter;
import model.Note;
import model.PenLine;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class DrawPanel extends JPanel {
    private final BufferedImage canvas;
    //private List<PenLine> penLineList=new ArrayList<>();
    private List<List<PenLine>> penLineLists = new ArrayList<>();
    private Graphics2D g2d; // Graphics2D를 멤버 변수로 추가
    @Getter
    private int pageNum;
    @Setter
    private int maxPageNum;
    int width;
    int height;
    private final int scale = 5;


    public DrawPanel(Note note) {
        //노트 데이터로부터 폭과 높이 불러오기
        Image thumbnail=note.getThumbNail();
        width=thumbnail.getWidth(null);
        height=thumbnail.getHeight(null);


        // BufferedImage 생성 (패널의 크기와 동일한 크기)
        canvas = new BufferedImage(300*scale, 700*scale, BufferedImage.TYPE_INT_ARGB);
        setLayout(null);
        setBorder(new TitledBorder(new LineBorder(Color.CYAN, 3), "drawpanel"));
       // setAlignmentX(Component.CENTER_ALIGNMENT);
        setBackground(new Color(0,0,0,0)); // alpah 값 0이면 투명화.
        setBounds(0,0,700,800);
        pageNum=0;
        penLineLists.add(new ArrayList<>());
        setSize(new Dimension(300, 700));
        setMaximumSize(new Dimension(300   , 700));

        //setPreferredSize(new Dimension(390, 870));

//        setOpaque(true);
    }

    public void setPageNum(int newPageNum) {
        if(newPageNum>=0 && newPageNum<maxPageNum) {
            while (newPageNum >= penLineLists.size()) {
                // 부족한 인덱스를 채우기 위해 빈 ArrayList 추가
                penLineLists.add(new ArrayList<>());
            }
        	pageNum = newPageNum;
        	reCanvas();
        }
    }

    public void createGraphics() {
    	g2d = canvas.createGraphics(); // Graphics2D 객체 생성
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
    
    public void disposeGraphics() {
    	g2d.dispose(); // 사용이 끝나면 Graphics2D 객체를 해제
    }

    public void addPenLine(PenLine penLine){
        penLineLists.get(pageNum).add(penLine);
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
        g2d.setStroke(new BasicStroke(width*scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(Color.BLACK);
        long startTime = System.nanoTime(); // 성능 측정 시작
        g2d.drawPolyline(Arrays.stream(xList).map(x -> x * 5).toArray(),
        		Arrays.stream(yList).map(y -> y * 5).toArray(),
        		n);
        long endTime = System.nanoTime(); // 성능 측정 완료
        //System.out.println("알짜시간 실행 시간: " + (endTime - startTime) + " ns"); // 성능 시간 출력
        repaint();
    }
    
    public void reCanvas() {

//    	Graphics2D g2d = canvas.createGraphics();
//        g2d.setColor( new Color(0,0,0,0));
//        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Graphics2D g2d = canvas.createGraphics();
//    	createGraphics();
        g2d.setComposite(AlphaComposite.Clear);  // 전체 지우기 위해 Clear 컴포지트 설정
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setStroke(new BasicStroke(width));
//        g2d.setColor(Color.BLACK);


        // 그리기 설정 초기화
        g2d.setComposite(AlphaComposite.SrcOver); // 다시 그릴 수 있도록 컴포지트 설정
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setColor(Color.BLACK);
        Iterator<PenLine> iterator = penLineLists.get(pageNum).iterator();
    	while (iterator.hasNext()) {
    	    PenLine penLine = iterator.next();
            g2d.setStroke(new BasicStroke(penLine.getWidth()*scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    	    g2d.drawPolyline(
    	    		penLine.getXList().stream().mapToInt(x -> x*5).toArray(),
            		penLine.getYList().stream().mapToInt(y -> y*5).toArray(),
            		penLine.getXList().size());
    	}
        
        repaint();
        g2d.dispose(); // 리소스 해제
        repaint(); // 다시 그리기
    }

    public void reCanvas2(){


        for(PenLine penLine: penLineLists.get(pageNum)){
            addPolyLine(penLine.getXList().stream().mapToInt(Integer::intValue).toArray(),
                    penLine.getYList().stream().mapToInt(Integer::intValue).toArray(),
                    penLine.getXList().size(),
                    penLine.getWidth());
        }
    }
    
    public void eraseLine(int x, int y, float width) {
    	float minX = x - width;
    	float maxX = x + width;
    	float minY = y - width;
    	float maxY = y + width;
    	
    	Iterator<PenLine> iterator = penLineLists.get(pageNum).iterator();
    	while (iterator.hasNext()) {
    	    PenLine penLine = iterator.next();
    	    if (penLine.isBoxContains(minX, maxX, minY, maxY)) { //이 조건 굳이 필요?
    	        if (penLine.isOverlapping(x, y, width)) {

    	            iterator.remove();
                    reCanvas();
                  //  repaint();

    	            //reCanvas(penLine.getWidth());
    	        }
    	    }
    	}

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setLayout(null);
        setBackground(new Color(0,0,0,0)); // alpha 값 0이면 투명화.
        g.drawImage(canvas, 0, 0, canvas.getWidth()/scale, canvas.getHeight()/scale, null); // BufferedImage에 그린 내용을 패널에 표시
    }
}
