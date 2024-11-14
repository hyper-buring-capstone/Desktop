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

        Image img = imageList.get(pageNum);
        int panelWidth = getWidth();
        int panelHeight = getHeight()-100; // 여백 추가

        // 원본 이미지 크기
        int imgWidth = img.getWidth(null);
        int imgHeight = img.getHeight(null);

        // 비율 유지하며 패널에 맞춰 조정된 이미지 크기
        double imgAspect = (double) imgWidth / imgHeight;
        double panelAspect = (double) panelWidth / panelHeight;

        int drawWidth, drawHeight;
        int xOffset = 0, yOffset = 0;

        // 패널의 비율에 따라 너비 또는 높이를 맞추고 여백을 계산
        if (panelAspect > imgAspect) {
            drawHeight = panelHeight;
            drawWidth = (int) (drawHeight * imgAspect);
            xOffset = (panelWidth - drawWidth) / 2;  // 좌우 여백 계산
        } else {
            drawWidth = panelWidth;
            drawHeight = (int) (drawWidth / imgAspect);
            yOffset = (panelHeight - drawHeight) / 2;  // 상하 여백 계산
        }

        // 이미지 그리기 (여백 포함하여 중앙에 위치)
        g.drawImage(img, xOffset, yOffset, drawWidth, drawHeight, null);
    }

    public void goOtherPage(int num){
        if(num>=0 && num<imageList.size()){
           pageNum=num;
            repaint();
        }
    }

}

