package codestream.jungmini.me.mail.service;

public interface MailService {
    void send(String to, String subject, String body);
}
