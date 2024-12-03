package control;

import StateModel.StateModel;
import model.Note;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;


/**
 * 컨트롤 정보들이 표시되는 곳.
 * 컨트롤 박스, 지우개 범위 등.
 */
public class ControlPanel extends JPanel {

    int boxStartX, boxStartY, boxEndX,  boxEndY; //컨트롤 박스 위치. 좌상단과 우하단 포인트.
    int eraseX, eraseY, diameter; // 지우개 위치와 크기. 지름임.
    boolean isEraser; //지우개 모드 온,오프. 항상 지우개 포인트가 떠 있으면 안 됨.

    //더블 버퍼링
    Image buffImg;
    Graphics buffG;

    public ControlPanel(StateModel state, Note note) {

        int imgHeight=note.getThumbNail().getHeight(null);
        int imgWidth=note.getThumbNail().getWidth(null);
        int baseSize=998;

        //크기 설정
        setPreferredSize(new Dimension(baseSize, baseSize*imgHeight/imgWidth));
        setMaximumSize(new Dimension(baseSize   , baseSize*imgHeight/imgWidth));
        setMinimumSize(new Dimension(baseSize   , baseSize*imgHeight/imgWidth));


        //색상 투명화
        setBackground(new Color(0,0,0,0)); // alpah 값 0이면 투명화.

        //플리커링?
      //  setDoubleBuffered(true);

      //  addMouseMotionListener(mouseMotionListener);

    }

    //컨트롤 박스의 위치 변경
    public void setControlBoxLoc(int startX, int startY, int endX, int endY){
        this.boxEndX =endX;
        this.boxEndY =endY;
        this.boxStartX =startX;
        this.boxStartY =startY;
        repaint();
    }

    //지우개 범위 위치 변경
    public void setEraserLoc(int x, int y, int diameter){
        this.eraseX=x;
        this.eraseY=y;
        this.diameter=diameter;

        isEraser=true;
        repaint();

    }


    //이상하게 paintcomponent는 호출이 안 되고 paint만 호출이 됨 ;;
    // 좀 찾아보니깐 lightweight component (단순 그리기) 정도는 repaint -> update() -> paint() 의 스택을 가짐.
    @Override
    public void paint(Graphics g) {
//        buffImg=createImage(getWidth(),getHeight());
//        buffG=buffImg.getGraphics();
//
//        //백지화
//        buffG.setColor(Color.white);
//        //buffG.fillRect(0,0,getWidth(), getHeight());


        super.paint(g);
        g.setColor(Color.BLACK);
        if(isEraser){ //지우개일 때만 그리기.
            g.drawOval(eraseX-diameter/2, eraseY-diameter/2, diameter, diameter);
            isEraser=false;
        }
        g.drawRect(boxStartX,boxStartY,boxEndX,boxEndY);

    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        g.setColor(Color.BLACK);
        if(isEraser){ //지우개일 때만 그리기.
            g.drawOval(eraseX-diameter/2, eraseY-diameter/2, diameter, diameter);
            isEraser=false;
        }
        g.drawRect(boxStartX,boxStartY,boxEndX,boxEndY);
    }


    //마우스로 테스트용

    MouseMotionListener mouseMotionListener=new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            eraseX=e.getX();
            eraseY=e.getY();

            isEraser=true;
            repaint();
        }
    };
}
