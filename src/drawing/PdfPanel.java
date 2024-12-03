package drawing;

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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static global.Constants.DATA_PATH;

public class PdfPanel extends JPanel {

    Image image;
    int width=0;
    int height=0;

    StateModel state;

    List<Image> imageList;

    @Getter
    int totalPageNum; //전체 페이지 수
    @Getter
    int pageIndex; //현재 페이지 수

    public PdfPanel(StateModel state, Note note) throws IOException { //pdf 객체를 받아 이미지로 가지고 있게 됨.
    	this.state = state;

        pageIndex=0;
        String title=note.getTitle();

        //자체 설정
        //setAlignmentX(Component.CENTER_ALIGNMENT); //NotePanelList의 중앙에 위치하도록.

        //디버깅용
       // setBorder(new TitledBorder(new LineBorder(Color.BLUE,4),"PDFPanel"));

        setBorder(new EtchedBorder());

        //이미지 로딩
        // 여기서 실행 시간의 병목 발생.
        File[] files=new File(DATA_PATH+title+"\\images").listFiles(); //이미지 폴더에 접근
        totalPageNum=files.length;
        imageList=new ArrayList<>(totalPageNum);

        Comparator<File> comparator=new Comparator<File>() {
            @Override
            public int compare(File f1, File f2)
            {
                return Integer.parseInt(f1.getName().split("\\.")[0])-Integer.parseInt(f2.getName().split("\\.")[0]);
            }
        };

        Arrays.sort(files, comparator);

        state.setFiles(files);
        state.setNoteTitle(title);


        //로딩 창 띄우면 좋은데 도저히 못하겠음;;

        //여기서 메모리 누수가 발생할 확률 있음.
//        for(int i=0; i<files.length; i++){
//            File file=files[i];
//            imageList.add(ImageIO.read(file)); //오래 걸림.
//        }

        //이미지 사이즈

//        if(!imageList.isEmpty()){
//            width=imageList.get(0).getWidth(null);
//            height=imageList.get(0).getHeight(null);
//        }
        setImage(0);

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

    // index에 위치한 페이지로 이미지 설정
    private void setImage(int index) throws IOException {
        this.image=ImageIO.read(new File(DATA_PATH + state.getNoteTitle() + "\\images\\"+index+".jpg"));
        state.setCurPageNum(index);
        pageIndex=index;
        repaint();
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
        g.drawImage(image, 0,0,parentW, parentW*height/width,null);

        setVisible(true);
    }


    public void setPageIndex(int num){
        if(num>=0 && num<totalPageNum){
            try{
                setImage(num);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}

