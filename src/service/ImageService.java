package service;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageService {


    // 아이콘의 색을 변경하고 크기를 조절하는 메서드
    public static Image recolorIcon(Image originalImage, Color color, int width, int height) {
        // 원본 이미지를 BufferedImage로 변환
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // 이미지 스케일링
        g2d.drawImage(originalImage, 0, 0, width, height, null);

        // 새로운 색상 덮어쓰기
        g2d.setComposite(AlphaComposite.SrcAtop); // 투명도 유지
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);

        g2d.dispose();
        return bufferedImage;
    }


//    public Image getImageBy
}
