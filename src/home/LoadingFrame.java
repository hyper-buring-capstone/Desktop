package home;

import javax.swing.*;
import java.awt.*;

import static global.Constants.FONT_BOLD;
import static global.Constants.FONT_REGULAR;

public class LoadingFrame extends JFrame implements Runnable{

    JProgressBar jpb;
    JLabel loadingLabel;
    public LoadingFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // 기본 테두리 제거
        setSize(500, 300);
        setLocationRelativeTo(null); // 화면 중앙 배치

        // 배경을 투명하게 설정
        setBackground(new Color(0, 0, 0, 0));

        // 프레임의 내용 설정
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                // 안티앨리어싱 설정
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 둥근 사각형 그리기
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
            }
        };

        contentPanel.setOpaque(false); // 배경 투명화
        contentPanel.setLayout(new BorderLayout());

        // 내용 추가
         loadingLabel = new JLabel("노트를 불러오는 중이에요", SwingConstants.CENTER);

        loadingLabel.setFont(FONT_REGULAR.deriveFont(Font.ITALIC,20.0f));
        contentPanel.add(loadingLabel, BorderLayout.CENTER);

        add(contentPanel);

        setVisible(true);

    }

    public void setLoadingText(String s){
        loadingLabel.setText(s);
        repaint();
        revalidate();
    }

    @Override
    public void run() {
        this.setVisible(true);
        add(new Label("dd"));

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
