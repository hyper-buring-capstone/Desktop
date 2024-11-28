package examples;

import javax.swing.*;
import java.util.Enumeration;

public class UIManagerKeys {
    public static void main(String[] args) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (key.toString().endsWith("Icon")) {
                System.out.println(key + ": " + UIManager.getIcon(key));
            }
        }
    }
}
