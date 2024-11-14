package drawing;

import lombok.Getter;
import model.Note;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    List<Image> imageList;

    public PdfPanel(Note note) throws IOException { //pdf 객체를 받아 이미지로 가지고 있게 됨.
        imageList=new ArrayList<>();
        pageNum=0;
        String title=note.getTitle();

        //자체 설정
        //setAlignmentX(Component.CENTER_ALIGNMENT); //NotePanelList의 중앙에 위치하도록.

        //디버깅용
        setBorder(new TitledBorder(new LineBorder(Color.BLUE,3),"PDFPanel"));


        //이미지 로딩
        File[] files=new File("C:\\drawing\\data\\"+title+"\\images").listFiles(); //이미지 폴더에 접근
        for(File file:files){
            imageList.add(ImageIO.read(file));
        }

        //이미지 사이즈

        if(!imageList.isEmpty()){
            width=imageList.getFirst().getWidth(null);
            height=imageList.getFirst().getHeight(null);
        }

        //setLayout(null);
        //setBounds(0,0,1000, 1000*height/width);

        setPreferredSize(new Dimension(1000, 1000*height/width));
        setMaximumSize(new Dimension(1000, 1000*height/width));
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
       // setLayout(null);
        //setBounds(0,0,width,height);
        g.drawImage(imageList.get(pageNum), 0,0,1000, 1000*height/width,null);

        setVisible(true);
    }


    public void goOtherPage(int num){
        if(num>=0 && num<imageList.size()){
           pageNum=num;
            repaint();
        }
    }

}
