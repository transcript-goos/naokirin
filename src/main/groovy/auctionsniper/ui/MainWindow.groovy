package main.groovy.auctionsniper.ui

import javax.swing.JFrame
import static main.groovy.auctionsniper.Main.MAIN_WINDOW_NAME
import javax.swing.JLabel
import javax.swing.border.LineBorder
import java.awt.Color

class MainWindow extends JFrame {

    static final String SNIPER_STATUS_NAME = 'sniper status'
    static final String STATUS_JOINING = 'Joining'
    static final String STATUS_LOST = 'Lost'
    private JLabel sniperStatus = createLabel(STATUS_JOINING)

    MainWindow () {
        super('Auction Sniper')
        setName(MAIN_WINDOW_NAME)
        add(sniperStatus)
        pack()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        setVisible(true)
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText)
        result.setName(SNIPER_STATUS_NAME)
        result.setBorder(new LineBorder(Color.BLACK))
        return result
    }

    void showStatus(String status) {
        sniperStatus.setText(status)
    }
}
