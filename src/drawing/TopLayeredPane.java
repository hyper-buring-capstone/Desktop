package drawing;

import drawing.button.NextPageBtn;
import drawing.button.PrevPageBtn;
import drawing.button.SaveBtn;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopLayeredPane extends JPanel {
    PdfPanel pdfPanel;
    DrawPanel drawPanel;

    TopLayeredPane(PdfPanel pdfPanel, DrawPanel drawPanel){
        this.pdfPanel=pdfPanel; //버튼 조작을 위해 객체 받음.
        this.drawPanel=drawPanel; // 이하동문


        //setBackground(new Color(120,1,22));
        setBackground(Color.white);
        setLayout(new FlowLayout());
        setBorder(new LineBorder(Color.red));
        setBorder(BorderFactory.createEmptyBorder(10 , 50 , 10 , 50));//내부 패딩


        JButton nextPageBtn=new NextPageBtn(pdfPanel, drawPanel);
        nextPageBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
        JButton prevPageBtn=new PrevPageBtn(pdfPanel, drawPanel);
        JButton saveBtn=new SaveBtn(drawPanel);

        add(prevPageBtn);
        add(nextPageBtn);
        add(saveBtn);



        setVisible(true);


    }
}
