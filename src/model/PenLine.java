package model;

import global.PenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class PenLine {

    /**
     * 획 지우개 방식으로 설정.
     * 한 획을 저장하는 방식.
     * 한 획은 같은 펜으로 그린 것이므로 정보를 공통으로 저장.
     */

    PenType penType=PenType.PEN; //펜 타입
    Color penColor;
    float width; //펜 두께

    List<Integer> xList=new ArrayList<Integer>(); // x좌표
    List<Integer> yList=new ArrayList<Integer>(); // y좌표

    public void addPoint(int x, int y){
        xList.add(x);
        yList.add(y);
    }
}
