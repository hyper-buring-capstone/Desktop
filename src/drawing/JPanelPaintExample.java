package drawing;

import javax.swing.*;

import model.PenLine;

import java.awt.*;
import java.awt.image.BufferedImage;

public class JPanelPaintExample extends JFrame {
    private final DrawPanel drawPanel;

    public JPanelPaintExample() {
        setTitle("drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(390, 870);

        // DrawPanel을 하나만 추가
        drawPanel = new DrawPanel();
        add(drawPanel);

        setVisible(true);

    }

    public void addLine(int x1, int y1, int x2, int y2, float width){

        drawPanel.addLine(x1,y1,x2,y2,width);
    }

    public void addPolyLine(int[] xList, int[] yList, int n, float width){
        drawPanel.addPolyLine(xList, yList, n, width);
    }
    
    public void eraseLine(int x, int y, float width) {
    	drawPanel.eraseLine(x, y, width);
    }

    public void callAddPenLine(PenLine penLine) {
        drawPanel.addPenLine(penLine);
    }

}


