package drawing;

import StateModel.StateModel;
import drawing.button.SaveBtn;

import javax.swing.*;
import java.awt.*;

public class FloatingPanel extends JPanel {
    JLabel pageNumLabel;
    Color curColor;
    int penWidth;
    StateModel state;
    boolean isPen;

    public FloatingPanel(StateModel state, PdfPanel pdfPanel, DrawPanel drawPanel) {
        this.state = state;
        penWidth = 15;
        curColor = Color.GRAY;
        isPen = true;

        // 패널 레이아웃 및 크기 설정
        setLayout(null);
        setPreferredSize(new Dimension(340, 90)); // 패널 크기를 그림자를 포함하여 설정
        setOpaque(false);

        // 버튼 추가
        SaveBtn saveBtn = new SaveBtn(drawPanel);
        saveBtn.setBounds(60, 20, 30, 30);
        add(saveBtn);

        // 페이지 라벨 추가
        pageNumLabel = new JLabel("" + (state.getCurPageNum() + 1) + "/" + state.getTotalPage());
        pageNumLabel.setBounds(135, 20, 50, 30);
        add(pageNumLabel);

        setVisible(true);
    }

    public void setPageIndex(int index) {
        pageNumLabel.setText("" + (index + 1) + "/" + state.getTotalPage());
    }

    public void setColorAndWidth(Color color, int width, boolean isPen) {
        this.isPen = isPen;
        this.curColor = color;
        this.penWidth = width;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // 안티앨리어싱 활성화
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 그림자 설정
        int shadowSize = 10; // 그림자 크기
        Color shadowColor = new Color(0, 0, 0, 15);
        int panelWidth = 300; // 패널의 실제 내용 크기
        int panelHeight = 50;

        int x = (getWidth() - panelWidth) / 2;
        int y = (getHeight() - panelHeight) / 2;

        // 부드러운 그림자 그리기
        for (int i = shadowSize; i > 0; i--) {
            int alpha = (int) ((1.0 - (double) i / shadowSize) * shadowColor.getAlpha());
            Color fadingShadow = new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), alpha);
            g2.setColor(fadingShadow);
            g2.fillRoundRect(x - i, y - i, panelWidth + i * 2, panelHeight + i * 2, 30, 30);
        }

        // 패널 내용 그리기
        g2.setColor(Color.WHITE); // 내용 배경색
        g2.fillRoundRect(x, y, panelWidth, panelHeight, 30, 30);

        // 현재 사용 중인 펜 표시
        if (isPen) {
            g2.setColor(curColor);
            g2.setStroke(new BasicStroke(penWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x + 210, y + 25, x + 240, y + 25);
        } else {
            g2.setColor(Color.BLACK);
            g2.drawOval(x + 225 - penWidth / 2, y + 25 - penWidth / 2, penWidth, penWidth);
        }

        g2.dispose();
    }
}