package drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class DrawPanel extends JPanel {
    private final BufferedImage canvas;

    public DrawPanel() {
        // BufferedImage 생성 (패널의 크기와 동일한 크기)
        canvas = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        setBackground(Color.WHITE);
    }

    // 새로운 선을 추가하는 메서드
    public void addLine(int x1, int y1, int x2, int y2) {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(Color.RED);
        g2d.drawLine(x1, y1, x2, y2);
        g2d.dispose(); // 리소스 해제
        repaint(); // 패널 다시 그리기
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, null); // BufferedImage에 그린 내용을 패널에 표시
    }
}
