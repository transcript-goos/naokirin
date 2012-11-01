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
        def event = AuctionEvent.from(message.body)
        def eventType = event.type()

        if ('CLOSE' == eventType) {
            listener.auctionClosed()
        }
        else if ('PRICE' == eventType) {
            listener.currentPrice(event.currentPrice(), event.increment())
        }
    }

    private static class AuctionEvent {
        private final def fields = new HashMap<String, String>()
        String type() { return get('Event') }
        int currentPrice() { return getInt('CurrentPrice') }
        int increment() {return getInt('Increment') }

        private int getInt(String fieldName) {
            return Integer.parseInt(get(fieldName))
        }

        private String get(String fieldName) { return fields.get(fieldName) }

        private void addField(String field) {
            def pair = field.split(':')
            fields.put(pair[0].trim(), pair[1].trim())
        }

        static AuctionEvent from(String messageBody) {
            def event = new AuctionEvent()
            fieldsIn(messageBody).each { field ->
                event.addField(field)
            }
            return event
        }

        static String[] fieldsIn(String messageBody) {
            return messageBody.split(';')
        }
    }
}
