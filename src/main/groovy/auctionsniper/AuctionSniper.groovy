package main.groovy.auctionsniper

import main.groovy.auctionsniper.AuctionEventListener.PriceSource
import static main.groovy.auctionsniper.AuctionEventListener.PriceSource.*

class AuctionSniper implements AuctionEventListener {
    private final Auction auction
    private final SniperListener sniperListener
    private boolean isWinning = false

    AuctionSniper(auction, sniperListener) {
        this.auction = auction
        this.sniperListener = sniperListener
    }

    @Override
    void auctionClosed() {
        if (isWinning) {
            sniperListener.sniperWon()
        } else {
            sniperListener.sniperLost()
        }
    }

    @Override
    void currentPrice(int price, int increment, PriceSource priceSource) {
        isWinning = priceSource == PriceSource.FromSniper
        if (isWinning) {
            sniperListener.sniperWinning()
        } else {
            auction.bid(price + increment)
            sniperListener.sniperBidding()
        }
    }
}
