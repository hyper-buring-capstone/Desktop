package home;

import drawing.NoteFrame;
import lombok.Getter;
import model.Note;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import service.FileService;

import javax.swing.*;

import StateModel.StateModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static service.FileService.saveMeta;

/**
 * 노트 하나에 대한 gui 객체.
 * 노트 객체(entity)를 받아서 해당 내용을 gui로 변경하여 표시함.
 * JPanel -> JButton으로 변경. 어디든지 클릭해도 파일이 열렸으면 좋겠음.
 */
public class NotePanel extends JButton {

    private static final Log log = LogFactory.getLog(NotePanel.class);
    Note note;

    NotePopupMenu notePopupMenu;

    NoteFrame noteFrame;

    private StateModel state;

    @Getter
    NoteListPanel noteListPanel;

    JProgressBar jpb;

    HomeFrame homeFrame;
    public NotePanel(StateModel state, Note note, NoteListPanel noteListPanel, JProgressBar jpb, HomeFrame homeFrame){
        this.homeFrame=homeFrame;
        this.jpb=jpb;
        //생성자
    	this.state = state;
        this.note=note;
        notePopupMenu=new NotePopupMenu(note, this);
        this.noteListPanel=noteListPanel;

        // 패널 자체 설정
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
       // setBorder(new TitledBorder(new LineBorder(Color.BLUE, 2),"NotePanel" )); //디버깅용. 두께 0으로 하면 없어짐.
        setBackground(Color.white);
        setBorderPainted(false); //테두리 없앰
        //  setMargin(new Insets(0,0,0,0));


        // 노트 객체로부터 gui 객체 생성
        //노트 제목
        JLabel titleLabel=new JLabel(note.getTitle());
        titleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //노트 수정일
        JLabel modifiedLabel=new JLabel(note.getModified_at().toLocalDate().toString() + " ");
        modifiedLabel.setFont(new Font("맑은 고딕", Font.ITALIC, 12));
        modifiedLabel.setForeground(Color.gray);
        modifiedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //썸네일
        Image thumbNail=note.getThumbNail().getScaledInstance(150,150,Image.SCALE_AREA_AVERAGING); //썸네일 축소. 속도 따라서 알고리즘 조정해.
        ImageIcon thumbNailIcon=new ImageIcon(thumbNail);
        JLabel thumbNailLabel=new JLabel(thumbNailIcon);
        thumbNailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);



        add(thumbNailLabel);
        add(titleLabel);
        add(modifiedLabel);

        //클릭 시 노트 창으로 변환해야 함.
        //홈 프레임은 닫고 루트 프레임을 생성해야 한다.
        //addActionListener(actionListener); //버튼일 때
        //패널일 때는 아래
        addMouseListener(mouseAdapter);

        //setVisible(true);
        
    }

    public void listRefresh(){

        noteListPanel.refresh();
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }

// 더블 클릭으로 노트 열기
    MouseAdapter mouseAdapter=new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) { // 더블 클릭인지 확인

            	try {
					noteFrame = new NoteFrame(state, note, homeFrame);
					state.setNoteFrame(noteFrame);
					state.setNoteOpen(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

            }

        }


        //우클릭 시 메뉴 보이도록 설정.
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showMenu(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showMenu(e);
            }
        }

        private void showMenu(MouseEvent e) {
            notePopupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    };

}
