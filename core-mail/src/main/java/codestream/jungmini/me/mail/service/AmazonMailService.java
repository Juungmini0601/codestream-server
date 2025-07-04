package codestream.jungmini.me.mail.service;

import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.mail.exception.MailException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonMailService implements MailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private static final String ADMIN_EMAIL = "codestream-admin@jungmini.me";

    @Override
    public void send(String to, String subject, String body) {
        try {
            SendEmailRequest request = new SendEmailRequest()
                    .withSource(ADMIN_EMAIL)
                    .withDestination(new Destination().withToAddresses(to))
                    .withMessage(new Message()
                            .withSubject(new Content()
                                    .withCharset(StandardCharsets.UTF_8.name())
                                    .withData(subject))
                            .withBody(new Body()
                                    .withText(new Content()
                                            .withCharset(StandardCharsets.UTF_8.name())
                                            .withData(body))));

            amazonSimpleEmailService.sendEmail(request);
        } catch (Exception e) {
            log.error("fail send mail to [{}]", to, e);
            throw new MailException(e.getMessage());
        }
    }
}
