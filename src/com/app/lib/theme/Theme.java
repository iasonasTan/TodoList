package com.app.lib.theme;

import java.awt.Color;

public enum Theme {
    DARK(Color.DARK_GRAY, Color.GRAY, Color.WHITE),
    LIGHT(Color.WHITE, Color.LIGHT_GRAY, Color.DARK_GRAY);

    public final Color DARK_COLOR;
    public final Color LIGHT_COLOR;
    public final Color FOREGROUND;

    Theme(Color darkColor, Color lightColor, Color foreground) {
        DARK_COLOR = darkColor;
        LIGHT_COLOR = lightColor;
        FOREGROUND = foreground;
    }

}
