package home;

import home.button.FileOpenBtn;
import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static global.Constants.*;

/**
 * 메인 화면 상단의 버튼 리스트
 * (파일 불러오기 , 정렬, 설정 등)
 */
public class HomeBtnPanel extends JPanel {

    public HomeBtnPanel(NoteListPanel noteListPanel){

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        setPreferredSize(new Dimension(1000,150));
        // setBorder(new TitledBorder(new LineBorder(Color.GREEN, 3),"HomeBtnPanel"));


        JPanel topPanel=new JPanel();
        JPanel bottomPanel=new JPanel();

        topPanel.setPreferredSize(new Dimension(0,200));
        topPanel.setBackground(Color.white);
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10 , 50 , 10 , 10));//내부 패딩
       // topPanel.setBorder(new LineBorder(Color.GREEN));
        
        try {
			Image icon = ImageIO.read(new File(APP_UPSCAILING_ICON_PATH));
			icon = icon.getScaledInstance(70,70,Image.SCALE_AREA_AVERAGING);
			ImageIcon LAVAIcon =new ImageIcon(icon);
			
			JLabel iconLabel = new JLabel(LAVAIcon);
			topPanel.add(iconLabel, BorderLayout.WEST);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        JLabel progLabel=new JLabel(APP_NAME);
        progLabel.setFont(FONT_REGULAR.deriveFont(50f));
        progLabel.setForeground(new Color(242, 59, 60));
        progLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
//        progLabel.setVerticalAlignment(verticalAlignment);
        topPanel.add(progLabel, BorderLayout.CENTER);

        bottomPanel.setPreferredSize(new Dimension(0,100));
//        bottomPanel.setPreferredSize(new Dimension(0,200));
       //  bottomPanel.setMaximumSize(new Dimension(900,100));
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(Color.white);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10 , 50 , 10 , 50));//내부 패딩
        JLabel myDocLabel=new JLabel("나의 문서");
        myDocLabel.setFont(FONT_REGULAR.deriveFont(30f));


        bottomPanel.add(myDocLabel, BorderLayout.WEST);

        //bottomPanel.add(new JLabel("                                                "));
        bottomPanel.add(new FileOpenBtn(noteListPanel), BorderLayout.EAST); //파일 열기 버튼 추가

        add(topPanel);
        add(bottomPanel);

    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }
}
