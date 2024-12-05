package test;

import global.BtParser;
import global.Constants;
import home.LoadingFrame;
import org.junit.Test;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;

import static global.Constants.BASE_PATH;
import static global.Constants.DATA_PATH;
import static service.FileService.createDirectory;

public class TestClass {

    @Test
    public void BtParserTest(){
        System.out.println(BtParser.getMsgType("HEADER:DRAWING"));
        System.out.println(BtParser.getMsgType("HEADER:PANNING"));
        System.out.println(BtParser.getMsgType("END"));
    }

    @Test
    public void 전역변수테스트(){
        System.out.println(Constants.DATA_PATH.toString().equals("c:\\drawing\\data\\"));
    }

    @Test
    public void 경로생성테스트(){

        //경로 끝에 "\\" 있어도 생성 됨.
        createDirectory(BASE_PATH);
        createDirectory(DATA_PATH);
    }

    @Test
    public void 로딩창띄우기(){
        LoadingFrame loadingFrame=new LoadingFrame();
        loadingFrame.setVisible(true);
        while(true){
            ;
        }
    }

    @Test
    public void 플로팅띄우기(){
        JFrame frame = new JFrame("OverlayLayout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // JPanel 생성 및 OverlayLayout 설정
        JPanel panel = new JPanel();
        panel.setSize(400,300);
        panel.setLayout(null);
        panel.setBorder(new LineBorder(Color.red));

        // 첫 번째 컴포넌트
        JLabel backgroundLabel = new JLabel("Background Layer");
        backgroundLabel.setVerticalAlignment(SwingConstants.CENTER);
        backgroundLabel.setFont(new Font("Arial", Font.BOLD, 20));
        backgroundLabel.setOpaque(true);
        backgroundLabel.setBackground(Color.CYAN);
        backgroundLabel.setBorder(new LineBorder(Color.red));
        backgroundLabel.setBounds(0, 0, 200, 500); // 가로 가운데 정렬
        panel.add(backgroundLabel);

        // 두 번째 컴포넌트 (겹칠 내용)
        JLabel overlayLabel = new JLabel("Overlay Layer");
        overlayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        overlayLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        overlayLabel.setOpaque(true);
        overlayLabel.setBackground(new Color(255, 200, 200, 150)); // 투명한 배경
        overlayLabel.setBounds(100, 200, 200, 500);
        panel.add(overlayLabel);

        // JPanel을 JFrame에 추가
        frame.add(panel);
        frame.setVisible(true);
        while(true){

        }
    }
}