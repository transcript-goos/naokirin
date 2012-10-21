package main.groovy.auctionsniper.ui

import javax.swing.JFrame
import static main.groovy.auctionsniper.Main.MAIN_WINDOW_NAME

class MainWindow extends JFrame {
    MainWindow () {
        super('Auction Sniper')
        setName(MAIN_WINDOW_NAME)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        setVisible(true)
    }
}
