package test.groovy.endtoend.auctionsniper

import static test.groovy.endtoend.auctionsniper.FakeAuctionServer.XMPP_HOSTNAME
import static main.groovy.auctionsniper.ui.MainWindow.STATUS_JOINING
import static main.groovy.auctionsniper.ui.MainWindow.STATUS_LOST

import main.groovy.auctionsniper.Main
import main.groovy.auctionsniper.ui.MainWindow

class ApplicationRunner {
    static final String SNIPER_ID = 'sniper'
    static final String SNIPER_PASSWORD = 'sniper'
    static final String SNIPER_XMPP_ID = "$SNIPER_ID@$XMPP_HOSTNAME/Auction"

    private AuctionSniperDriver driver

    void startBiddingIn(final FakeAuctionServer auction) {
        Thread thread = new Thread('Test Application') {
            @Override void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId())
                } catch (Exception e) {
                    e.printStackTrace()
                }
            }
        }
        thread.setDaemon(true)
        thread.start()
        driver = new AuctionSniperDriver(1000)
        driver.showsSniperStatus(STATUS_JOINING)
    }

    void showsSniperHasLostAuction() {
        driver.showsSniperStatus(STATUS_LOST)
    }

    void stop() {
            driver?.dispose()
    }

   void hasShownSniperIsBidding() {
       driver.showsSniperStatus(MainWindow.STATUS_BIDDING)
    }
}
