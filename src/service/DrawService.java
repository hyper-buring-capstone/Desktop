package service;


import drawing.RootFrame;
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
    RootFrame rootFrame;
    boolean isEraser = false;


    public  void drawProcess(String msg, RootFrame rootFrame){
        if(BtParser.getMsgType(msg).equals(MsgType.HEADER)){
        	if(msg.split(":")[1].split("\r")[0].equals("DRAWING")) {
                penLine=new PenLine(); //새 선 객체 생성함.
                rootFrame.callAddPenLine(penLine);
                rootFrame.createGraphics();
        	}
        	else if(msg.split(":")[1].split("\r")[0].equals("ERASER")) {
        		eraserPoint = new EraserPoint(); // 새 지우개 포인트 생성.
        		isEraser = true;
        	}
        }
        else if(BtParser.getMsgType(msg).equals(MsgType.END)){
        	isEraser = false;
        	rootFrame.disposeGraphics();
        }
        else if(BtParser.getMsgType(msg).equals(MsgType.POINT)){
        	if(!isEraser) {
                penLine.addPoint(BtParser.getX(msg), BtParser.getY(msg));
                int size=penLine.getXList().size();
//                jPanelPaintExample.addPolyLine(
//                		penLine.getXList().stream().mapToInt(Integer::intValue).toArray(),
//                		penLine.getYList().stream().mapToInt(Integer::intValue).toArray(),
//                		size,
//                		penLine.getWidth());
                long startTime = System.nanoTime(); // 성능 측정 시작
                if (size >= 2) {
//                	jPanelPaintExample.addPolyLine(
//                		penLine.getXList().subList(size - 2, size).stream().mapToInt(Integer::intValue).toArray(),
//                		penLine.getYList().subList(size - 2, size).stream().mapToInt(Integer::intValue).toArray(),
//		        		2,
//		        		penLine.getWidth());
                	rootFrame.addPolyLine(
                    		penLine.getX2List(),
                    		penLine.getY2List(),
    		        		2,
    		        		penLine.getWidth());
                } else {
                	rootFrame.addPolyLine(
		        		penLine.getXList().stream().mapToInt(Integer::intValue).toArray(),
		        		penLine.getYList().stream().mapToInt(Integer::intValue).toArray(),
		        		size,
		        		penLine.getWidth());
                }
                long endTime = System.nanoTime(); // 성능 측정 완료
              //  System.out.println("Method 실행 시간: " + (endTime - startTime) + " ns"); // 성능 시간 출력

        	}
        	else {
        		eraserPoint.movePoint(BtParser.getX(msg), BtParser.getY(msg));
        		rootFrame.eraseLine(eraserPoint.getX(), eraserPoint.getY(), eraserPoint.getWidth());
        	}
        }


    }


}
