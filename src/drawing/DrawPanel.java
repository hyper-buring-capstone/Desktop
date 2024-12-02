package drawing;

import lombok.Getter;
import lombok.Setter;
import model.Note;
import model.PenLine;
import service.FileService;

import javax.swing.*;

import StateModel.StateModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.awt.image.BufferedImage;
import java.util.List;

@Getter
public class DrawPanel extends JPanel {
    private final BufferedImage canvas;
    private List<List<PenLine>> penLineLists = new ArrayList<>();
    private Graphics2D g2d; // Graphics2D를 멤버 변수로 추가
    @Getter
    private int pageIndex;
    @Setter
//    private int maxPageNum;
    //int width;
   // int height; //여기에 값 넣지 말 것. 외부 컴포넌트에서 getHeight()로 값 가져가서 크기 이상해짐.
    private final int scale = 5;
    Note note;
    private StateModel state;

    int offsetX=0, offsetY=0;
    int controlX, controlY; //컨트롤 박스의 크기.

    private Point startPoint; // 처음 클릭한 좌표
    private Point currentPoint; // 드래그 중의 현재 좌표


    public DrawPanel(StateModel state, Note note) {
    	this.state = state;
    	this.note=note;

        //노트 데이터로부터 폭과 높이 불러오기
        Image thumbnail=note.getThumbNail();
        int imgWidth=thumbnail.getWidth(null);
        int imgHeight=thumbnail.getHeight(null);


        // BufferedImage 생성 (패널의 크기와 동일한 크기)
        canvas = new BufferedImage(999*scale, 999*imgHeight/imgWidth*scale, BufferedImage.TYPE_INT_ARGB);
        //  setLayout(null);

        // setAlignmentX(Component.CENTER_ALIGNMENT);
        setBackground(new Color(0,0,0,0)); // alpah 값 0이면 투명화.
        // setBounds(0,0,300,800);
        pageIndex =0;
        penLineLists.add(new ArrayList<>());
        setPreferredSize(new Dimension(999, 999*imgHeight/imgWidth));
        setMaximumSize(new Dimension(999   , 999*imgHeight/imgWidth));
        setMinimumSize(new Dimension(999   , 999*imgHeight/imgWidth));
        /**
         * 크기 999로 한 이유
         * pdf패널보다 크기가 크거나 같으면(1000 이상) pdf가 완전히 가려져서 아예 안 나옴.
         * 약간이라도 틈에서 pdf가 보여야 만들어지는 듯 함.
         */


        // setBorder(new TitledBorder(new LineBorder(Color.CYAN, 3), "drawpanel"));

        //노트 데이터로부터 드로잉 정보 불러오기.
        penLineLists= FileService.loadPenLineLists(note);
        state.setLineString(FileService.getSpecificBlock(state.getNoteTitle(), state.getCurPageNum(), state.getImageWidth(), state.getImageHeight()));

        createGraphics();

        //클릭 연동 -> 컨트롤 패널 조작.
        addMouseMotionListener(mouseDragAdapter);
        addMouseListener(mouseDragAdapter);


        reCanvas();


    }

    //드래그해서 컨트롤 박스 크기 조정
    MouseAdapter mouseDragAdapter=new MouseAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            // 드래그 중 현재 좌표 저장
            controlX=e.getX()-offsetX;
            controlY=e.getY()-offsetY;
            getParent().repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            controlX=0;
            controlY=0;
            offsetX=e.getX();
            offsetY=e.getY();
            getParent().repaint();
        }
    };

    public void setPageIndex(int newPageIndex) {
        if(newPageIndex>=0 && newPageIndex<state.getTotalPage()) {
            while (newPageIndex >= penLineLists.size()) {
                // 부족한 인덱스를 채우기 위해 빈 ArrayList 추가
                penLineLists.add(new ArrayList<>());
            }
        	pageIndex = newPageIndex;
        	reCanvas();
        }
    }

    public void createGraphics() {
    	g2d = canvas.createGraphics(); // Graphics2D 객체 생성
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
    
    public void disposeGraphics() {
    	//g2d.dispose(); // 사용이 끝나면 Graphics2D 객체를 해제
    }

    public void addPenLine(PenLine penLine){
        penLineLists.get(pageIndex).add(penLine);
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
//    public void addLine(int x1, int y1, int x2, int y2, float width) {
//        Graphics2D g2d = canvas.createGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setStroke(new BasicStroke(width));
//
//        g2d.setColor(Color.RED);
//        g2d.drawLine(x1, y1, x2, y2);
//        g2d.dispose(); // 리소스 해제
//        repaint(); // 패널 다시 그리기
//    }

    //그릴 때 이거 사용함. addline 삭제해도 될듯.
    public void addPolyLine(int[] xList, int[] yList, int n, float width){
        g2d.setStroke(new BasicStroke(width*scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(40, 123, 144, 80));
        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.5f));
        g2d.drawPolyline(Arrays.stream(xList).map(x -> x * 5).toArray(),
        		Arrays.stream(yList).map(y -> y * 5).toArray(),
        		n);
        repaint();
    }


    public void reCanvas() {
        //Graphics2D g2d = canvas.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);  // 전체 지우기 위해 Clear 컴포지트 설정
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // 그리기 설정 초기화
        g2d.setComposite(AlphaComposite.SrcOver); // 다시 그릴 수 있도록 컴포지트 설정
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setColor(Color.BLACK);
        //Iterator<PenLine> iterator = penLineLists.get(pageIndex).iterator();
        g2d.setColor(new Color(40, 123, 144, 80));
        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // 투명도 합성
        Iterator<PenLine> iterator = penLineLists.get(pageIndex).iterator();
    	while (iterator.hasNext()) {
    	    PenLine penLine = iterator.next();
            g2d.setStroke(new BasicStroke(penLine.getWidth()*scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    	    g2d.drawPolyline(
    	    		penLine.getXList().stream().mapToInt(x -> x*5).toArray(),
            		penLine.getYList().stream().mapToInt(y -> y*5).toArray(),
            		penLine.getXList().size());
    	}

        //g2d.dispose(); // 리소스 해제
        repaint(); // 다시 그리기
        //
    }

    public void reCanvas2(){


        for(PenLine penLine: penLineLists.get(pageIndex)){
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
    	
    	Iterator<PenLine> iterator = penLineLists.get(pageIndex).iterator();
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
       // setLayout(null);
       // setBackground(new Color(0,0,0,0)); // alpha 값 0이면 투명화.
        g.drawImage(canvas, 0, 0, canvas.getWidth()/scale, canvas.getHeight()/scale, null); // BufferedImage에 그린 내용을 패널에 표시

        //컨트롤 패널

            g.drawRect(offsetX,offsetY,controlX,controlY);



    }
}
