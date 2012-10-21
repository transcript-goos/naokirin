package test.groovy.endtoend.auctionsniper

import static test.groovy.endtoend.auctionsniper.FakeAuctionServer.XMPP_HOSTNAME
import main.groovy.auctionsniper.Main

class ApplicationRunner {
    static final String SNIPER_ID = "sniper"
    static final String SNIPER_PASSWORD = "sniper"
    static final String STATUS_JOINING = null
    static final String STATUS_LOST = null

    private AuctionSniperDriver driver

    void startBiddingIn(final FakeAuctionServer auction) {
        Thread thread = new Thread("Test Application") {
            @Override void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId())
                } catch (Exception e) {
                    e.printStackTrace()
                }
            }
        }
        thread.setDaemon((true))
        thread.start()
        driver = new AuctionSniperDriver(1000)
        driver.showsSniperStatus(STATUS_JOINING)
    }

    void showsSniperHasLostAuction() {
        driver.showsSniperStatus(STATUS_LOST)
    }

    void stop() {
        if (driver != null) {
            driver.dispose()
        }
    }
}
