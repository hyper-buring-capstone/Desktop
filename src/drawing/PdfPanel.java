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
        int dpi = 300;  // 고해상도를 원하면 더 높여도 되긴함(근데 300 이상으론 크게 의미 없더라. 아마도 확대할 때는 필요할지도)

        for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {
            // renderImageWithDPI로 DPI를 설정하여 고해상도 이미지 생성
            imageList.add(pdfRenderer.renderImageWithDPI(i, dpi));
        }

        setLayout(null);
        setBounds(0,0,1000,800);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setLayout(null);
        setBounds(0, 0, getWidth(), getHeight());  // 패널 크기에 맞게 조정

        Image img = imageList.get(pageNum); // JPanel 크기에 맞게 이미지 크기 조정
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);  // 패널 크기에 맞게 이미지 크기 조정
    }

    public void goOtherPage(int num){
        if(num>=0 && num<imageList.size()){
           pageNum=num;
            repaint();
        }
    }

}

