package main.groovy.auctionsniper

import javax.swing.SwingUtilities
import main.groovy.auctionsniper.ui.MainWindow

class Main {
    private MainWindow ui

    static final MAIN_WINDOW_NAME = 'Auction Sniper Main'

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
