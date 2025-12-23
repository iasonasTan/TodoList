package com.app.main;

import com.app.lib.comp.EditText;
import com.app.lib.theme.Theme;
import com.app.storage.DataStorage;
import com.app.task.TaskData;
import com.app.task.TaskManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;
import java.net.URL;

public final class MainPanel extends JPanel { // context
    private TaskManager taskManager;

    // gui
    private JFrame frame;
    private final JButton addTaskButton=new JButton("add"),completeTaskButton=new JButton("complete"),
            swapTaskDescButton=new JButton("swap desc"),exitButton=new JButton("exit"),
            showSettingsButton=new JButton("settings");
    private final EditText taskNameInput=new EditText("name...", 6),taskDescInput=new EditText("desc...", 10);

    public MainPanel () {
        initFrame();

        try {
            initUI();
        } catch (Exception e) {
            System.out.println("Cannot start application");
            e.printStackTrace();
            System.exit(3);
        }
        addComponents();
        addListeners();
        restoreProperties(DataStorage.loadProperties());

        frame.setVisible(true);
    }

    private void initUI () throws Exception {
        interface IconManager {
            void setIcon(JButton button, URL iconSource) throws Exception;
        }
        final IconManager im=new IconManager() {
            @Override
            public void setIcon(JButton button, URL source) throws Exception {
                try {
                    BufferedImage icon_buff = ImageIO.read(source);
                    Icon bi = new ImageIcon(icon_buff.getScaledInstance(20, 20, 0));
                    button.setIcon(bi);
                } catch (Exception e) {
                    System.out.println("unknown source: "+source);
                    throw new Exception(e);
                }
            }
        };

        final Dimension inputSize=new Dimension(100,30);
        taskNameInput.setPreferredSize(inputSize);
        taskDescInput.setPreferredSize(inputSize);
        taskManager=new TaskManager();

        im.setIcon(addTaskButton, getClass().getResource("/icons/add.png"));
        im.setIcon(exitButton, getClass().getResource("/icons/exit.png"));
        im.setIcon(showSettingsButton, getClass().getResource("/icons/settings.png"));
        im.setIcon(swapTaskDescButton, getClass().getResource("/icons/show_info.png"));
        im.setIcon(completeTaskButton, getClass().getResource("/icons/task_complete.png"));
        completeTaskButton.setBackground(Color.GREEN);
    }

    private void initFrame () {
        frame = new JFrame("Todo List");
        frame.setContentPane(this);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        ImageIcon icon=new ImageIcon(getClass().getResource("/icons/frame_icon.png"));
        frame.setIconImage(icon.getImage());
    }

    private void restoreProperties(Properties properties) {
        final String theme = properties.getProperty("theme");
        if (theme.equals("light")) {
            setTheme(Theme.LIGHT);
        } else if (theme.equals("dark")) {
            setTheme(Theme.DARK);
        }

        final String width_str = properties.getProperty("win-width");
        final String height_str = properties.getProperty("win-height");
        final int WIDTH = Integer.parseInt(width_str);
        final int HEIGHT = Integer.parseInt(height_str);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.pack();

        taskManager.setPreferredSize(getPreferredSize());
        revalidate();
        repaint();
    }

    private void setTheme (Theme theme) {
        this.setBackground(theme.DARK_COLOR);
        for (Component c: getComponents()) {
            if (c instanceof JButton) {
                c.setBackground(theme.DARK_COLOR);
            } else {
                c.setBackground(theme.LIGHT_COLOR);
            }
            c.setForeground(theme.FOREGROUND);
        }
    }

    public void addTask (ActionEvent ae) {
        String taskName = taskNameInput.getText();
        String taskDesc = taskDescInput.getText();

        if (!taskName.isBlank()) {
            taskManager.addTask(new TaskData(taskName, taskDesc.isBlank() ? "[blank]" : taskDesc));
            taskNameInput.setText("");
            taskDescInput.setText("");
        }
    }

    private void addListeners () {
        final FocusedTaskChangeHandler ftch = new FocusedTaskChangeHandler();
        for (Component c: getComponents()) {
            c.addKeyListener(ftch);
        }

        completeTaskButton.addActionListener(ae -> taskManager.clickFocusedTask());
        swapTaskDescButton.addActionListener(ae -> taskManager.swapFocusedTaskDesc());

        showSettingsButton.addActionListener(ae -> {
            new PropertiesDialog(this);
        });

        addTaskButton.addActionListener(this::addTask);

        taskNameInput.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    taskDescInput.requestFocus();
                }
            }

            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });

        taskDescInput.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    taskNameInput.requestFocus();
                    addTask(null);
                }
            }

            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });

        exitButton.addActionListener(ae -> {
            taskManager.saveTasks();
            System.exit(0);
        });
    }

    public void updateProperties(Properties nextProperties) {
        restoreProperties(nextProperties);
        DataStorage.writeProperties(nextProperties);
    }

    private class FocusedTaskChangeHandler implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            int kc = e.getKeyCode();
            switch (kc) {
                case KeyEvent.VK_UP: taskManager.moveFocus(-1); break;
                case KeyEvent.VK_DOWN: taskManager.moveFocus(+1); break;
                case KeyEvent.VK_LEFT: taskManager.moveFocusOneCol(-1); break;
                case KeyEvent.VK_RIGHT: taskManager.moveFocusOneCol(+1); break;
            }
        }
        @Override public void keyTyped(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }

    private void addComponents () {
        add(exitButton);
        add(showSettingsButton);
        add(completeTaskButton);
        add(addTaskButton);
        add(swapTaskDescButton);
        add(taskNameInput);
        add(taskDescInput);
        add(taskManager);
    }

}
