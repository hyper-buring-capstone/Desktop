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
import java.util.List;

//왼 쪽 썸네일 바로가기의 스크롤.
//썸네일 불러올 때 다른 쓰레드에서 동기적으로 실행해야
// 키면서 딜레이 없앨 수 있음.
//

public class ThumbnailPanel extends JPanel implements Runnable {

    List<Image> imageList;
    Note note;
    StateModel state;
    LoadingFrame loadingFrame;
    NoteFrame noteFrame;
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

    @Override
    public void run() {

        imageList= FileService.getImagesByTitle(note.getTitle()); //시간 체크 48p 기준 2.8초

        loadingFrame.setLoadingText("페이지를 구성하는 중이에요");

        setPreferredSize((new Dimension(200,200*imageList.size()))); //높이 추후에 수정해야 됨.
        for(int i=0; i<imageList.size(); i++){
            JButton thumbnailBtn=new ThumbnailBtn(i, imageList.get(i)); //개당 0.2초
            thumbnailBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(thumbnailBtn);
            revalidate();
            if(i==5) {
                noteFrame.setVisible(true);
                loadingFrame.dispose();
            }
        }
        noteFrame.setVisible(true);
        loadingFrame.dispose();
    }


    private class ThumbnailBtn extends JButton{

        int pageIndex;
        private ThumbnailBtn(int pageIndex, Image thumbnail){
            this.pageIndex=pageIndex;
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
            thumbnail=thumbnail.getScaledInstance(150,150,Image.SCALE_AREA_AVERAGING); //크기 조정
            ImageIcon thumbNailIcon=new ImageIcon(thumbnail); //정확히는 여기서 시간 많이 소요됨. 0.2초정도
            JLabel thumbNailLabel=new JLabel(thumbNailIcon);
            thumbNailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            //번호
            JLabel numberLabel=new JLabel(String.valueOf(pageIndex+1));

            add(numberLabel);
            add(thumbNailLabel);


            addActionListener(actionListener);

            addMouseListener(mouseAdapter);
        }

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.setPageIndex(pageIndex);
            }
        };

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
    }



}
