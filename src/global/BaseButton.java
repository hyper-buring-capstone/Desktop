package global;

import com.formdev.flatlaf.ui.FlatButtonBorder;
import com.formdev.flatlaf.ui.FlatButtonUI;

import javax.swing.*;
import java.awt.*;

public class BaseButton extends JButton {

    public BaseButton(){
        //setBackground(Color.white);
        putClientProperty("JButton.buttonType", "roundRect");
    }
}
