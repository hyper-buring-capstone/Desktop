package home;

import model.Note;
import service.FileService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 노트 패널의 리스트가 들어가는 패널.
 * 추후에 스크롤바 추가
 */
public class NoteListPanel extends JPanel {

    List<NotePanel> notePanelList=new ArrayList<>();
    List<Note> noteList=new ArrayList<>();

    //노트 리스트를 받아서 NotePanel 리스트를 생성하고 이를 모두 add.
    public NoteListPanel(){

        //자체 패널 설정 
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //세로 방향으로 나열

        setBorder(new TitledBorder(new LineBorder(Color.red, 3),"notePanelList")); //테두리 설정(디버깅)

        //노트 각각에 대한 패널 생성
        noteList=FileService.loadNoteList();
        for(Note note:noteList){
            add(new NotePanel(note)); //gui에 삽입
        }


        //노트 데이터 불러오기.
        //noteList= FileService.loadNoteList();


        setBounds(0,0,1000,800);
        setVisible(true);






    }

    public void refresh(){
        repaint();
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        g.drawRect(0,0,100,100);
        noteList=FileService.loadNoteList();
    }
}
