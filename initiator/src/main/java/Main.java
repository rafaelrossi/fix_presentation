
import quickfix.Application;
import quickfix.SessionID;
import quickfix.Session;
import quickfix.Initiator;
import quickfix.SocketInitiator;
import quickfix.SessionSettings;
import quickfix.FileStoreFactory;
import quickfix.MessageStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.ScreenLogFactory;
import quickfix.DefaultMessageFactory;
import quickfix.ConfigError;
import quickfix.SessionNotFound;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.RejectLogon;
import quickfix.DoNotSend;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.NewOrderSingle;
import quickfix.field.Side;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.Symbol;
import quickfix.field.OrdType;
import quickfix.field.TransactTime;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

public class Main
{

    public static class ClientApplication implements Application
    {
        public static volatile SessionID sessionID = null;

        @Override
        public void onCreate(SessionID sessionID)
        {
            System.out.println("OnCreate");
        }

        @Override
        public void onLogon(SessionID sessionID)
        {
            System.out.println("OnLogon");
            this.sessionID = sessionID;
        }

        @Override
        public void onLogout(SessionID sessionID)
        {
            System.out.println("OnLogout");
            this.sessionID = null;
        }

        @Override
        public void toAdmin(quickfix.Message message, SessionID sessionID)
        {

        }

        @Override
        public void fromAdmin(quickfix.Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon
        {

        }

        @Override
        public void toApp(quickfix.Message message, SessionID sessionID) throws DoNotSend
        {

        }

        @Override
        public void fromApp(quickfix.Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType
        {

        }

        public Integer orderId = 0;
        public void sendOrder() throws SessionNotFound
        {
            orderId++;
            LocalDateTime rightNow = LocalDateTime.now();
            NewOrderSingle newOrder = new NewOrderSingle(
                    new ClOrdID(orderId.toString()),
                    new Side(Side.BUY),
                    new TransactTime(rightNow),
                    new OrdType(OrdType.MARKET));
            newOrder.setField(new Symbol("PETR4"));
            newOrder.setField(new HandlInst('1'));

            Session.sendToTarget(newOrder, sessionID);
        }
    }

    public static void main(String[] args) throws ConfigError, FileNotFoundException, InterruptedException, SessionNotFound {
        SessionSettings settings = new SessionSettings("initiator/src/main/resources/config.cfg");

        ClientApplication application = new ClientApplication();
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new ScreenLogFactory( true, true, true);
        MessageFactory messageFactory = new DefaultMessageFactory();

        Initiator initiator = new SocketInitiator(application, messageStoreFactory, settings, logFactory, messageFactory);
        initiator.start();


        while (true)
        {
            Thread.sleep(10000);
            application.sendOrder();
        }
    }

}
