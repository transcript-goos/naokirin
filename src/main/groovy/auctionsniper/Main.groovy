package main.groovy.auctionsniper

import javax.swing.SwingUtilities

class Main {
    private MainWindow ui

    static final MAIN_WINDOW_NAME = null
    static final SNIPER_STATUS_NAME = null

    Main () {
        startUserInterface()
    }

    static void main(String... args) {
        Main main = new Main()
    }

    private void startUserInterface() {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            void run() {
                ui = new MainWindow()
            }
        })
    }
}
