package test.groovy.endtoend.auctionsniper

import static org.hamcrest.CoreMatchers.equalTo

import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.gesture.GesturePerformer
import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.JLabelDriver
import main.groovy.auctionsniper.Main
import main.groovy.auctionsniper.ui.MainWindow

class AuctionSniperDriver extends JFrameDriver {
    AuctionSniperDriver(int timeoutMillis) {
        super(new GesturePerformer(),
            JFrameDriver.topLevelFrame(
                named(Main.MAIN_WINDOW_NAME),
                showingOnScreen()),
                new AWTEventQueueProber(timeoutMillis, 100))
    }

    void showsSniperStatus(String statusText) {
        new JLabelDriver(
                this, named(MainWindow.SNIPER_STATUS_NAME)).hasText(equalTo(statusText))
    }
}

