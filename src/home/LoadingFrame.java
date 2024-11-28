package home;

import javax.swing.*;
import java.awt.*;

public class LoadingFrame extends JFrame implements Runnable{

    JProgressBar jpb;
    public LoadingFrame(){
        this.jpb=jpb;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("로딩 중");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel jPanel=new JPanel();
        jPanel.add(new JLabel("hello"));
        add(jPanel);
        JLabel jLabel=new JLabel("로딩 중");
        add(jLabel);

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
