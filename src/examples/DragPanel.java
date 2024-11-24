package examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DragPanel extends JPanel {
    private Point startPoint; // 처음 클릭한 좌표
    private Point currentPoint; // 드래그 중의 현재 좌표

    public DragPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));

        // MouseAdapter 추가
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 드래그 시작 좌표 저장
                startPoint = e.getPoint();
                currentPoint = startPoint; // 초기화
                getParent().repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 드래그 중 현재 좌표 저장
                currentPoint = e.getPoint();
                getParent().repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (startPoint != null && currentPoint != null) {
            g.setColor(Color.RED);
            g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y); // 클릭-드래그 연결 선
            g.fillOval(startPoint.x - 3, startPoint.y - 3, 6, 6); // 시작점 표시
            g.fillOval(currentPoint.x - 3, currentPoint.y - 3, 6, 6); // 현재 좌표 표시
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mouse Drag Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DragPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
