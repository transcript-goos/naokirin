package test.groovy.unit.auctionsniper

import spock.lang.Specification
import main.groovy.auctionsniper.SniperListener
import main.groovy.auctionsniper.AuctionSniper

class AuctionSniperTest extends Specification {
    private final def sniperListener = Mock(SniperListener)
    private final def sniper = new AuctionSniper(sniperListener)

    def "reports lost when auction closes"() {
        when:
        sniper.auctionClosed()

        then:
        1 * sniperListener.sniperLost()
    }
}
