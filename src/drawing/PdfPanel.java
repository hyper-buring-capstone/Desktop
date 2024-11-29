package drawing;

import home.HomeFrame;
import home.LoadingFrame;
import lombok.Getter;
import model.Note;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import StateModel.StateModel;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfPanel extends JPanel {

    Image image;
    int width=0;
    int height=0;
    @Getter
    int pageNum;
    StateModel state;

    List<Image> imageList;

    public PdfPanel(StateModel state, Note note) throws IOException { //pdf 객체를 받아 이미지로 가지고 있게 됨.
    	this.state = state;
        imageList=new ArrayList<>();
        pageNum=0;
        String title=note.getTitle();

        //자체 설정
        //setAlignmentX(Component.CENTER_ALIGNMENT); //NotePanelList의 중앙에 위치하도록.

        //디버깅용
       // setBorder(new TitledBorder(new LineBorder(Color.BLUE,4),"PDFPanel"));

        setBorder(new EtchedBorder());

        //이미지 로딩
        // 여기서 실행 시간의 병목 발생.
        File[] files=new File("C:\\drawing\\data\\"+title+"\\images").listFiles(); //이미지 폴더에 접근
        state.setFiles(files);


        //로딩 창 띄우면 좋은데 도저히 못하겠음;;

        //여기서 메모리 누수가 발생할 확률 있음.
        for(int i=0; i<files.length; i++){
            File file=files[i];
            imageList.add(ImageIO.read(file)); //오래 걸림.
        }





        //이미지 사이즈

        if(!imageList.isEmpty()){
            width=imageList.get(0).getWidth(null);
            height=imageList.get(0).getHeight(null);
        }

        width=note.getThumbNail().getWidth(null);
        height=note.getThumbNail().getHeight(null);


        //setLayout(null);
        //setBounds(0,0,1000, 1000*height/width);

        int imageWidth = 1000;
        int imageHeight = 1000*height/width;
        setMinimumSize(new Dimension(imageWidth, imageHeight));
        setPreferredSize(new Dimension(imageWidth, imageHeight));
        setMaximumSize(new Dimension(imageWidth, imageHeight));
        state.setImageWidth(imageWidth);
        state.setImageHeight(imageHeight);
        setVisible(true);

    }

    public void removeAllImages(){
        imageList.clear();
    }

    //백그라운드에서 이미지 로딩하기.
    // 백그라운드에서 돌리니깐 이미지 로딩하는 동안 다른 프레임 떠서 오류남.;;
    class ImageLoadingTask extends SwingWorker<Void, Integer> {
        private final JProgressBar progressBar;
        File[] files;

        public ImageLoadingTask(JProgressBar progressBar, File[] files) {
            this.progressBar = progressBar;
            this.files=files;
        }

        @Override
        protected Void doInBackground() throws IOException {


            int totalImages = files.length;

            for (int i = 0; i < totalImages; i++) {
                File file=files[i];
                imageList.add(ImageIO.read(file));

                // 진행 상태 업데이트
                int progress = (int) (((i + 1) / (double) totalImages) * 100);
                publish(progress); // UI 업데이트 요청
            }

            return null;
        }

        @Override
        protected void process(java.util.List<Integer> chunks) {
            // 마지막 progress 값으로 업데이트
            int progress = chunks.get(chunks.size() - 1);
            progressBar.setValue(progress);
        }


    }

    public int getImageListSize() {
    	return imageList.size();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
       // setLayout(null);
        //setBounds(0,0,width,height);
//        int parentW=getParent().getWidth();
        int parentW=1000;
        setMaximumSize(new Dimension(parentW, parentW*height/width));
        setPreferredSize(new Dimension(parentW, parentW*height/width));
        g.drawImage(imageList.get(pageNum), 0,0,parentW, parentW*height/width,null);

        setVisible(true);
    }


    public void goOtherPage(int num){
        if(num>=0 && num<imageList.size()){
        	pageNum=num;
        	repaint();
        }
    }

}

