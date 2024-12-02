package control;

import StateModel.StateModel;
import model.Note;

import javax.swing.*;
import java.awt.*;


/**
 * 컨트롤 정보들이 표시되는 곳.
 * 컨트롤 박스, 지우개 범위 등.
 */
public class ControlPanel extends JPanel {

    int boxStartX, boxStartY, boxEndX,  boxEndY; //컨트롤 박스 위치. 좌상단과 우하단 포인트.
    int eraseX, eraseY, diameter; // 지우개 위치와 크기. 지름임.
    boolean isEraser; //지우개 모드 온,오프. 항상 지우개 포인트가 떠 있으면 안 됨.

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
        setDoubleBuffered(true);
        repaint();

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
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(isEraser){ //지우개일 때만 그리기.
            g.drawOval(eraseX-diameter/2, eraseY-diameter/2, diameter, diameter);
            isEraser=false;
        }
        g.drawRect(boxStartX,boxStartY,boxEndX,boxEndY);
    }

//    @Override
//    public void paintComponents(Graphics g) {
//        super.paintComponents(g);
//        if(isEraser){ //지우개일 때만 그리기.
//            g.drawOval(eraseX, eraseY, diameter, diameter);
//        }
//        g.drawRect(boxStartX,boxStartY,boxEndX,boxEndY);
//    }
}
