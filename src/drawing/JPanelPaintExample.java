package drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JPanelPaintExample extends JFrame {
    private final DrawPanel drawPanel;

    public JPanelPaintExample() {
        setTitle("drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);

        // DrawPanel을 하나만 추가
        drawPanel = new DrawPanel();
        add(drawPanel);

        setVisible(true);

        // 예시 선 추가
        drawPanel.addLine(10, 10, 100, 200);
        drawPanel.addLine(100, 200, 200, 100);
    }

    public void addLine(int x1, int y1, int x2, int y2){

        drawPanel.addLine(x1,y2,x2,y2);
    }


}


