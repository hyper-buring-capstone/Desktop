package drawing;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfPanel extends JPanel {

    Image image;

    @Getter
    int pageNum;

    List<Image> imageList;

    public PdfPanel(PDDocument pdDocument) throws IOException { //pdf 객체를 받아 이미지로 가지고 있게 됨.
        imageList=new ArrayList<>();
        pageNum=0;

        PDFRenderer pdfRenderer=new PDFRenderer(pdDocument);
        for(int i=0; i< pdDocument.getNumberOfPages(); i++){
            imageList.add(pdfRenderer.renderImage(i)); //이미지 사이즈 조정 가능함.

        }

        setLayout(null);
        setBounds(0,0,1000,800);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setLayout(null);
        setBounds(0,0,1000,800);
        g.drawImage(imageList.get(pageNum), 0,0,null);
    }


    public void goOtherPage(int num){
        if(num>=0 && num<imageList.size()){
            pageNum=num;
        }
    }

}
