package com.app.lib.comp;

import com.app.lib.layout.VerticalFlowLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Slider extends JPanel {
    private final JSlider input_slider=new JSlider();
    private final JLabel text_label=new JLabel();
    private final JLabel value_label=new JLabel();

    public Slider () {
        setPreferredSize(new Dimension(200, 50));
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(text_label);
        add(value_label);
        add(input_slider);

        input_slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedValue = ((JSlider)e.getSource()).getValue();
                value_label.setText("val: "+selectedValue);
            }
        });
    }

    @Override
    public void setBackground (Color clr) {
        super.setBackground(clr);
        for(Component c: getComponents()) {
            c.setBackground(clr);
        }
    }

    public Slider (int max, int min, String text) {
        this();
        config(max, min);
        text_label.setText(text);
    }

    public void config (int max, int min) {
        input_slider.setMaximum(max);
        input_slider.setMinimum(min);
        input_slider.setValue((max+min)/2);
    }

    public void setText (String newText) {
        text_label.setText(newText);
    }

    public int getValue () {
        return input_slider.getValue();
    }

    public void setValue(int val) {
        input_slider.setValue(val);
    }
}
