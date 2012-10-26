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
        listener.auctionClosed()
    }
}
