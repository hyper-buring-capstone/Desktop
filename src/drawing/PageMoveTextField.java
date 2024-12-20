package drawing;

import javax.swing.*;

import StateModel.StateModel;
import service.FileService;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PageMoveTextField extends JTextField {
		StateModel state;
		
    public PageMoveTextField(StateModel state, PdfPanel pdfPanel, DrawPanel drawPanel){
    	this.state = state;
        setText(String.valueOf(pdfPanel.getPageIndex()+1));
        //setForeground(Color.gray);
        setPreferredSize(new Dimension(50, 30));


        // 포커스를 받았을 때 안내 문구를 지우는 이벤트
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                //if (getText().equals(placeholder)) {
                    setText(""); // 안내 문구를 지운다.
                    setForeground(Color.BLACK); // 입력 색상 변경
            }

            @Override
            public void focusLost(FocusEvent e) {
                //if (getText().isEmpty()) {
                    setText(String.valueOf(pdfPanel.getPageIndex()+1)); // 텍스트가 없으면 안내 문구를 다시 표시
                   // setForeground(Color.GRAY); // 안내 문구 색상 회색으로 설정
               // }
            }
        });


        // 액션 리스너 (엔터 키를 눌렀을 때 동작)
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // 엔터 키 확인
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        // 숫자 입력값 받아오기
                        int number = Integer.parseInt(getText()); // 가상

                        if(number<=0 && number>pdfPanel.totalPageNum) return; //최대, 최소 페이지를 넘어갈 수 없음.
                        // 숫자에 따라 액션 발동
                        drawPanel.setPageIndex(number-1);
                        pdfPanel.setPageIndex(number-1);
                        state.setCurPageNum(number-1);
                        state.setLineString(FileService.getSpecificBlock(state.getNoteTitle(), state.getCurPageNum(), state.getImageWidth(), state.getImageHeight()));
                        if(state.getReceiver() != null) {
                            state.getReceiver().Sender("HEADER:PAGE&&" + (state.getCurPageNum()+1));
                        }

                    } catch (NumberFormatException ex) {
                        // 잘못된 숫자 입력 시 경고
                        JOptionPane.showMessageDialog(drawPanel.getParent(), "숫자를 입력하세요!", "잘못된 입력", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });



    }

    public void setPageIndex(int pageIndex){
       setText(String.valueOf(pageIndex+1));
       repaint();
    }


}
