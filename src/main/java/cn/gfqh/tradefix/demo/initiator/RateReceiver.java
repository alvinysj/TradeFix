package cn.gfqh.tradefix.demo.initiator;


import cn.gfqh.tradefix.common.LoggerWapper;
import cn.gfqh.tradefix.common.MessageCreateFactory;
import quickfix.Message;
import quickfix.field.MDEntryPx;
import quickfix.field.MDEntryType;
import quickfix.field.NoMDEntries;
import quickfix.field.Symbol;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.component.Instrument;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Alvin on 2017/6/19.
 */
public class RateReceiver implements Runnable {
    private final static BlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(1000);

    public void run() {
        try {
            while(true) {

                MarketDataSnapshotFullRefresh marketData = MessageCreateFactory.cast(queue.take(),
                        MarketDataSnapshotFullRefresh.class);

                Instrument instrument   = MessageCreateFactory.createMessage(Instrument.class);

                //String reqID = marketData.getString(MDReqID.FIELD);
                String symbol = marketData.get(instrument).getString(Symbol.FIELD);
                int size = marketData.getInt(NoMDEntries.FIELD);

                BigDecimal offer = new BigDecimal(0);
                BigDecimal bid = new BigDecimal(0);

                for(int i=1;i<=size;i++) {
                    MarketDataSnapshotFullRefresh.NoMDEntries entries =
                            new MarketDataSnapshotFullRefresh.NoMDEntries();
                    marketData.getGroup(i, entries);

                    char side = entries.getChar(MDEntryType.FIELD);

                    switch (side) {

                        case MDEntryType.OFFER:
                            offer = BigDecimal.valueOf(entries.getDouble(MDEntryPx.FIELD));
                            break;

                        case MDEntryType.BID:
                            bid = BigDecimal.valueOf(entries.getDouble(MDEntryPx.FIELD));
                            break;

                        default:
                            break;
                    }
                }

                LoggerWapper.info("Symbol -> " + symbol + " | " + "OFFER -> " +
                        offer.toPlainString() + " | " + "BID -> " + bid.toPlainString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BlockingQueue<Message> getQueue() {
        return queue;
    }
}
