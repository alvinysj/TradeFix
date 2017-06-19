package cn.gfqh.tradefix.demo.acceptor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.gfqh.tradefix.common.MessageCreateFactory;
import quickfix.Message;
import quickfix.field.MDEntryPx;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.NoMDEntries;
import quickfix.field.QuoteEntryID;
import quickfix.field.Symbol;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.component.Instrument;

/**
 * Created by Alvin on 2017/6/19.
 */
public class RateGenerater {
    private final String MIN_KEY = "_MIN";
    private final String MAX_KEY = "_MAX";

    private final Map<String,Integer> rateRange = new HashMap<String,Integer>() {
        private static final long serialVersionUID = 1475369029118134623L;
        {
            // USD/JPY Rate range
            put("USD/JPY"+MIN_KEY,80);
            put("USD/JPY"+MAX_KEY,83);

            // USD/CHF Rate range
            put("USD/CHF"+MIN_KEY,1);
            put("USD/CHF"+MAX_KEY,10);

            // EUR/JPY Rate range
            put("EUR/JPY"+MIN_KEY,100);
            put("EUR/JPY"+MAX_KEY,120);

            //****************************//

            // XAU/USD Rate range
            put("XAU/USD"+MIN_KEY,1500);
            put("XAU/USD"+MAX_KEY,1900);

            // XAG/USD Rate range
            put("XAG/USD"+MIN_KEY,40);
            put("XAG/USD"+MAX_KEY,80);

            // OIL/USD Rate range
            put("OIL/USD"+MIN_KEY,13000);
            put("OIL/USD"+MAX_KEY,18000);


        }
    };

    private MDReqID mdReqID = null;
    private Symbol symbol = null;
    private BigDecimal currentVal = new BigDecimal(0);
    private int direction = 1;

    public RateGenerater(MDReqID mdReqID, Symbol symbol) {
        this.mdReqID = mdReqID;
        this.symbol = symbol;
    }

    public Message generate() {
        Integer minRange = rateRange.get(symbol.getValue()+MIN_KEY);
        Integer maxRange = rateRange.get(symbol.getValue()+MAX_KEY);

        if (currentVal.intValue() == 0) {
            currentVal = currentVal.add(new BigDecimal(minRange));
        }

        //direction=1为正向加，direction=2为反向减

        if (currentVal.compareTo(new BigDecimal(minRange)) <= 0) {
            direction = 1;
        } else if (currentVal.compareTo(new BigDecimal(maxRange)) > 0) {
            direction = 2;
        }

        if (direction == 1) {
            currentVal = currentVal.add(new BigDecimal(0.1));
        } else if (direction == 2) {
            currentVal = currentVal.subtract(new BigDecimal(0.1));
        }
        BigDecimal ask = currentVal.add(new BigDecimal(1));
        BigDecimal bid = currentVal;

        MarketDataSnapshotFullRefresh message = MessageCreateFactory.createMessage(MarketDataSnapshotFullRefresh.class);
        message.set(mdReqID);
        message.set(new Instrument(symbol));
        message.set(new NoMDEntries(2));

        MarketDataSnapshotFullRefresh.NoMDEntries noMDEntriesGroup = new MarketDataSnapshotFullRefresh.NoMDEntries();

        noMDEntriesGroup.set(new MDEntryType( MDEntryType.BID));
        noMDEntriesGroup.set(new MDEntryPx(bid.doubleValue()));
        noMDEntriesGroup.set(new QuoteEntryID(UUID.randomUUID().toString()));
        message.addGroup(noMDEntriesGroup);

        noMDEntriesGroup.set(new MDEntryType( MDEntryType.OFFER));
        noMDEntriesGroup.set(new MDEntryPx(ask.doubleValue()));
        noMDEntriesGroup.set(new QuoteEntryID(UUID.randomUUID().toString()));
        message.addGroup(noMDEntriesGroup);

        return message;

    }
}
