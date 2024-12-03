package examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class FollowMouseExample extends JPanel implements MouseMotionListener {
    private int mouseX = -50; // 초기 마우스 X 좌표
    private int mouseY = -50; // 초기 마우스 Y 좌표
    private static final int CIRCLE_DIAMETER = 50; // 원의 크기

    public FollowMouseExample() {
        addMouseMotionListener(this); // 마우스 모션 리스너 추가
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        setDoubleBuffered(false);
        // 배경 색상
        //g.setColor(Color.WHITE);
       // g.fillRect(0, 0, getWidth(), getHeight());

        // 원 그리기
        g.setColor(Color.RED);
        g.fillOval(mouseX - CIRCLE_DIAMETER / 2, mouseY - CIRCLE_DIAMETER / 2, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // 마우스 좌표 업데이트
        mouseX = e.getX();
        mouseY = e.getY();

        // 다시 그리기 요청
        getParent().repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // 드래그 중에도 동일 동작
        mouseMoved(e);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Follow Mouse Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        FollowMouseExample panel = new FollowMouseExample();
        frame.add(panel);

        frame.setVisible(true);
    }
}
