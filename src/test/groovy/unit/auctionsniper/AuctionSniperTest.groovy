package test.groovy.unit.auctionsniper

import spock.lang.Specification
import main.groovy.auctionsniper.SniperListener
import main.groovy.auctionsniper.AuctionSniper
import main.groovy.auctionsniper.Auction
import main.groovy.auctionsniper.AuctionEventListener
import static main.groovy.auctionsniper.AuctionEventListener.PriceSource.*

class AuctionSniperTest extends Specification {
    private final def auction = Mock(Auction)
    private final def sniperListener = Mock(SniperListener)
    private final def sniper = new AuctionSniper(auction, sniperListener)

    def "reports lost when auction closes immediately"() {
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
        sniper.currentPrice(price, increment, FromOtherBidder)

        then:
        1 * auction.bid(price + increment)
        (1.._) * sniperListener.sniperBidding()
    }

    def "reports is winning when current price comes from sniper"() {
        when:
        sniper.currentPrice(123, 45, FromSniper)

        then:
        (1.._) * sniperListener.sniperWinning()
    }

    def "reports lost if auction closes when bidding"() {
        when:
        sniper.currentPrice(123, 45, FromOtherBidder)
        sniper.auctionClosed()
        _ * auction._()

        then:
        _ * sniperListener.sniperBidding()
        (1.._) * sniperListener.sniperLost()
    }

    def "reports won if auction closes when winning"() {
        when:
        sniper.currentPrice(123, 45, FromSniper)
        sniper.auctionClosed()
        _ * auction._()

        then:
        _ * sniperListener.sniperWinning()
        (1.._) * sniperListener.sniperWon()

    }
}
