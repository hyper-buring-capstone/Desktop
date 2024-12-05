package home;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static global.Constants.*;

public class LoadingFrame extends JFrame implements Runnable{

    JProgressBar jpb;
    JLabel loadingLabel;
    public LoadingFrame(){
        //progress bar
        jpb=new JProgressBar();
        jpb.setForeground(COLOR_ORANGE);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // 기본 테두리 제거
        setSize(400, 250);
        setLocationRelativeTo(null); // 화면 중앙 배치

        try {
            setIconImage(ImageIO.read(new File(APP_ICON_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // 라벨 추가
        loadingLabel = new JLabel("노트를 불러오는 중이에요", SwingConstants.CENTER);
        loadingLabel.setFont(FONT_REGULAR.deriveFont(Font.ITALIC, 20.0f));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 진행 막대 추가
        jpb.setAlignmentX(Component.CENTER_ALIGNMENT);
        jpb.setMaximum(100);
        jpb.setMaximumSize(new Dimension(300, 10));

        // 내용 패널에 컴포넌트 추가
        contentPanel.add(Box.createVerticalGlue()); // 상단 여백
        contentPanel.add(loadingLabel);
        contentPanel.add(Box.createVerticalStrut(10)); // 라벨과 진행 막대 사이 여백
        contentPanel.add(jpb);
        contentPanel.add(Box.createVerticalGlue()); // 하단 여백

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

    public void setLoadingValue(int i, int size) {
        jpb.setValue(i*100/size);
        jpb.repaint();
    }
}
