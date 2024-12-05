package drawing;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class FloatingPanel extends JPanel {


    public FloatingPanel(){
        setPreferredSize(new Dimension(300,50));
        setMaximumSize(new Dimension(300,50));
        // 배경을 투명하게 설정
        setBackground(new Color(0, 0, 0, 0));

        setBorder(new LineBorder(Color.red));

        setLayout(new BorderLayout());
        setVisible(true);
        repaint();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        // 안티앨리어싱 설정
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 둥근 사각형 그리기
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // 안티앨리어싱 설정
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 둥근 사각형 그리기
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
    }

//    public void drawPenCircle(Graphics g) {
//        g.setColor(color);
//    }
}
