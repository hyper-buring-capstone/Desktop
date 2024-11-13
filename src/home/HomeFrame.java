package home;

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
        setTitle("drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        FileService.initDirectory(); //디렉토리 구조 초기화.


        noteListPanel=new NoteListPanel(); //노트 리스트 패널
        HomeBtnPanel homeBtnPanel=new HomeBtnPanel(noteListPanel); //상단 버튼 패널

        add(homeBtnPanel, BorderLayout.NORTH);
        add(noteListPanel, BorderLayout.CENTER);




        setVisible(true);
    }

//    @Override
//    public void paintComponents(Graphics g) {
//        super.paintComponents(g);
//        remove(noteListPanel);
//    }

    public static void main(String[] args){

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
