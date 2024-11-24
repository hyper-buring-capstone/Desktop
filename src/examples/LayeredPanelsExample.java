package examples;

import javax.swing.*;
import java.awt.*;

public class LayeredPanelsExample extends JFrame {
    public LayeredPanelsExample() {
        // 기본 JFrame 설정
        setTitle("Layered JPanel Example");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // JLayeredPane 생성
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(400, 300));

        // 첫 번째 JPanel (배경 패널) 생성
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(Color.CYAN);
        backgroundPanel.setBounds(0, 0, 400, 300);

        // 두 번째 JPanel (위에 올려질 패널) 생성
        JPanel overlayPanel = new JPanel();
        overlayPanel.setBackground(new Color(255, 0, 0, 100)); // 투명한 빨간색
        overlayPanel.setBounds(50, 50, 200, 150);  // 위치와 크기 설정
        overlayPanel.setOpaque(true);

        // LayeredPane에 패널 추가 (층 순서 설정)
        layeredPane.add(backgroundPanel, Integer.valueOf(1)); // 아래 레이어
        layeredPane.add(overlayPanel, Integer.valueOf(2));    // 위 레이어

        add(layeredPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LayeredPanelsExample frame = new LayeredPanelsExample();
            frame.setVisible(true);
        });
    }
}
