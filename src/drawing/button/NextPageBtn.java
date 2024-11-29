package drawing.button;

import com.formdev.flatlaf.ui.FlatButtonUI;
import com.formdev.flatlaf.ui.FlatRoundBorder;

import StateModel.StateModel;
import drawing.PdfPanel;
import global.BaseButton;
import service.Receiver;
import drawing.DrawPanel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static service.ImageService.recolorIcon;

public class NextPageBtn extends BaseButton {
	StateModel state;
	
    public NextPageBtn(StateModel state, PdfPanel pdfPanel, DrawPanel drawPanel){
    	this.state = state;
        // 원본 이미지 아이콘 로드
        ImageIcon icon = new ImageIcon("src/icon/next.png");
        // 새 색상 지정
        Color newColor = Color.black;

        Image image=icon.getImage().getScaledInstance(25,25,Image.SCALE_AREA_AVERAGING);

        // 색상 변환된 이미지 생성
       // Image coloredIconImage = recolorIcon(icon.getImage(), newColor, 25, 25);
        setIcon(new ImageIcon(image));

        setBorderPainted(false);

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.setPageNum(drawPanel.getPageNum()+1);
                pdfPanel.goOtherPage(pdfPanel.getPageNum()+1);
                state.setCurPageNum(state.getCurPageNum()+1);
                state.getReceiver().sender("HEADER:PAGE&&" + state.getCurPageNum());
                getParent().getParent().repaint(); //8번 트러블 문제랑 비슷하게 해결.
            }
        };

        addActionListener(actionListener);

    }


}


