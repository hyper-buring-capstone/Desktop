package home;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.Getter;
import model.Note;
import service.FileService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 초기 화면을 담당하는 프레임
 * 노트 선택, 환경설정, 노트 리스트 뷰어 등의 기능.
 */
public class HomeFrame extends JFrame {

    @Getter
    NoteListPanel noteListPanel;


    public HomeFrame(){
        //자체 설정
        setTitle("drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        FileService.initDirectory(); //디렉토리 구조 초기화.


        noteListPanel=new NoteListPanel(); //노트 리스트 패널
        HomeBtnPanel homeBtnPanel=new HomeBtnPanel(noteListPanel); //상단 버튼 패널

        //스크롤 기능
        JScrollPane jScrollPane=new JScrollPane(noteListPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16); //스크롤바 속도 조정.



        add(homeBtnPanel, BorderLayout.NORTH);
        add(jScrollPane, BorderLayout.CENTER);




        setVisible(true);
    }

//    @Override
//    public void paintComponents(Graphics g) {
//        super.paintComponents(g);
//        remove(noteListPanel);
//    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {

        UIManager.setLookAndFeel(new FlatLightLaf());
        HomeFrame homeFrame=new HomeFrame();
    }

    private List<Note> getDummyNotes(){
        List<Note> noteList=new ArrayList<>();

        Note note1=new Note("dummy1");
        Note note2=new Note("dummy2");
        Note note3=new Note("dummy3");

        noteList.add(note1);
        noteList.add(note2);
        noteList.add(note3);

        return noteList;
    }
}
