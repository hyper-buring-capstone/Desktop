package drawing;

import drawing.button.NextPageBtn;
import drawing.button.PrevPageBtn;
import drawing.button.SaveBtn;

import javax.swing.*;
import java.awt.*;

public class NoteTopPanel extends JPanel {
    PdfPanel pdfPanel;
    DrawPanel drawPanel;

    NoteTopPanel(PdfPanel pdfPanel, DrawPanel drawPanel){
        this.pdfPanel=pdfPanel; //버튼 조작을 위해 객체 받음.
        this.drawPanel=drawPanel; // 이하동문


        //setBackground(new Color(120,1,22));
        setBackground(Color.white);
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createEmptyBorder(10 , 50 , 10 , 50));//내부 패딩


        JButton nextPageBtn=new NextPageBtn(pdfPanel, drawPanel);
        nextPageBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
        JButton prevPageBtn=new PrevPageBtn(pdfPanel, drawPanel);
        JButton saveBtn=new SaveBtn(drawPanel);

        add(prevPageBtn); //이전 버튼
        add(nextPageBtn); //다음 버튼
        add(new PageMoveTextField(pdfPanel, drawPanel)); //페이지 무빙
        add(new JLabel("/ "+ pdfPanel.getImageListSize() + " "));
        add(saveBtn); // 저장 버튼



        setVisible(true);


    }
}
