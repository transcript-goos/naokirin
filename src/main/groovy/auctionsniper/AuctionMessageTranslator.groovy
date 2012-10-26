package main.groovy.auctionsniper

import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.packet.Message

class AuctionMessageTranslator implements MessageListener {
    private AuctionEventListener listener

    AuctionMessageTranslator(listener) {
        this.listener = listener
    }

    @Override
    void processMessage(Chat chat, Message message) {
        def event = unpackEventFrom(message)

        def type = event.get('Event')
        if ('CLOSE' == type) {
            listener.auctionClosed()
        }
        else if ('PRICE' == type) {
            listener.currentPrice(Integer.parseInt(event.get('CurrentPrice')),
                Integer.parseInt(event.get('Increment')))
        }
    }

    private def unpackEventFrom(Message message) {
        def event = new HashMap()
        message.body.split(';').each {
            def pair = it.split(':')
            event.put(pair[0].trim(), pair[1].trim())
        }

        return event
    }
}
