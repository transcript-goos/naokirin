package test.groovy.endtoend.auctionsniper

import spock.lang.Specification

class AuctionSniperEndToEndTest extends Specification {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321")
    private final ApplicationRunner application = new ApplicationRunner()

    def "Sniper joins auction until auction closes"() {
        when:
        auction.startSellingItem()
        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper()
        auction.announceClosed()
        application.showsSniperHasLostAuction()

        then:
        notThrown(Exception)
    }

    def cleanup() {
        auction.stop()
        application.stop()
    }
}

