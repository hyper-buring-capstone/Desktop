package home;

import drawing.NoteFrame;
import lombok.Getter;
import model.Note;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import service.FileService;
import service.Receiver;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;

import StateModel.StateModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static global.Constants.FONT_LIGHT;
import static global.Constants.FONT_REGULAR;
import static service.FileService.saveMeta;

/**
 * 노트 하나에 대한 gui 객체.
 * 노트 객체(entity)를 받아서 해당 내용을 gui로 변경하여 표시함.
 * JPanel -> JButton으로 변경. 어디든지 클릭해도 파일이 열렸으면 좋겠음.
 */
public class NotePanel extends JButton {

    private static final Log log = LogFactory.getLog(NotePanel.class);

    @Getter
    Note note;

    NotePopupMenu notePopupMenu;

    NoteFrame noteFrame;

    private StateModel state;

    @Getter
    NoteListPanel noteListPanel;


    HomeFrame homeFrame;

    JLabel modifiedLabel;
    public NotePanel(StateModel state, Note note, NoteListPanel noteListPanel, HomeFrame homeFrame){
        this.homeFrame=homeFrame;
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
        titleLabel.setFont(FONT_LIGHT.deriveFont(15.f));
        titleLabel.setPreferredSize(new Dimension(150,50));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //노트 수정일
        modifiedLabel=new JLabel(note.getModified_at().toLocalDate().toString() + " ");
        modifiedLabel.setFont(FONT_LIGHT.deriveFont(12.f));
        modifiedLabel.setForeground(Color.gray);
        modifiedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //썸네일
        //여기서 시간 많이 소요됨. //average: 0.25~0.3초 ,fast: 0.16초 내외. 근데 화질 너무깨짐.
        Image thumbNail = null;
        if(note.getThumbNail().getWidth(null) < note.getThumbNail().getHeight(null)) {
            thumbNail=note.getThumbNail().getScaledInstance((150*note.getThumbNail().getWidth(null))/note.getThumbNail().getHeight(null),150,Image.SCALE_AREA_AVERAGING); //크기 조정
        }
        else {
            thumbNail=note.getThumbNail().getScaledInstance(150, (150*note.getThumbNail().getHeight(null))/note.getThumbNail().getWidth(null),Image.SCALE_AREA_AVERAGING); //크기 조정
        }
        ImageIcon thumbNailIcon=new ImageIcon(thumbNail); //정확히는 여기서 시간 많이 소요됨. 0.2초정도
        JLabel thumbNailLabel=new JLabel(thumbNailIcon);
        
     // JLabel 크기 고정 (전체 크기를 150x150으로 맞춤)
        thumbNailLabel.setPreferredSize(new Dimension(150, 150));
        thumbNailLabel.setMaximumSize(new Dimension(150, 150));
        thumbNailLabel.setMinimumSize(new Dimension(150, 150));
        thumbNailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);



        add(thumbNailLabel);
        add(Box.createVerticalStrut(20)); // 라벨과 진행 막대 사이 여백)
        add(titleLabel);
        add(modifiedLabel);

        //클릭 시 노트 창으로 변환해야 함.
        //홈 프레임은 닫고 루트 프레임을 생성해야 한다.
        //addActionListener(actionListener); //버튼일 때
        //패널일 때는 아래
        addMouseListener(mouseAdapter);

        //setVisible(true);
        this.getHeight();
        this.getWidth();
    }

    public void setLastOpenDate(){
        remove(modifiedLabel);

        modifiedLabel.setText(LocalDate.now()+" ");
        modifiedLabel.setFont(FONT_LIGHT.deriveFont(Font.ITALIC, 12f));
        modifiedLabel.setForeground(Color.gray);
        modifiedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(modifiedLabel);

    }

    public void listRefresh(){

        noteListPanel.remove(this);
        noteListPanel.revalidate();
        noteListPanel.repaint();

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

                    saveMeta(note);
                    noteListPanel.setLastNote(note); //최근에 오픈한 노트.

					state.setNoteFrame(noteFrame);
					state.setNoteOpen(true);
					state.setCurPageNum(0);

					if(state.getReceiver() != null) {
						state.getReceiver().Sender("HEADER:PAGE&&" + 1);	
					}

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
