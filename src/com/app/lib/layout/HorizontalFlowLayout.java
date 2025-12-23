package com.app.lib.layout;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public final class HorizontalFlowLayout implements LayoutManager2 {
    private ArrayList<Component> components = new ArrayList<>();
    private int vGap = 0; // vertical
    private int hGap = 0; // horizontal

    public HorizontalFlowLayout() {
    }

    public HorizontalFlowLayout (int hGap, int vGap) {
        this.hGap = hGap;
        this.vGap = vGap;
    }

    public void setVerticalGap (int vg) {
        this.vGap = vg;
    }

    public void setHorizontalGap (int hg) {
        this.hGap = hg;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        components.add(comp);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return null;
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {

    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        components.add(comp);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        components.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    @Override
    public void layoutContainer(Container parent) {
        int x = hGap;
        int y = vGap;

        int maxHeight = 0;

        Dimension parentSize = parent.getPreferredSize();

        for (Component c: components) {
            if (!c.isVisible()) {
                continue;
            }

            Dimension componentSize = c.getPreferredSize();
            if (maxHeight < componentSize.height) {
                maxHeight = componentSize.height;
            }

            c.setBounds(x, y, componentSize.width, componentSize.height);

            x+=componentSize.width+hGap;

            if (x+componentSize.width > parentSize.width) {
                x = hGap;
                y+=vGap+maxHeight;
                maxHeight = hGap;
            }

        }

    }
}
