package com.eliaswalyba.gameoflife.frontend;

import com.eliaswalyba.gameoflife.backend.Config;
import com.eliaswalyba.gameoflife.backend.Controller;
import com.eliaswalyba.gameoflife.frontend.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame {

    public Window(String title, int w, int h) {
        this.setTitle(title);
        this.setSize(w, h);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        Grid grid = new Grid();
        JButton _nextGeneration_ = new JButton(Config.NEXT_GENERATION_BUTTON_TITLE);
        this.add(_nextGeneration_, BorderLayout.SOUTH);
        this.add(grid, BorderLayout.CENTER);
        this.setVisible(true);
        _nextGeneration_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int[][] states = grid.getStates();
                boolean noPatternDrawn = true;
                for (int row[] : states) for (int cell : row)
                    if (cell != 0) noPatternDrawn = false;
                if (noPatternDrawn) {
                    JOptionPane.showMessageDialog(null, Config.EMPTY_PATTERN_ALERT_MESSAGE);
                } else {
                    Controller controller = new Controller(states);
                    controller.nextGeneration();
                    grid.updateGrid(controller.getCells());
                }
            }
        });
    }

}
