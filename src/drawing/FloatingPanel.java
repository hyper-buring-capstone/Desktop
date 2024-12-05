package drawing;

import StateModel.StateModel;
import drawing.button.SaveBtn;

import javax.swing.*;
import java.awt.*;

import StateModel.StateModel;
import drawing.button.SaveBtn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FloatingPanel extends JPanel {
    JLabel pageNumLabel;
    Color curColor;
    int penWidth;
    StateModel state;
    boolean isPen;
    boolean isFolded; // 패널이 접혔는지 여부
    JButton toggleButton; // 패널 상태를 전환하는 버튼

    public FloatingPanel(StateModel state, PdfPanel pdfPanel, DrawPanel drawPanel) {
        this.state = state;
        penWidth = 15;
        curColor = Color.GRAY;
        isPen = true;
        isFolded = false;

        // 패널 레이아웃 및 크기 설정
        setLayout(null);
        setPreferredSize(new Dimension(340, 90)); // 패널 크기를 그림자를 포함하여 설정
        setOpaque(false);

        // 저장 버튼 추가
        SaveBtn saveBtn = new SaveBtn(drawPanel);
        saveBtn.setBounds(60, 20, 30, 30);
        add(saveBtn);

        // 페이지 라벨 추가
        pageNumLabel = new JLabel("" + (state.getCurPageNum() + 1) + "/" + state.getTotalPage());
        pageNumLabel.setBounds(135, 20, 50, 30);
        add(pageNumLabel);

        // 패널 상태를 전환하는 버튼 추가
        toggleButton = new JButton("Fold");
        toggleButton.setBounds(250, 20, 70, 30);
        add(toggleButton);

        // 버튼 액션 리스너 추가
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePanelState();
            }
        });

        setVisible(true);
    }

    // 패널 상태를 전환하는 메서드
    private void togglePanelState() {
        if (isFolded) {
            // 펼치는 동작
            setPreferredSize(new Dimension(340, 90));
            toggleButton.setText("Fold");
            setBackground(new Color(255, 255, 255, 255)); // 불투명
        } else {
            // 접는 동작
            setPreferredSize(new Dimension(340, 30)); // 접힌 상태 크기
            toggleButton.setText("Unfold");
            setBackground(new Color(255, 255, 255, 150)); // 반투명
        }
        isFolded = !isFolded;
        revalidate(); // 레이아웃 갱신
        repaint(); // 패널 다시 그리기
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
        int panelWidth = isFolded ? 300 : 300; // 접힌 상태에서도 그림자 유지
        int panelHeight = isFolded ? 30 : 50;

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
        g2.setColor(getBackground()); // 내용 배경색
        g2.fillRoundRect(x, y, panelWidth, panelHeight, 30, 30);

        // 현재 사용 중인 펜 표시
        if (!isFolded) { // 접힌 상태에서는 펜 표시 생략
            if (isPen) {
                g2.setColor(curColor);
                g2.setStroke(new BasicStroke(penWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 210, y + 25, x + 240, y + 25);
            } else {
                g2.setColor(Color.BLACK);
                g2.drawOval(x + 225 - penWidth / 2, y + 25 - penWidth / 2, penWidth, penWidth);
            }
        }

        g2.dispose();
    }
}
