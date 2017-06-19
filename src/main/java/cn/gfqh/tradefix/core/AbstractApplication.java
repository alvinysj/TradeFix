package cn.gfqh.tradefix.core;

import cn.gfqh.tradefix.common.LoggerWapper;
import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.field.MsgType;

/**
 * Created by Alvin on 2017/6/19.
 */
public abstract class AbstractApplication implements Application {

    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound,
            IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        processMessage(message, sessionID, EventType.FROM_ADMIN);
    }

    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound,
            IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        processMessage(message, sessionID,EventType.FROM_APP);
    }

    public void onCreate(SessionID sessionID) {
        processOnCreate(sessionID);
    }

    public void onLogon(SessionID sessionID) {
        processOnLogon(sessionID);
    }

    public void onLogout(SessionID sessionID) {
        processOnLogout(sessionID);
    }

    public void toAdmin(Message message, SessionID sessionID) {
        try {
            processMessage(message, sessionID,EventType.TO_ADMIN);
        } catch (FieldNotFound e) {
            e.printStackTrace();
        }
    }

    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        try {
            processMessage(message, sessionID,EventType.TO_APP);
        } catch (FieldNotFound e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(Message message) {
        boolean sendStatus = false;
        try {
            sendStatus = Session.sendToTarget(message);
        } catch (SessionNotFound e) {
            LoggerWapper.error(e);
            sendStatus = false;
        }
        return sendStatus;
    }

    public boolean sendMessage(Message message, SessionID sessionID) {
        boolean sendStatus = false;
        try {
            sendStatus = Session.sendToTarget(message,sessionID);
        } catch (SessionNotFound e) {
            LoggerWapper.error(e.getMessage(), e.getCause());
            sendStatus = false;
        }
        return sendStatus;
    }

    private void processMessage(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound {
        String msgType = message.getHeader().getString(MsgType.FIELD);

        if(MsgType.HEARTBEAT.equals(msgType)) {
            processHeartbeat(message, sessionID, eventType); //--> 35=0
        } else if(MsgType.TEST_REQUEST.equals(msgType)) {
            processTestRequest(message, sessionID, eventType); //--> 35=1
        } else if(MsgType.RESEND_REQUEST.equals(msgType)) {
            processResendRequest(message, sessionID, eventType); //--> 35=2
        } else if(MsgType.REJECT.equals(msgType)) {
            processReject(message, sessionID, eventType); //--> 35=3
        } else if(MsgType.SEQUENCE_RESET.equals(msgType)) {
            processSequenceReset(message, sessionID, eventType); //--> 35=4
        } else if(MsgType.LOGOUT.equals(msgType)) {
            processLogout(message, sessionID, eventType); //--> 35=5
        } else if(MsgType.INDICATION_OF_INTEREST.equals(msgType)) {
            processIndicationOfInterest(message, sessionID, eventType); //--> 35=6
        } else if(MsgType.ADVERTISEMENT.equals(msgType)) {
            processAdvertisement(message, sessionID, eventType); //--> 35=7
        } else if(MsgType.EXECUTION_REPORT.equals(msgType)) {
            processExecutionReport(message, sessionID, eventType); //--> 35=8
        } else if(MsgType.ORDER_CANCEL_REJECT.equals(msgType)) {
            processOrderCancelReject(message, sessionID, eventType); //--> 35=9
        } else if(MsgType.LOGON.equals(msgType)) {
            processLogon(message, sessionID, eventType); //--> 35=A
        } else if(MsgType.NEWS.equals(msgType)) {
            processNews(message, sessionID, eventType); //--> 35=B
        } else if(MsgType.EMAIL.equals(msgType)) {
            processEmail(message, sessionID, eventType); //--> 35=C
        } else if(MsgType.ORDER_SINGLE.equals(msgType)) {
            processOrderSingle(message, sessionID, eventType); //--> 35=D
        } else if(MsgType.ORDER_LIST.equals(msgType)) {
            processOrderList(message, sessionID, eventType); //--> 35=E
        } else if(MsgType.ORDER_CANCEL_REQUEST.equals(msgType)) {
            processOrderCancelRequest(message, sessionID, eventType); //--> 35=F
        } else if(MsgType.ORDER_CANCEL_REPLACE_REQUEST.equals(msgType)) {
            processOrderCancelReplaceRequest(message, sessionID, eventType); //--> 35=G
        } else if(MsgType.ORDER_STATUS_REQUEST.equals(msgType)) {
            processOrderStatusRequest(message, sessionID, eventType); //--> 35=H
        } else if(MsgType.ALLOCATION_INSTRUCTION.equals(msgType)) {
            processAllocationInstruction(message, sessionID, eventType); //--> 35=J
        } else if(MsgType.LIST_CANCEL_REQUEST.equals(msgType)) {
            processListCancelRequest(message, sessionID, eventType); //--> 35=K
        } else if(MsgType.LIST_EXECUTE.equals(msgType)) {
            processListExecute(message, sessionID, eventType); //--> 35=L
        } else if(MsgType.LIST_STATUS_REQUEST.equals(msgType)) {
            processListStatusRequest(message, sessionID, eventType); //--> 35=M
        } else if(MsgType.LIST_STATUS.equals(msgType)) {
            processListStatus(message, sessionID, eventType); //--> 35=N
        } else if(MsgType.ALLOCATION_INSTRUCTION_ACK.equals(msgType)) {
            processAllocationInstructionAck(message, sessionID, eventType); //--> 35=P
        } else if(MsgType.DONT_KNOW_TRADE.equals(msgType)) {
            processDontKnowTrade(message, sessionID, eventType); //--> 35=Q
        } else if(MsgType.QUOTE_REQUEST.equals(msgType)) {
            processQuoteRequest(message, sessionID, eventType); //--> 35=R
        } else if(MsgType.QUOTE.equals(msgType)) {
            processQuote(message, sessionID, eventType); //--> 35=S
        } else if(MsgType.SETTLEMENT_INSTRUCTIONS.equals(msgType)) {
            processSettlementInstructions(message, sessionID, eventType); //--> 35=T
        } else if(MsgType.MARKET_DATA_REQUEST.equals(msgType)) {
            processMarketDataRequest(message, sessionID, eventType); //--> 35=V
        } else if(MsgType.MARKET_DATA_SNAPSHOT_FULL_REFRESH.equals(msgType)) {
            processMarketDataSnapshotFullRefresh(message, sessionID, eventType); //--> 35=W
        } else if(MsgType.MARKET_DATA_INCREMENTAL_REFRESH.equals(msgType)) {
            processMarketDataIncrementalRefresh(message, sessionID, eventType); //--> 35=X
        } else if(MsgType.MARKET_DATA_REQUEST_REJECT.equals(msgType)) {
            processMarketDataRequestReject(message, sessionID, eventType); //--> 35=Y
        } else if(MsgType.QUOTE_CANCEL.equals(msgType)) {
            processQuoteCancel(message, sessionID, eventType); //--> 35=Z
        } else if(MsgType.QUOTE_STATUS_REQUEST.equals(msgType)) {
            processQuoteStatusRequest(message, sessionID, eventType); //--> 35=a
        } else if(MsgType.MASS_QUOTE_ACKNOWLEDGEMENT.equals(msgType)) {
            processMassQuoteAcknowledgement(message, sessionID, eventType); //--> 35=b
        } else if(MsgType.SECURITY_DEFINITION_REQUEST.equals(msgType)) {
            processSecurityDefinitionRequest(message, sessionID, eventType); //--> 35=c
        } else if(MsgType.SECURITY_DEFINITION.equals(msgType)) {
            processSecurityDefinition(message, sessionID, eventType); //--> 35=d
        } else if(MsgType.SECURITY_STATUS_REQUEST.equals(msgType)) {
            processSecurityStatusRequest(message, sessionID, eventType); //--> 35=e
        } else if(MsgType.SECURITY_STATUS.equals(msgType)) {
            processSecurityStatus(message, sessionID, eventType); //--> 35=f
        } else if(MsgType.TRADING_SESSION_STATUS_REQUEST.equals(msgType)) {
            processTradingSessionStatusRequest(message, sessionID, eventType); //--> 35=g
        } else if(MsgType.TRADING_SESSION_STATUS.equals(msgType)) {
            processTradingSessionStatus(message, sessionID, eventType); //--> 35=h
        } else if(MsgType.MASS_QUOTE.equals(msgType)) {
            processMassQuote(message, sessionID, eventType); //--> 35=i
        } else if(MsgType.BUSINESS_MESSAGE_REJECT.equals(msgType)) {
            processBusinessMessageReject(message, sessionID, eventType); //--> 35=j
        } else if(MsgType.BID_REQUEST.equals(msgType)) {
            processBidRequest(message, sessionID, eventType); //--> 35=k
        } else if(MsgType.BID_RESPONSE.equals(msgType)) {
            processBidResponse(message, sessionID, eventType); //--> 35=l
        } else if(MsgType.LIST_STRIKE_PRICE.equals(msgType)) {
            processListStrikePrice(message, sessionID, eventType); //--> 35=m
        } else if(MsgType.XML_MESSAGE.equals(msgType)) {
            processXmlMessage(message, sessionID, eventType); //--> 35=n
        } else if(MsgType.REGISTRATION_INSTRUCTIONS.equals(msgType)) {
            processRegistrationInstructions(message, sessionID, eventType); //--> 35=o
        } else if(MsgType.REGISTRATION_INSTRUCTIONS_RESPONSE.equals(msgType)) {
            processRegistrationInstructionsResponse(message, sessionID, eventType); //--> 35=p
        } else if(MsgType.ORDER_MASS_CANCEL_REQUEST.equals(msgType)) {
            processOrderMassCancelRequest(message, sessionID, eventType); //--> 35=q
        } else if(MsgType.ORDER_MASS_CANCEL_REPORT.equals(msgType)) {
            processOrderMassCancelReport(message, sessionID, eventType); //--> 35=r
        } else if(MsgType.NEW_ORDER_CROSS.equals(msgType)) {
            processNewOrderCross(message, sessionID, eventType); //--> 35=s
        } else if(MsgType.CROSS_ORDER_CANCEL_REPLACE_REQUEST.equals(msgType)) {
            processCrossOrderCancelReplaceRequest(message, sessionID, eventType); //--> 35=t
        } else if(MsgType.CROSS_ORDER_CANCEL_REQUEST.equals(msgType)) {
            processCrossOrderCancelRequest(message, sessionID, eventType); //--> 35=u
        } else if(MsgType.SECURITY_TYPE_REQUEST.equals(msgType)) {
            processSecurityTypeRequest(message, sessionID, eventType); //--> 35=v
        } else if(MsgType.SECURITY_TYPES.equals(msgType)) {
            processSecurityTypes(message, sessionID, eventType); //--> 35=w
        } else if(MsgType.SECURITY_LIST_REQUEST.equals(msgType)) {
            processSecurityListRequest(message, sessionID, eventType); //--> 35=x
        } else if(MsgType.SECURITY_LIST.equals(msgType)) {
            processSecurityList(message, sessionID, eventType); //--> 35=y
        } else if(MsgType.DERIVATIVE_SECURITY_LIST_REQUEST.equals(msgType)) {
            processDerivativeSecurityListRequest(message, sessionID, eventType); //--> 35=z
        } else if(MsgType.DERIVATIVE_SECURITY_LIST.equals(msgType)) {
            processDerivativeSecurityList(message, sessionID, eventType); //--> 35=AA
        } else if(MsgType.NEW_ORDER_MULTILEG.equals(msgType)) {
            processNewOrderMultileg(message, sessionID, eventType); //--> 35=AB
        } else if(MsgType.MULTILEG_ORDER_CANCEL_REPLACE.equals(msgType)) {
            processMultilegOrderCancelReplace(message, sessionID, eventType); //--> 35=AC
        } else if(MsgType.TRADE_CAPTURE_REPORT_REQUEST.equals(msgType)) {
            processTradeCaptureReportRequest(message, sessionID, eventType); //--> 35=AD
        } else if(MsgType.TRADE_CAPTURE_REPORT.equals(msgType)) {
            processTradeCaptureReport(message, sessionID, eventType); //--> 35=AE
        } else if(MsgType.ORDER_MASS_STATUS_REQUEST.equals(msgType)) {
            processOrderMassStatusRequest(message, sessionID, eventType); //--> 35=AF
        } else if(MsgType.QUOTE_REQUEST_REJECT.equals(msgType)) {
            processQuoteRequestReject(message, sessionID, eventType); //--> 35=AG
        } else if(MsgType.RFQ_REQUEST.equals(msgType)) {
            processRfqRequest(message, sessionID, eventType); //--> 35=AH
        } else if(MsgType.QUOTE_STATUS_REPORT.equals(msgType)) {
            processQuoteStatusReport(message, sessionID, eventType); //--> 35=AI
        } else if(MsgType.QUOTE_RESPONSE.equals(msgType)) {
            processQuoteResponse(message, sessionID, eventType); //--> 35=AJ
        } else if(MsgType.CONFIRMATION.equals(msgType)) {
            processConfirmation(message, sessionID, eventType); //--> 35=AK
        } else if(MsgType.POSITION_MAINTENANCE_REQUEST.equals(msgType)) {
            processPositionMaintenanceRequest(message, sessionID, eventType); //--> 35=AL
        } else if(MsgType.POSITION_MAINTENANCE_REPORT.equals(msgType)) {
            processPositionMaintenanceReport(message, sessionID, eventType); //--> 35=AM
        } else if(MsgType.REQUEST_FOR_POSITIONS.equals(msgType)) {
            processRequestForPositions(message, sessionID, eventType); //--> 35=AN
        } else if(MsgType.REQUEST_FOR_POSITIONS_ACK.equals(msgType)) {
            processRequestForPositionsAck(message, sessionID, eventType); //--> 35=AO
        } else if(MsgType.POSITION_REPORT.equals(msgType)) {
            processPositionReport(message, sessionID, eventType); //--> 35=AP
        } else if(MsgType.TRADE_CAPTURE_REPORT_REQUEST_ACK.equals(msgType)) {
            processTradeCaptureReportRequestAck(message, sessionID, eventType); //--> 35=AQ
        } else if(MsgType.TRADE_CAPTURE_REPORT_ACK.equals(msgType)) {
            processTradeCaptureReportAck(message, sessionID, eventType); //--> 35=AR
        } else if(MsgType.ALLOCATION_REPORT.equals(msgType)) {
            processAllocationReport(message, sessionID, eventType); //--> 35=AS
        } else if(MsgType.ALLOCATION_REPORT_ACK.equals(msgType)) {
            processAllocationReportAck(message, sessionID, eventType); //--> 35=AT
        } else if(MsgType.CONFIRMATION_ACK.equals(msgType)) {
            processConfirmationAck(message, sessionID, eventType); //--> 35=AU
        } else if(MsgType.SETTLEMENT_INSTRUCTION_REQUEST.equals(msgType)) {
            processSettlementInstructionRequest(message, sessionID, eventType); //--> 35=AV
        } else if(MsgType.ASSIGNMENT_REPORT.equals(msgType)) {
            processAssignmentReport(message, sessionID, eventType); //--> 35=AW
        } else if(MsgType.COLLATERAL_REQUEST.equals(msgType)) {
            processCollateralRequest(message, sessionID, eventType); //--> 35=AX
        } else if(MsgType.COLLATERAL_ASSIGNMENT.equals(msgType)) {
            processCollateralAssignment(message, sessionID, eventType); //--> 35=AY
        } else if(MsgType.COLLATERAL_RESPONSE.equals(msgType)) {
            processCollateralResponse(message, sessionID, eventType); //--> 35=AZ
        } else if(MsgType.COLLATERAL_REPORT.equals(msgType)) {
            processCollateralReport(message, sessionID, eventType); //--> 35=BA
        } else if(MsgType.COLLATERAL_INQUIRY.equals(msgType)) {
            processCollateralInquiry(message, sessionID, eventType); //--> 35=BB
        } else if(MsgType.NETWORK_STATUS_REQUEST.equals(msgType)) {
            processNetworkStatusRequest(message, sessionID, eventType); //--> 35=BC
        } else if(MsgType.NETWORK_STATUS_RESPONSE.equals(msgType)) {
            processNetworkStatusResponse(message, sessionID, eventType); //--> 35=BD
        } else if(MsgType.USER_REQUEST.equals(msgType)) {
            processUserRequest(message, sessionID, eventType); //--> 35=BE
        } else if(MsgType.USER_RESPONSE.equals(msgType)) {
            processUserResponse(message, sessionID, eventType); //--> 35=BF
        } else if(MsgType.COLLATERAL_INQUIRY_ACK.equals(msgType)) {
            processCollateralInquiryAck(message, sessionID, eventType); //--> 35=BG
        } else if(MsgType.CONFIRMATION_REQUEST.equals(msgType)) {
            processConfirmationRequest(message, sessionID, eventType); //--> 35=BH
        } else if(MsgType.TRADING_SESSION_LIST_REQUEST.equals(msgType)) {
            processTradingSessionListRequest(message, sessionID, eventType); //--> 35=BI
        } else if(MsgType.TRADING_SESSION_LIST.equals(msgType)) {
            processTradingSessionList(message, sessionID, eventType); //--> 35=BJ
        } else if(MsgType.SECURITY_LIST_UPDATE_REPORT.equals(msgType)) {
            processSecurityListUpdateReport(message, sessionID, eventType); //--> 35=BK
        } else if(MsgType.ADJUSTED_POSITION_REPORT.equals(msgType)) {
            processAdjustedPositionReport(message, sessionID, eventType); //--> 35=BL
        } else if(MsgType.ALLOCATION_INSTRUCTION_ALERT.equals(msgType)) {
            processAllocationInstructionAlert(message, sessionID, eventType); //--> 35=BM
        } else if(MsgType.EXECUTION_ACKNOWLEDGEMENT.equals(msgType)) {
            processExecutionAcknowledgement(message, sessionID, eventType); //--> 35=BN
        } else if(MsgType.CONTRARY_INTENTION_REPORT.equals(msgType)) {
            processContraryIntentionReport(message, sessionID, eventType); //--> 35=BO
        } else if(MsgType.SECURITY_DEFINITION_UPDATE_REPORT.equals(msgType)) {
            processSecurityDefinitionUpdateReport(message, sessionID, eventType); //--> 35=BP
        } else {
            processUnknownMessage(message, sessionID, eventType); //--> Unknown Message
        }
    }

    public abstract void processOnCreate(SessionID sessionID);

    public abstract void processOnLogon(SessionID sessionID);

    public abstract void processOnLogout(SessionID sessionID);

    /**
     *
     * [35=0] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processHeartbeat(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=1] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processTestRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=2] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processResendRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=3] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processReject(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=4] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSequenceReset(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=5] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processLogout(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=6] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processIndicationOfInterest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=7] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processAdvertisement(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=8] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processExecutionReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=9] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processOrderCancelReject(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=A] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processLogon(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=B] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processNews(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=C] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processEmail(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=D] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processOrderSingle(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=E] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processOrderList(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=F] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processOrderCancelRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=G] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processOrderCancelReplaceRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=H] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processOrderStatusRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=J] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processAllocationInstruction(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=K] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processListCancelRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=L] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processListExecute(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=M] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processListStatusRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=N] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processListStatus(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=P] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processAllocationInstructionAck(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=Q] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processDontKnowTrade(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=R] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processQuoteRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=S] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processQuote(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=T] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSettlementInstructions(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=V] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processMarketDataRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=W] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processMarketDataSnapshotFullRefresh(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=X] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processMarketDataIncrementalRefresh(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=Y] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processMarketDataRequestReject(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=Z] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processQuoteCancel(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=a] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processQuoteStatusRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=b] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processMassQuoteAcknowledgement(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=c] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSecurityDefinitionRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=d] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSecurityDefinition(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=e] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSecurityStatusRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=f] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSecurityStatus(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=g] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processTradingSessionStatusRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=h] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processTradingSessionStatus(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=i] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processMassQuote(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=j] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processBusinessMessageReject(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=k] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processBidRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=l] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processBidResponse(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=m] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processListStrikePrice(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=n] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processXmlMessage(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=o] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processRegistrationInstructions(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=p] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processRegistrationInstructionsResponse(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=q] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processOrderMassCancelRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=r] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processOrderMassCancelReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=s] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processNewOrderCross(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=t] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processCrossOrderCancelReplaceRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=u] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processCrossOrderCancelRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=v] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSecurityTypeRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=w] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSecurityTypes(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=x] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSecurityListRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=y] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSecurityList(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=z] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processDerivativeSecurityListRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AA] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processDerivativeSecurityList(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AB] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processNewOrderMultileg(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AC] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processMultilegOrderCancelReplace(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AD] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processTradeCaptureReportRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AE] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processTradeCaptureReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AF] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processOrderMassStatusRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AG] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processQuoteRequestReject(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AH] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processRfqRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AI] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processQuoteStatusReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AJ] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processQuoteResponse(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AK] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processConfirmation(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AL] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processPositionMaintenanceRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AM] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processPositionMaintenanceReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AN] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processRequestForPositions(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AO] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processRequestForPositionsAck(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AP] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processPositionReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AQ] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processTradeCaptureReportRequestAck(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AR] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processTradeCaptureReportAck(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AS] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processAllocationReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AT] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processAllocationReportAck(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AU] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processConfirmationAck(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AV] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSettlementInstructionRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AW] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processAssignmentReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AX] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processCollateralRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AY] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processCollateralAssignment(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=AZ] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processCollateralResponse(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BA] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processCollateralReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BB] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processCollateralInquiry(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BC] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processNetworkStatusRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BD] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processNetworkStatusResponse(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BE] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processUserRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BF] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processUserResponse(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BG] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processCollateralInquiryAck(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BH] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processConfirmationRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BI] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processTradingSessionListRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BJ] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processTradingSessionList(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BK] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSecurityListUpdateReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BL] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processAdjustedPositionReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BM] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processAllocationInstructionAlert(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BN] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processExecutionAcknowledgement(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BO] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processContraryIntentionReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * [35=BP] Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processSecurityDefinitionUpdateReport(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;

    /**
     *
     * Unknown Message process.
     *
     * @param message
     * @param sessionID
     * @param eventType
     * @throws FieldNotFound
     */
    public abstract void processUnknownMessage(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound;
}
