package drawing;
import javax.swing.*;
import java.awt.*;

public class SmoothShadowedPanel extends JPanel {
    private int shadowSize = 10; // 그림자의 크기
    private Color shadowColor = new Color(0, 0, 0, 50); // 그림자 색상 (반투명)

    public SmoothShadowedPanel() {
        setOpaque(false); // 투명 배경을 허용
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // 안티앨리어싱 활성화
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = shadowSize;
        int y = shadowSize;
        int width = getWidth() - shadowSize * 2;
        int height = getHeight() - shadowSize * 2;

        // 부드러운 그림자 그리기
        for (int i = shadowSize; i > 0; i--) {
            int alpha = (int) ((1.0 - (double) i / shadowSize) * shadowColor.getAlpha());
            Color fadingShadow = new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), alpha);
            g2d.setColor(fadingShadow);
            g2d.fillRoundRect(x - i, y - i, width + i * 2, height + i * 2, 30, 30);
        }

        // 패널 내용 그리기
        g2d.setColor(getBackground());
        g2d.fillRoundRect(x, y, width, height, 30, 30);

        g2d.dispose();
    }
}
