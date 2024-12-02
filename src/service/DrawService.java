package service;


import StateModel.StateModel;
import drawing.NoteFrame;
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
    NoteFrame noteFrame;
    boolean isEraser = false;
    StateModel state;


    public  void drawProcess(String msg, NoteFrame noteFrame){
        if(BtParser.getMsgType(msg).equals(MsgType.HEADER)){
        	//noteFrame.createGraphics();
        	if(msg.split(":")[1].split("\r")[0].equals("DRAWING")) {
                penLine=new PenLine(); //새 선 객체 생성함.
                noteFrame.callAddPenLine(penLine);
        	}
        	else if(msg.split(":")[1].split("\r")[0].equals("ERASER")) {
        		eraserPoint = new EraserPoint(); // 새 지우개 포인트 생성.
        		isEraser = true;
        	}
        }
        else if(BtParser.getMsgType(msg).equals(MsgType.END)){
        	isEraser = false;
        	noteFrame.disposeGraphics();
        	noteFrame.tempEnd();
        }
        else if(BtParser.getMsgType(msg).equals(MsgType.POINT)){
        	if(!isEraser) {
				//컨트롤 박스 offset 보정.
				/**
				 * offset 보정을 해도 성능에 영향 없음을 확인함.
				 * 보정 전: 평균 190ms, 보정 후: 평균 170ms.
				 */
				long startTime = System.nanoTime(); // 성능 측정 시작
                penLine.addPoint(((BtParser.getX(msg)*state.getImageWidth())/10000)+noteFrame.getDrawPanel().getOffsetX(), ((BtParser.getY(msg)*state.getImageHeight())/10000)+noteFrame.getDrawPanel().getOffsetY());
                int size=penLine.getXList().size();
//                jPanelPaintExample.addPolyLine(
//                		penLine.getXList().stream().mapToInt(Integer::intValue).toArray(),
//                		penLine.getYList().stream().mapToInt(Integer::intValue).toArray(),
//                		size,
//                		penLine.getWidth());

                if (size >= 2) {
//                	jPanelPaintExample.addPolyLine(
//                		penLine.getXList().subList(size - 2, size).stream().mapToInt(Integer::intValue).toArray(),
//                		penLine.getYList().subList(size - 2, size).stream().mapToInt(Integer::intValue).toArray(),
//		        		2,
//		        		penLine.getWidth());
                	noteFrame.addPolyLine(
                    		penLine.getX2List(),
                    		penLine.getY2List(),
    		        		2,
    		        		penLine.getWidth());
                } else {
                	noteFrame.addPolyLine(
		        		penLine.getXList().stream().mapToInt(Integer::intValue).toArray(),
		        		penLine.getYList().stream().mapToInt(Integer::intValue).toArray(),
		        		size,
		        		penLine.getWidth());
                }
                long endTime = System.nanoTime(); // 성능 측정 완료
                //System.out.println((endTime - startTime)); // 성능 시간 출력

        	}
        	else {
        		eraserPoint.movePoint(((BtParser.getX(msg)*state.getImageWidth())/10000)+noteFrame.getDrawPanel().getOffsetX()
						, ((BtParser.getY(msg)*state.getImageHeight())/10000)+noteFrame.getDrawPanel().getOffsetY());
        		noteFrame.eraseLine(eraserPoint.getX(), eraserPoint.getY(), eraserPoint.getWidth());
        	}
        }


    }


}
