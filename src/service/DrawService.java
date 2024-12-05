package service;


import java.awt.Color;

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
    	System.out.println(msg);
		MsgType msgType=BtParser.getMsgType(msg); //여러 번 조건문 돌면 오버헤드 발생. 최적화했음.
		// msg의 타입은 무조건 btparser에서 알아오도록 변경(로직 최적화).

		switch (msgType){
			case HEADER_PEN -> {
				penLine=new PenLine(); //새 선 객체 생성함.
				penLine.setPenColor(state.getPenColor());
				penLine.setPenWidth(state.getPenWidth());
				noteFrame.setPenColorAndWidth(state.getPenColor(), state. getPenWidth(), true);
				noteFrame.callAddPenLine(penLine);
            }
			case HEADER_ERASER -> {
				eraserPoint = new EraserPoint(); // 새 지우개 포인트 생성.
				noteFrame.setPenColorAndWidth(Color.BLACK, 30, false);
				isEraser = true;
			}
			case HEADER_PANNING -> {
				//System.out.println(msg);
				String[] points=msg.split("&&")[1].split(","); //컨트롤 박스 포인트 데이터만 분리.
				String[] startPoint=points[0].split(" "); //공백으로 분리
				String[] endPoint=points[1].split(" ");

				//넘어오는 값이 float자료형.
				int startX=(int)Float.parseFloat(startPoint[0]);
				int startY=(int)Float.parseFloat(startPoint[1]);
				int endX=(int)Float.parseFloat(endPoint[1]);
				int endY=(int)Float.parseFloat(endPoint[2].split("\r")[0]); //맨 끝에 널문자 제거.

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
					System.out.println("혹시 지금 점찍힘?");
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
								penLine.getPenWidth());
					} else {
						noteFrame.addPolyLine(
								penLine.getXList().stream().mapToInt(Integer::intValue).toArray(),
								penLine.getYList().stream().mapToInt(Integer::intValue).toArray(),
								size,
								penLine.getPenWidth());
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
			case HEADER_WIDTH -> {
				switch(msg.split("&&")[1].trim()) {
					case "ss" -> {
						state.setPenWidth(3);
					}
					case "s" -> {
						state.setPenWidth(5);
					}
					case "m" -> {
						state.setPenWidth(10);
					}
					case "l" -> {
						state.setPenWidth(20);
					}
					case "ll" -> {
						state.setPenWidth(30);
					}
				}
			}
			case HEADER_COLOR -> {
				System.out.println("컬러?");
				int transparency = Integer.parseInt(msg.split("&&")[1].substring(0,2), 16);
				int colorRed = Integer.parseInt(msg.split("&&")[1].substring(2, 4), 16);
				int colorGreen = Integer.parseInt(msg.split("&&")[1].substring(4, 6), 16);
				int colorBlue = Integer.parseInt(msg.split("&&")[1].substring(6, 8), 16);
				Color newColor = new Color(colorRed, colorGreen, colorBlue, transparency);
				state.setPenColor(newColor);
			}
		}


    }


}
