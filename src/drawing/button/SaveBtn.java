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

/**
 * 점 데이터를 저장하는 (ctrl+s) 버튼.
 */
public class SaveBtn extends BaseButton {
    DrawPanel drawPanel;
    public SaveBtn(DrawPanel drawPanel){
        this.drawPanel=drawPanel;

        //이미지 설정
        ImageIcon icon=new ImageIcon("src/icon/save.png");
        Image iconImage=icon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        icon.setImage(iconImage);
        setIcon(icon);
       // setBackground(new Color(0,0,0,0));
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
