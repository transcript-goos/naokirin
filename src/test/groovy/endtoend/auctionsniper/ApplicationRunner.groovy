package test.groovy.endtoend.auctionsniper

import static test.groovy.endtoend.auctionsniper.FakeAuctionServer.XMPP_HOSTNAME
import static main.groovy.auctionsniper.ui.MainWindow.*

import main.groovy.auctionsniper.Main

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

    void stop() {
            driver?.dispose()
    }

    void showsSniperHasLostAuction() {
        driver.showsSniperStatus(STATUS_LOST)
    }

    void hasShownSniperIsBidding() {
       driver.showsSniperStatus(STATUS_BIDDING)
    }

    void hasShownSniperIsWinning() {
        driver.showsSniperStatus(STATUS_WINNING)
    }

    void showsSniperHasWonAuction() {
        driver.showsSniperStatus(STATUS_WINNING)
    }
}
