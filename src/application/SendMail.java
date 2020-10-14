package application;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	
	public static void sendMail(String recipient, String name) throws MessagingException {
		
		Properties properties = new Properties();
		
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		
		String sender = "farmhub.co.in@gmail.com";
		String password = "farmhub99";

		Session session = Session.getInstance(properties, new Authenticator() {	
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, password);
			}	
		});
		
		Message message = prepareMessage(session, sender, recipient, name);
		
		Transport.send(message);

	}
	
	private static Message prepareMessage(Session session, String sender, String recipient, String name) {
		
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject("Welcome to FarmHub!");
			String content = "Hi, "+name+"!\nWelcome to FarmHub. Your one-stop shopping destination for all fruits and vegetables. Directly from the farm.";
			message.setText(content);
			return message;

		} catch (AddressException e) {
			e.printStackTrace();

		} catch (MessagingException e) {
			e.printStackTrace();

		}return null;
	}
}
