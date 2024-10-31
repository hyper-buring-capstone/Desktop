package service;


import drawing.JPanelPaintExample;
import global.BtParser;
import global.MsgType;
import lombok.AllArgsConstructor;
import model.EraserPoint;
import model.PenLine;

/**
 * 모바일로부터 받은 점 데이터를 실질적으로 처리하는 곳.
 */
@AllArgsConstructor
public class DrawService {
    PenLine penLine;
    EraserPoint eraserPoint;
    JPanelPaintExample jPanelPaintExample;
    boolean isEraser = false;


    public  void drawProcess(String msg, JPanelPaintExample jPanelPaintExample){
        if(BtParser.getMsgType(msg).equals(MsgType.HEADER)){
        	if(msg.split(":")[1].split("\r")[0].equals("DRAWING")) {
                penLine=new PenLine(); //새 선 객체 생성함.
                jPanelPaintExample.callAddPenLine(penLine);
        	}
        	else if(msg.split(":")[1].split("\r")[0].equals("ERASER")) {
        		eraserPoint = new EraserPoint(); // 새 지우개 포인트 생성.
        		isEraser = true;
        	}
        }
        else if(BtParser.getMsgType(msg).equals(MsgType.END)){
        	isEraser = false;
        }
        else if(BtParser.getMsgType(msg).equals(MsgType.POINT)){
        	if(!isEraser) {
                penLine.addPoint(BtParser.getX(msg), BtParser.getY(msg));
                int size=penLine.getXList().size();
                jPanelPaintExample.addPolyLine(
                		penLine.getXList().stream().mapToInt(Integer::intValue).toArray(),
                		penLine.getYList().stream().mapToInt(Integer::intValue).toArray(),
                		size,
                		penLine.getWidth());	
        	}
        	else {
        		eraserPoint.movePoint(BtParser.getX(msg), BtParser.getY(msg));
        		jPanelPaintExample.eraseLine(eraserPoint.getX(), eraserPoint.getY(), eraserPoint.getWidth());
        	}
        }


    }


}
