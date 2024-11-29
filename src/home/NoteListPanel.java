package home;

import model.Note;
import service.FileService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import StateModel.StateModel;
import drawing.NoteFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 노트 패널의 리스트가 들어가는 패널.
 * 추후에 스크롤바 추가
 */
public class NoteListPanel extends JPanel {

    List<NotePanel> notePanelList=new ArrayList<>();
    List<Note> noteList;
    private StateModel state;

    HomeFrame homeFrame; // 로딩 닫히고 포커스 낮추기 위함,
    JProgressBar jpb;
    //노트 리스트를 받아서 NotePanel 리스트를 생성하고 이를 모두 add.
  
    public NoteListPanel(StateModel state, HomeFrame homeFrame){
    	this.state = state;
      this.jpb=jpb;
      this.homeFrame=homeFrame;

        //자체 패널 설정 
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //세로 방향으로 나열
        //setPreferredSize(new Dimension(1000,0));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.white);
        setBorder(BorderFactory.createEmptyBorder(10 , 30 , 10 , 30));//내부 패딩

       //setBorder(new TitledBorder(new LineBorder(Color.red, 3),"notePanelList")); //테두리 설정(디버깅)

        //노트 각각에 대한 패널 생성
        noteList=FileService.loadNoteList();
        for(Note note:noteList){
            NotePanel notePanel=new NotePanel(state, note, this, jpb, homeFrame);
            notePanelList.add(notePanel);
            add(notePanel); //gui에 삽입
        }


        //노트 데이터 불러오기.
        //noteList= FileService.loadNoteList();


   //     setBounds(0,0,1000,800);
     //   setVisible(true);

        //스크롤바를 위한 크기 조정
        int buttonWidth=200;
        int totalWidth=1000;
        int totalHeight=(notePanelList.size()/(totalWidth/buttonWidth)+1)*250;
        setPreferredSize(new Dimension(totalWidth,totalHeight));
        setMinimumSize(new Dimension(totalWidth,totalHeight));


    }

    public void releaseNote(){
        for(NotePanel notePanel:notePanelList){
            remove(notePanel);
        }
        notePanelList.clear(); //메모리 청소
    }

    public void refresh(){

        releaseNote();
        noteList=FileService.loadNoteList();
        for(Note note:noteList){
            NotePanel notePanel=new NotePanel(state, note, this, jpb, homeFrame);
            notePanelList.add(notePanel);
            add(notePanel); //gui에 삽입
        }
        revalidate();
        repaint();
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }
}
