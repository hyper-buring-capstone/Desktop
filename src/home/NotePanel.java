package home;

import drawing.RootFrame;
import model.Note;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * 노트 하나에 대한 gui 객체.
 * 노트 객체(entity)를 받아서 해당 내용을 gui로 변경하여 표시함.
 * JPanel -> JButton으로 변경. 어디든지 클릭해도 파일이 열렸으면 좋겠음.
 */
public class NotePanel extends JButton {

    public NotePanel(Note note){
        // 패널 자체 설정
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT); //NotePanelList의 중앙에 위치하도록.
        //setSize(new Dimension(800,200)); // 사이즈 설정 어떻게함?;;
        setBorder(new TitledBorder(new LineBorder(Color.BLUE, 2),"NotePanel" )); //디버깅용
        
        // 노트 객체로부터 gui 객체 생성
        JLabel titleLabel=new JLabel(note.getTitle());
        JLabel modifiedLabel=new JLabel(note.getModified_at().toString());
        Image thumbNail=note.getThumbNail();

        add(titleLabel);
        add(modifiedLabel);

        //클릭 시 노트 창으로 변환해야 함.
        //홈 프레임은 닫고 루트 프레임을 생성해야 한다.
        addActionListener(actionListener);

        setVisible(true);
    }


    ActionListener actionListener=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //기존 창 닫고

            // 노트 창 오픈
            try {
                RootFrame rootFrame=new RootFrame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    };


}
