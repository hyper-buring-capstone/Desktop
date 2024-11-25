package home;

import home.button.FileOpenBtn;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

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
        topPanel.setLayout(null);
        topPanel.setBorder(new LineBorder(Color.GREEN));
        JLabel progLabel=new JLabel("     Phonote");
        progLabel.setFont(new Font("Times", Font.BOLD, 50));
        progLabel.setBounds(0,0,300,100);
        topPanel.add(progLabel);

        bottomPanel.setPreferredSize(new Dimension(0,100));
       //  bottomPanel.setMaximumSize(new Dimension(900,100));
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(Color.white);
        JLabel myDocLabel=new JLabel("        나의 문서");
        myDocLabel.setFont(new Font("맑은 고딕", Font.BOLD, 30));


        bottomPanel.add(myDocLabel, BorderLayout.WEST);

        //bottomPanel.add(new JLabel("                                                "));
        bottomPanel.add(new FileOpenBtn(noteListPanel), BorderLayout.EAST); //파일 열기 버튼 추가

        add(topPanel);
        add(bottomPanel);

    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        setPreferredSize(new Dimension(getParent().getWidth()-100, 100));
        setMaximumSize(new Dimension(getParent().getWidth()-100, 100));
    }
}
