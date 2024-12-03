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
		MsgType msgType=BtParser.getMsgType(msg); //여러 번 조건문 돌면 오버헤드 발생. 최적화했음.
		// msg의 타입은 무조건 btparser에서 알아오도록 변경(로직 최적화).

		switch (msgType){
			case HEADER_PEN -> {
				penLine=new PenLine(); //새 선 객체 생성함.
				noteFrame.callAddPenLine(penLine);
            }
			case HEADER_ERASER -> {
				eraserPoint = new EraserPoint(); // 새 지우개 포인트 생성.
				isEraser = true;
			}
			case HEADER_PANNING -> {
				String[] points=msg.split("&&")[1].split(","); //컨트롤 박스 포인트 데이터만 분리.
				String[] startPoint=points[0].split(" "); //공백으로 분리
				String[] endPoint=points[1].split(" ");

				int startX=Integer.parseInt(startPoint[0]);
				int startY=Integer.parseInt(startPoint[1]);
				int endX=Integer.parseInt(endPoint[0]);
				int endY=Integer.parseInt(endPoint[1].split("\r")[0]); //맨 끝에 널문자 제거.

				/*
				좌표의 가공이 필요한 경우 수행할 것.
				 */

				noteFrame.setControlBoxLoc(startX,startY,endX,endY); //컴퓨터 화면의 좌표가 들어가야 함.

			}
			case END -> {
				isEraser = false;
				noteFrame.disposeGraphics();
				noteFrame.tempEnd();
				noteFrame.repaint();
			}
			case POINT -> {
				if(!isEraser) {
					//컨트롤 박스 offset 보정.
					/**
					 * offset 보정을 해도 성능에 영향 없음을 확인함.
					 * 보정 전: 평균 190ms, 보정 후: 평균 170ms.
					 */
					long startTime = System.nanoTime(); // 성능 측정 시작
					penLine.addPoint(((BtParser.getX(msg)*state.getImageWidth())/10000)+noteFrame.getDrawPanel().getOffsetX(), ((BtParser.getY(msg)*state.getImageHeight())/10000)+noteFrame.getDrawPanel().getOffsetY());
					int size=penLine.getXList().size();

					if (size >= 2) {
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
					noteFrame.setEraserLoc(eraserPoint.getX(), eraserPoint.getY(), (int) eraserPoint.getWidth()*2);
				}
			}
		}


    }


}
