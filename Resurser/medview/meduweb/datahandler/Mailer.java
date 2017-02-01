package medview.meduweb.datahandler;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.sql.*;
/**
 *This class is used to send single e-mails in standard form.
 *@author Figge
 *@version 1.0
 */
public class Mailer {
    //private String host = "piggy.mdstud.chalmers.se";
	private String host = System.getProperty("mail.host");
    //private String from = "dproj-6@mdstud.chalmers.se";
	private String from = System.getProperty("mail.fromAdress");
    private String protocol = "smtp";
    private boolean sessionDebug = false;

    /**
     *Sends the email to the specified address.
     *@param addrs The first element is the adress to send to.
     *@param subject The header/subject of the mail to send.
     *@param text The textbody for the email.
     */
    public void sendMail(String[] addrs, String subject, String text){
	try {
	InternetAddress[] address = {new InternetAddress(addrs[0])};
	Properties props = System.getProperties();
	Message msg;
	Session session;
	props.put("mail.host", host);
	props.put("mail.transport.protocol", protocol);
        session = Session.getDefaultInstance(props, null);;
	session.setDebug(sessionDebug);
	msg = new MimeMessage(session);
	msg.setFrom(new InternetAddress(from));
	msg.setRecipients(Message.RecipientType.TO, address);
	msg.setSubject(subject);
	msg.setSentDate(new java.util.Date());
	msg.setText(text);
	Transport.send(msg);
	} catch(MessagingException me) {
	}
    }

    /**
     *Sets the host to use for delivery.
     *@param hostaddr The address of the the server to use for delivery.
     */
    public void setHost(String hostaddr) {
	this.host = hostaddr;
    }

    /**
     *Sets the from-address for outgoing messages.
     *@param fromaddr email address to use as from-address.
     */
    public void setFrom(String fromaddr) {
	this.from = fromaddr;
    }

    /**
     *Used to decide to use debug for this session.
     *The default is false (no debug).
     *@param sd Sets whether to use debug or not.
     */
    public void setSessionDebug(boolean sd) {
	this.sessionDebug = sd;
    }

    /**
     *Sets the protocol to use for outgoing emails.
     *SMTP is used by default.
     *@param protocol The name of the protocol.
     */
    public void setProtocol(String protocol) {
	this.protocol = protocol;
    }
}
