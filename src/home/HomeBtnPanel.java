package home;

import home.button.FileOpenBtn;

import javax.swing.*;
import java.awt.*;

/**
 * 메인 화면 상단의 버튼 리스트
 * (파일 불러오기 , 정렬, 설정 등)
 */
public class HomeBtnPanel extends JPanel {

    public HomeBtnPanel(NoteListPanel noteListPanel){

        setLayout(new FlowLayout());

        add(new FileOpenBtn(noteListPanel));
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }
}
