package com.mail.sample;

import com.mail.sample.constants.SampleMailConstants;
import com.mail.sample.model.EmailModel;
import com.mail.sample.model.ProxyModel;
import com.mail.sample.model.SmtpModel;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    private static InputStream inputStream = null;
    private static EmailModel emailModel = null;
    private static ProxyModel proxyModel = null;
    private static SmtpModel smtpModel = null;

    public static void main(String[] args) {
        try {
            System.out.println("====== Send Mail Begin ======");
            loadProperties();
            sendMail();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("====== Send Mail End ======");
        }
    }

    private static void loadProperties() throws IOException {
        System.out.println("====== Send Mail Begin - loadProperties ======");
        Properties properties = new Properties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();


        inputStream = classloader.getResourceAsStream("config.properties");
        properties.load(inputStream);

        emailModel = new EmailModel(properties.getProperty(SampleMailConstants.MAIL_FROM),
                properties.getProperty(SampleMailConstants.MAIL_TO),
                properties.getProperty(SampleMailConstants.MAIL_SUBJECT),
                properties.getProperty(SampleMailConstants.MAIL_BODY), null);

        logEmail();

        smtpModel = new SmtpModel(properties.getProperty(SampleMailConstants.MAIL_SMTP_SERVER),
                Integer.valueOf(properties.getProperty(SampleMailConstants.MAIL_SMTP_PORT)),
                properties.getProperty(SampleMailConstants.MAIL_SMTP_USER),
                properties.getProperty(SampleMailConstants.MAIL_SMTP_PASSWORD));

        logSmtp();

        if (Boolean.valueOf(properties.getProperty(SampleMailConstants.MAIL_PROXY_ACTIVE))) {
            proxyModel = new ProxyModel(properties.getProperty(SampleMailConstants.MAIL_PROXY_HOST),
                    Integer.valueOf(properties.getProperty(SampleMailConstants.MAIL_PROXY_PORT)),
                    properties.getProperty(SampleMailConstants.MAIL_PROXY_USER),
                    properties.getProperty(SampleMailConstants.MAIL_PROXY_PASSWORD),
                    Boolean.valueOf(properties.getProperty(SampleMailConstants.MAIL_PROXY_ACTIVE)));
        }

        logProxy();

        System.out.println("====== Send Mail End - loadProperties ======");
    }

    private static void logEmail() {
        System.out.println("====== Logging Email properties ======");
        System.out.println(SampleMailConstants.MAIL_FROM + ": " + emailModel.getFrom());
        System.out.println(SampleMailConstants.MAIL_TO + ": " + emailModel.getTo());
        System.out.println(SampleMailConstants.MAIL_SUBJECT + ": " + emailModel.getSubject());
        System.out.println(SampleMailConstants.MAIL_BODY + ": " + emailModel.getBody());
    }

    private static void logSmtp() {
        System.out.println("====== Logging SMTP properties ======");
        System.out.println(SampleMailConstants.MAIL_SMTP_SERVER + ": " + smtpModel.getServer());
        System.out.println(SampleMailConstants.MAIL_SMTP_PORT + ": " + smtpModel.getPort());
        System.out.println(SampleMailConstants.MAIL_SMTP_USER + ": " + smtpModel.getUser());
    }

    private static void logProxy() {
        System.out.println("====== Logging PROXY properties ======");

        if (proxyModel != null && proxyModel.getActive() != null) {
            System.out.println(SampleMailConstants.MAIL_PROXY_ACTIVE + ": " + proxyModel.getActive());
            System.out.println(SampleMailConstants.MAIL_PROXY_HOST + ": " + proxyModel.getHost());
            System.out.println(SampleMailConstants.MAIL_PROXY_PORT + ": " + proxyModel.getPort());
            System.out.println(SampleMailConstants.MAIL_PROXY_USER + ": " + proxyModel.getUser());
            System.out.println(SampleMailConstants.MAIL_PROXY_PASSWORD + ": " + proxyModel.getPassword());
        } else {
            System.out.println(SampleMailConstants.MAIL_PROXY_ACTIVE + ": false");
        }

    }

    private static void sendMail() {
        Email email = EmailBuilder.startingBlank()
                .from(emailModel.getFrom())
                .to(emailModel.getTo())
                .withSubject(emailModel.getSubject())
                .withPlainText(emailModel.getBody())
                .buildEmail();

        if (Boolean.valueOf(SampleMailConstants.MAIL_PROXY_ACTIVE)) {
            Mailer mailer = MailerBuilder
                    .withSMTPServer(smtpModel.getServer(), smtpModel.getPort(), smtpModel.getUser(), smtpModel.getPassword())
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withProxy(proxyModel.getHost(), proxyModel.getPort(), proxyModel.getUser(), proxyModel.getPassword())
                    .withSessionTimeout(10 * 1000)
                    .clearEmailAddressCriteria() // turns off email validation
                    .withDebugLogging(true)
                    .buildMailer();

            mailer.sendMail(email);
        } else {
            Mailer mailer = MailerBuilder
                    .withSMTPServer(smtpModel.getServer(), smtpModel.getPort(), smtpModel.getUser(), smtpModel.getPassword())
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withSessionTimeout(10 * 1000)
                    .clearEmailAddressCriteria() // turns off email validation
                    .withDebugLogging(true)
                    .buildMailer();

            mailer.sendMail(email);
        }
    }
}
