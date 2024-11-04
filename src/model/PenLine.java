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
    //int pageNum; //몇 페이지에 기록되는지?

    List<Integer> xList=new ArrayList<Integer>(); // x좌표
    List<Integer> yList=new ArrayList<Integer>(); // y좌표
    
    int minX = 9999;
    int maxX = 0;
    int minY = 9999;
    int maxY = 0;

    public void addPoint(int x, int y){
        xList.add(x);
        yList.add(y);
        if(x < minX) {
        	minX = x;
        }
        if(x > maxX) {
        	maxX = x;
        }
        if(y < minY) {
        	minY = y;
        }
        if(y > maxY) {
        	maxY = y;
        }
    }
    
    public boolean isBoxContains(float boxMinX, float boxMaxX, float boxMinY, float boxMaxY) {
    	if(boxMinX < maxX && boxMaxX > minX) {
    		if(boxMinY < maxY && boxMaxY > minY) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean isOverlapping(int x, int y, float width) {
    	boolean result = false;
    	for(int i = 0; i < xList.size(); i++) {
    		double distance = Math.sqrt(Math.pow(Math.abs(xList.get(i) - x), 2) + Math.pow(Math.abs(yList.get(i) - y), 2)) - width - this.width;
    		if(distance < 0) {
    			result = true;
    			break;
    		}
    	}
    	return result;
    }

    public PenLine(){
        penColor=Color.BLUE;
        width=5;
    }
}
