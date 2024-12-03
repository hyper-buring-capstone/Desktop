package drawing.button;

import drawing.DrawPanel;
import global.BaseButton;
import model.PenLine;
import service.FileService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

import static global.Constants.SAVE_ICON_PATH;
import static service.ImageService.recolorIcon;

/**
 * 점 데이터를 저장하는 (ctrl+s) 버튼.
 */
public class SaveBtn extends BaseButton {
    DrawPanel drawPanel;
    public SaveBtn(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;

        // 원본 이미지 아이콘 로드
        ImageIcon icon = new ImageIcon(SAVE_ICON_PATH);
        // 새 색상 지정
        Color newColor = Color.black;

        // 색상 변환된 이미지 생성
        Image coloredIconImage = recolorIcon(icon.getImage(), newColor, 25, 25);
        setIcon(new ImageIcon(coloredIconImage));

        // 버튼 스타일
        setBorderPainted(false);

        addActionListener(actionListener);
    }






    //버튼 누르면 드로잉 정보 저장해야 함.
    ActionListener actionListener=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            

            FileService.saveLines(drawPanel.getNote(),drawPanel.getPenLineLists()); //저장


        }
    };
}
