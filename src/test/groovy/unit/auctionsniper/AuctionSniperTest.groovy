package test.groovy.unit.auctionsniper

import spock.lang.Specification
import main.groovy.auctionsniper.SniperListener
import main.groovy.auctionsniper.AuctionSniper
import main.groovy.auctionsniper.Auction

class AuctionSniperTest extends Specification {
    private final def auction = Mock(Auction)
    private final def sniperListener = Mock(SniperListener)
    private final def sniper = new AuctionSniper(auction, sniperListener)

    def "reports lost when auction closes"() {
        when:
        sniper.auctionClosed()

        then:
        1 * sniperListener.sniperLost()
    }

    def "bids higher and reports bidding when new price arrives"() {
        given:
        final def price = 1001
        final def increment = 25

        when:
        sniper.currentPrice(price, increment, null)

        then:
        1 * auction.bid(price + increment)
        (1.._) * sniperListener.sniperBidding()
    }
}
