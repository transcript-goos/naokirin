package main.groovy.auctionsniper

class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener

    AuctionSniper(sniperListener) {
        this.sniperListener = sniperListener
    }

    @Override
    void auctionClosed() {
        sniperListener.sniperLost()
    }

    @Override
    void currentPrice(int price, int increment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
