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
public class EraserPoint extends Point{

    PenType penType=PenType.ERASER;
    float width;
    
    public EraserPoint() {
    	width = 5;
    }
    
    public void movePoint(int x, int y) {
    	this.x = x;
    	this.y = y;
    }
    
    public int getX() {
    	return this.x;
    }
    
    public int getY() {
    	return this.y;
    }
    
}
