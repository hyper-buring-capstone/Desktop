package drawing;

import drawing.button.NextPageBtn;
import drawing.button.PrevPageBtn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopLayeredPane extends JLayeredPane {
    PdfPanel pdfPanel;
    TopLayeredPane(PdfPanel pdfPanel){
        this.pdfPanel=pdfPanel; //버튼 조작을 위해 객체 받음.

        setSize(1000, 100);
        setBackground(Color.black);
        setLayout(new FlowLayout());


        JButton nextPageBtn=new NextPageBtn(pdfPanel);
        JButton prevPageBtn=new PrevPageBtn(pdfPanel);

        add(prevPageBtn);
        add(nextPageBtn);



        setVisible(true);


    }
}
