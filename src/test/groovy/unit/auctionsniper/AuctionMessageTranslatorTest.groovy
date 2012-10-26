package test.groovy.unit.auctionsniper

import spock.lang.*

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.packet.Message

import main.groovy.auctionsniper.AuctionMessageTranslator
import main.groovy.auctionsniper.AuctionEventListener


class AuctionMessageTranslatorTest extends Specification {

    static final Chat UNUSED_CHAT = null
    private final def listener = Mock(AuctionEventListener)
    private final def translator = new AuctionMessageTranslator(listener)

    def "notifies auction closed when close message received"() {
        given:
        def message = new Message()
        message.body = 'SOLVersion: 1.1; Event: CLOSE;'

        when:
        translator.processMessage(UNUSED_CHAT, message)

        then:
        1 * listener.auctionClosed()
    }
}
