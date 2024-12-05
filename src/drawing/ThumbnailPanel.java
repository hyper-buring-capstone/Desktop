package drawing;

import StateModel.StateModel;
import home.LoadingFrame;
import model.Note;
import service.FileService;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

//왼 쪽 썸네일 바로가기의 스크롤.
//썸네일 불러올 때 다른 쓰레드에서 동기적으로 실행해야
// 키면서 딜레이 없앨 수 있음.
public class ThumbnailPanel extends JPanel implements Runnable {

    List<Image> imageList;
    Note note;
    StateModel state;
    LoadingFrame loadingFrame;
    NoteFrame noteFrame;
    List<ThumbnailBtn> thumbnailBtnList;
    public ThumbnailPanel(StateModel state, Note note, NoteFrame noteFrame){
        this.note=note;
        this.state=state;
        this.noteFrame=noteFrame;

        //setBackground(Color.white);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 세로 방향 나열



        setPreferredSize(new Dimension(200,0));


        //로딩창 띄우기 위해.
        noteFrame.setVisible(false);
         loadingFrame=new LoadingFrame();


        Thread thread=new Thread(this);

        thread.start();

    }
    
    public void setSelected(int num) {
    	for(int i = 0; i < thumbnailBtnList.size(); i++) {
    		if(num != i) {
    	    	thumbnailBtnList.get(i).setSelected(false);
    		}
    		else {
    	    	thumbnailBtnList.get(i).setSelected(true);
    		}
	    	thumbnailBtnList.get(i).repaint();
    	}
    }

    @Override
    public void run() {

        thumbnailBtnList = new ArrayList<>();
        imageList= FileService.getImagesByTitle(note.getTitle(), loadingFrame); //시간 체크 48p 기준 2.8초

        loadingFrame.setLoadingText("페이지를 구성하는 중이에요");

        setPreferredSize((new Dimension(200,200*imageList.size()))); //높이 추후에 수정해야 됨.
        for(int i=0; i<imageList.size(); i++){
        	if(!state.getNoteOpen()) {
        		break;
        	}
            ThumbnailBtn thumbnailBtn=new ThumbnailBtn(i, imageList.get(i)); //개당 0.2초
            thumbnailBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(thumbnailBtn);
            thumbnailBtnList.add(thumbnailBtn);
            revalidate();
            if(i==5) {
                noteFrame.setVisible(true);
                loadingFrame.dispose();
            }
        }
        if(!state.getNoteOpen()) {
        	noteFrame.setVisible(false);
        }
        loadingFrame.dispose();
    }


    private class ThumbnailBtn extends JButton{

        int pageIndex;
        boolean isSelected;
        private ThumbnailBtn(int pageIndex, Image thumbnail){
            this.pageIndex=pageIndex;
            if(state.getCurPageNum() == pageIndex) {
            	isSelected = true;
            }
            else {
            	isSelected = false;
            }
            // 크기
            setPreferredSize(new Dimension(150,0));
            //setMaximumSize(new Dimension(150,200));

            // 레이아웃
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setMargin(new Insets(0,10,30,10));
            setBorderPainted(false);

            // 색상
            setBackground(new Color(0,0,0,0));

            // 이미지
            if(state.getImageWidth() < state.getImageHeight()) {
                thumbnail=thumbnail.getScaledInstance((150*state.getImageWidth())/state.getImageHeight(),150,Image.SCALE_AREA_AVERAGING); //크기 조정
            }
            else {
                thumbnail=thumbnail.getScaledInstance(150, (150*state.getImageHeight())/state.getImageWidth(),Image.SCALE_AREA_AVERAGING); //크기 조정
            }
            ImageIcon thumbNailIcon=new ImageIcon(thumbnail); //정확히는 여기서 시간 많이 소요됨. 0.2초정도
            JLabel thumbNailLabel=new JLabel(thumbNailIcon);
            
            // 사이즈 고정
            thumbNailLabel.setPreferredSize(new Dimension(150, 150));
            thumbNailLabel.setMaximumSize(new Dimension(150, 150));
            thumbNailLabel.setMinimumSize(new Dimension(150, 150));

            thumbNailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            //번호
            JLabel numberLabel=new JLabel(String.valueOf(pageIndex+1));

            add(numberLabel);
            add(thumbNailLabel);


            addActionListener(actionListener);

            addMouseListener(mouseAdapter);
        }
        
        public void setSelected(boolean select) {
        	this.isSelected = select;
        }

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.setPageIndex(pageIndex);
            }
        };
        
//        // 오버레이를 그리기 위해 paintComponent 오버라이드
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g); // 기본 버튼 렌더링
//
//            if (true) {//isOverlayVisible) {
//                Graphics2D g2d = (Graphics2D) g.create();
//                g2d.setColor(new Color(128, 128, 128, 128)); // 회색(50% 투명도)
//                g2d.fillRect(0, 0, getWidth(), getHeight()); // 전체 버튼 크기만큼 사각형 그리기
//                g2d.dispose();
//            }
//        }
     // 오버레이를 그리기 위해 paint 메서드 오버라이드
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // 기본 버튼 렌더링

            if (isSelected) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(128, 128, 128, 64)); // 회색(25% 투명도)
                g2d.fillRect(0, 0, getWidth(), getHeight()); // 전체 버튼 크기만큼 사각형 그리기
                g2d.dispose();
            }
        }

        MouseAdapter mouseAdapter=new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorderPainted(true);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorderPainted(false);
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                setBorderPainted(true);
                repaint();super.mouseClicked(e);
            }
        };
        
        @Override
        public void paint(Graphics g) {
            super.paint(g); // 기본 그리기 호출
            if (isSelected) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(128, 128, 128, 64)); // 투명도 있는 회색
                g2d.fillRect(0, 0, getWidth(), getHeight()); // 전체 크기 덮기
                g2d.dispose();
            }
        }
    }



}
