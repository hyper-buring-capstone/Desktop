package service;


import drawing.JPanelPaintExample;
import global.BtParser;
import global.MsgType;
import lombok.AllArgsConstructor;
import model.PenLine;

/**
 * 모바일로부터 받은 점 데이터를 실질적으로 처리하는 곳.
 */
@AllArgsConstructor
public class DrawService {
    PenLine penLine;
    JPanelPaintExample jPanelPaintExample;


    public  void drawProcess(String msg, JPanelPaintExample jPanelPaintExample){
        if(BtParser.getMsgType(msg).equals(MsgType.HEADER)){
            penLine=new PenLine(); //새 선 객체 생성함.
        }
        else if(BtParser.getMsgType(msg).equals(MsgType.END)){

        }
        else if(BtParser.getMsgType(msg).equals(MsgType.POINT)){

            penLine.addPoint(BtParser.getX(msg), BtParser.getY(msg));
            int size=penLine.getXList().size();
            jPanelPaintExample.addPolyLine(penLine.getXList().stream().mapToInt(Integer::intValue).toArray()
                    , penLine.getYList().stream().mapToInt(Integer::intValue).toArray()
                    , size);

        }

    }


}
