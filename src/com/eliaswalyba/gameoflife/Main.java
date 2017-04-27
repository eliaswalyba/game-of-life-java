package com.eliaswalyba.gameoflife;

import com.eliaswalyba.gameoflife.backend.Config;
import com.eliaswalyba.gameoflife.frontend.Window;

public class Main {
    public static void main(String[] args) {
        new Window(
                Config.WINDOW_TITLE,
                Config.WINDOW_WIDTH,
                Config.WINDOW_HEIGHT
        );
    }
}