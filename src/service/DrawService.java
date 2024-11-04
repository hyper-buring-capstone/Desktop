package service;


import drawing.DrawFrame;
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
    DrawFrame drawFrame;

    int oldX=0;
    int oldY=0;
    int newX=0;
    int newY=0;

    public DrawService(PenLine penLine, DrawFrame drawFrame){
        this.penLine=penLine;
        this.drawFrame=drawFrame;
    }


    public  void drawProcess(String msg, DrawFrame drawFrame){
        if(BtParser.getMsgType(msg).equals(MsgType.HEADER)){
            penLine=new PenLine(); //새 선 객체 생성함.
            oldX=0;
            oldY=0;
        }
        else if(BtParser.getMsgType(msg).equals(MsgType.END)){
//        	int size=penLine.getXList().size();
//        	jPanelPaintExample.addPolyLine(
//            		penLine.getXList().stream().mapToInt(Integer::intValue).toArray(),
//            		penLine.getYList().stream().mapToInt(Integer::intValue).toArray(),
//            		size);
        }
        else if(BtParser.getMsgType(msg).equals(MsgType.POINT)){



            newX =BtParser.getX(msg);
            newY =BtParser.getY(msg);

            if(oldX==0&&oldY==0){
                oldX=newX;
                oldY=newY;
            }

            penLine.addPoint(newX, newY);

            drawFrame.addPolyLine(new int[]{oldX, newX}, new int[]{oldY, newY}, 2);

            oldX=newX;
            oldY=newY;

//            penLine.addPoint(BtParser.getX(msg), BtParser.getY(msg));
//
//            int size=penLine.getXList().size();
//
//            if(size==1){ //시작포인트일 경우 점이 하나이므로 선을 그릴 수 없음. 그냥 두개로 만들어버림.
//                penLine.addPoint(BtParser.getX(msg), BtParser.getY(msg));
//                size++; //2로 만드는 것.
//            }
//
//
//            // 인접한 두 노드의 정보만 전송. 매 선을 처음부터 그릴 필요 없다.
//            int[] xArr=new int[2];
//            int[] yArr=new int[2];
//
//            List<Integer> xList=penLine.getXList();
//            List<Integer> yList=penLine.getYList();
//
//            xArr[0]=xList.get(size-2);
//            xArr[1]=xList.get(size-1);
//
//            yArr[0]=yList.get(size-2);
//            yArr[1]=yList.get(size-1);
//
//            drawFrame.addPolyLine(xArr, yArr ,2);
        }


    }


}
