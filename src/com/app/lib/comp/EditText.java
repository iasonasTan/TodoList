package com.app.lib.comp;

import javax.swing.*;
import java.awt.*;

public class EditText extends JTextField {
    private String hint;

    public EditText () {
    }

    public EditText (String hint, int size) {
        super(size);
        this.hint = hint;
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        if (hint!=null&&getText().isBlank()) {
            g.drawString(hint, 5, 15);
        }
    }
}
