package test.groovy.endtoend.auctionsniper

import spock.lang.Specification

class AuctionSniperEndToEndTest extends Specification {
    private final FakeAuctionServer auction = new FakeAuctionServer('item-54321')
    private final ApplicationRunner application = new ApplicationRunner()

    def "Sniper joins auction until auction closes"() {
        when:
        auction.startSellingItem()
        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID)
        auction.announceClosed()
        application.showsSniperHasLostAuction()

        then:
        notThrown(Exception)
    }

    def "Sniper makes a higher bid but loses"() {
        given:
        auction.startSellingItem()

        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, 'other bidder')

        when:
        application.hasShownSniperIsBidding()
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID)

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

