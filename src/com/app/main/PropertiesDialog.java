package com.app.main;

import com.app.lib.comp.Slider;
import com.app.lib.layout.VerticalFlowLayout;
import com.app.storage.DataStorage;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class PropertiesDialog extends JFrame {
    private MainPanel mainPanel;

    // gui
    private final JPanel mainGuiPanel=new JPanel(new FlowLayout());
    private final JCheckBox darkThemeCheckBox=new JCheckBox("dark theme");
    private final Slider widthInput=new Slider(1000, 100, "win-width"),
            heightInput=new Slider(1000, 100, "win-height");
    private final JButton saveAndExitButton=new JButton("save");

    public PropertiesDialog (MainPanel mp) {
        this.mainPanel = mp;

        initUI();
        restoreState(DataStorage.loadProperties());
        addComponents();
        setLocationRelativeTo(mp);
        pack();
        setVisible(true);
    }

    private void restoreState(Properties currentProperties) {
        darkThemeCheckBox.setSelected(currentProperties.getProperty("theme").equals("dark"));
        widthInput.setValue(Integer.parseInt(currentProperties.getProperty("win-width")));
        heightInput.setValue(Integer.parseInt(currentProperties.getProperty("win-height")));
    }

    private void initUI() {
        mainGuiPanel.setPreferredSize(new Dimension(200,200));
        mainGuiPanel.setLayout(new VerticalFlowLayout());
        setContentPane(mainGuiPanel);
        saveAndExitButton.addActionListener(ae -> saveAndExit());
    }

    private void addComponents() {
        mainGuiPanel.add(darkThemeCheckBox);
        mainGuiPanel.add(widthInput);
        mainGuiPanel.add(heightInput);
        mainGuiPanel.add(saveAndExitButton);
    }

    private void saveAndExit() {
        Properties nextProperties=new Properties();
        nextProperties.setProperty("theme", darkThemeCheckBox.isSelected()?"dark":"light");
        nextProperties.setProperty("win-width", widthInput.getValue()+"");
        nextProperties.setProperty("win-height", heightInput.getValue()+"");

        dispose();
        mainPanel.updateProperties(nextProperties);
    }

}
