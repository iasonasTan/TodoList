package com.app.task;

import com.app.lib.layout.HorizontalFlowLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

public final class Task extends JPanel {
    private TaskData data;
    private TaskManager manager;
    private boolean focused;
    private boolean tempFocused;

    private final Dimension DEFAULT_SIZE = new Dimension(130, 35);
    private final Dimension EXPANDED_SIZE = new Dimension(200, 100);

    // ui
    private final JButton completeButton=new JButton();
    private final JLabel nameLabel=new JLabel();
    private final JTextArea descLabel=new JTextArea();
    private final Border DEFAULT_BORDER = BorderFactory.createLineBorder(Color.BLACK, 2);
    private final Border FOCUSED_BORDER = BorderFactory.createLineBorder(Color.ORANGE, 3);
    private final Border TEMP_FOCUSED_BORDER = BorderFactory.createLineBorder(Color.RED, 2);
    private final InfoPopup info_popup=new InfoPopup(this);

    private Task () {
    }

    public Task(TaskData someData, TaskManager manager, Color background_color) {
        this.data = someData;
        this.manager = manager;
        setBackground(background_color);

        try {
            initUI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initUI () throws IOException {
        setPreferredSize(DEFAULT_SIZE);
        setLayout(new HorizontalFlowLayout(5,5));
        setBorder(DEFAULT_BORDER);

        nameLabel.setText(data.name());
        descLabel.setText(data.desc());
        info_popup.setText(data.name());

        BufferedImage bi_buff = ImageIO.read(getClass().getResource("/icons/task_complete.png"));
        Icon bi = new ImageIcon(bi_buff.getScaledInstance(25,25,0));
        completeButton.setPreferredSize(new Dimension(27,25));
        completeButton.setIcon(bi);
        completeButton.setBackground(Color.GREEN);
        completeButton.addActionListener(ae -> manager.clickTask(this));

        setDefaultState(true);
        final Dimension descLabelDimension=new Dimension(EXPANDED_SIZE);
        descLabelDimension.width-=10;
        descLabelDimension.height-=10;
        descLabel.setPreferredSize(descLabelDimension);
        descLabel.setLineWrap(true);

        addMouseListener(new MouseHandler());

        add(completeButton);
        add(nameLabel);
        add(descLabel);
    }


    @Override
    public void setBackground (Color clr) {
        super.setBackground(clr);
        for (Component c: getComponents()) {
            c.setBackground(clr);
        }
    }

    public boolean isDescHiddenOrHide() {
        if (nameLabel.isVisible()) {
            return true;
        } else {
            setDefaultState(true);
            return false;
        }
    }

    public TaskData getData() {
        return data;
    }

    public static Task empty () {
        return new Task();
    }

    public boolean isOnDefaultState() {
        return nameLabel.isVisible();
    }

    private class MouseHandler implements MouseListener {
        @Override public void mouseClicked(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            getFocus();
            manager.requestFocus();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            $requestTempFocus();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setTempFocused(false);
        }
    }

    private void $requestTempFocus () {
        manager.setTempFocused(this);
    }

    public void setTempFocused (boolean f) {
        tempFocused=f;
        setBorder(f?
                TEMP_FOCUSED_BORDER:
                (focused?FOCUSED_BORDER:DEFAULT_BORDER));

        info_popup.setVisible(f);
    }

    private static class InfoPopup extends JFrame {
        private final Component parent;

        // gui components
        private final JLabel text_label=new JLabel();

        public InfoPopup (Component parent) {
            this.parent = parent;
            setFocusable(false);
            setUndecorated(true);
            setLocationRelativeTo(parent);
            add(text_label);
        }

        public InfoPopup (Component parent, String text) {
            this(parent);
            setText(text);
        }

        public void setText (String text) {
            text_label.setText(text);
            pack();
        }

        @Override
        public void setVisible (boolean v) {
            super.setVisible(v);
            if (v) {
                Point locationOnScreen=Optional.ofNullable(parent.getLocationOnScreen()).orElse(new Point());
                Point mouseLocation=Optional.ofNullable(parent.getMousePosition()).orElse(new Point());
                Point targetLocation=new Point(locationOnScreen.x+mouseLocation.x, locationOnScreen.y+mouseLocation.y);
                setLocation(targetLocation);
            }
        }
    };

    public void setDefaultState(boolean v) {
        nameLabel.setVisible(v);
        completeButton.setVisible(v);
        descLabel.setVisible(!v);
        setPreferredSize(v?DEFAULT_SIZE:EXPANDED_SIZE);
    }

    public void setFocused(boolean b) {
        setBorder(b?FOCUSED_BORDER:DEFAULT_BORDER);
        focused = b;
        if (!b) {
            info_popup.setVisible(false);
        }
    }

    public void getFocus () {
        manager.focusTask(this);
    }

}
