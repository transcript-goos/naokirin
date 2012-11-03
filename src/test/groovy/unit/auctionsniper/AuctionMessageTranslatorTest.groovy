package test.groovy.unit.auctionsniper

import spock.lang.*

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.packet.Message

import main.groovy.auctionsniper.AuctionMessageTranslator
import main.groovy.auctionsniper.AuctionEventListener
import main.groovy.auctionsniper.AuctionEventListener.PriceSource
import test.groovy.endtoend.auctionsniper.ApplicationRunner
import static test.groovy.endtoend.auctionsniper.ApplicationRunner.SNIPER_ID

class AuctionMessageTranslatorTest extends Specification {

    static final Chat UNUSED_CHAT = null
    private final def listener = Mock(AuctionEventListener)
    private final def translator = new AuctionMessageTranslator(ApplicationRunner.SNIPER_ID, listener)

    def "notifies auction closed when close message received"() {
        given:
        def message = new Message()
        message.body = 'SOLVersion: 1.1; Event: CLOSE;'

        when:
        translator.processMessage(UNUSED_CHAT, message)

        then:
        1 * listener.auctionClosed()
    }

    def "notifies bid details when current price message received from other bidder"() {
        given:
        def message = new Message()
        message.body = 'SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;'

        when:
        translator.processMessage(UNUSED_CHAT, message)

        then:
        1 * listener.currentPrice(192, 7, PriceSource.FromOtherBidder)
    }

    def "notifies bid details when current price message received from sniper"() {
        given:
        def message = new Message()
        message.body = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: $SNIPER_ID;"

        when:
        translator.processMessage(UNUSED_CHAT, message)

        then:
        1 * listener.currentPrice(234, 5, PriceSource.FromSniper)
    }
}
