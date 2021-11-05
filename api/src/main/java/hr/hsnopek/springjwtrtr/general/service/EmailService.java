package hr.hsnopek.springjwtrtr.general.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import hr.hsnopek.springjwtrtr.domain.base.BaseService;
import hr.hsnopek.springjwtrtr.domain.feature.user.dto.UserDTO;
import hr.hsnopek.springjwtrtr.general.localization.Message;
import hr.hsnopek.springjwtrtr.general.util.ApplicationProperties;

@Component
public class EmailService extends BaseService {
	
	@Autowired
	private JavaMailSender emailSender;

	public void sendSimpleMessage(String to, String subject, String text) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("zsnopek@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}

	public void sendVerificationEmail(UserDTO userDto, String from, String senderName)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = userDto.getEmail();
		String subject = Translator.toLocale(Message.REGISTRATION_EMAIL_SUBJECT);
		
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(from, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		String verificationURL = ApplicationProperties.APPLICATION_URL + "/user/email-verify?verificationCode=" + userDto.getVerificationCode();
		
		String email = Translator.toLocale(Message.REGISTRATION_EMAIL,
				new Object[] { userDto.getFirstName(), verificationURL });

		helper.setText(email, true);

		try {
			emailSender.send(message);
		} catch (MailException e) {
			e.printStackTrace();
		}

	}
}
