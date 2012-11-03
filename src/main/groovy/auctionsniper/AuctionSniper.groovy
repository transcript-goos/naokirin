package main.groovy.auctionsniper

import main.groovy.auctionsniper.AuctionEventListener.PriceSource

class AuctionSniper implements AuctionEventListener {
    private final Auction auction
    private final SniperListener sniperListener

    AuctionSniper(auction, sniperListener) {
        this.auction = auction
        this.sniperListener = sniperListener
    }

    @Override
    void auctionClosed() {
        sniperListener.sniperLost()
    }

    @Override
    void currentPrice(int price, int increment, PriceSource priceSource) {
        auction.bid(price + increment)
        sniperListener.sniperBidding()
    }
}
