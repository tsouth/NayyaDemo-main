package com.nayya.utilities.email;

import com.nayya.utilities.logger.Logger;
import org.testng.TestException;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmailUtility {
    private final EmailCredentialUtility emailCredentialUtility = new EmailCredentialUtility();
    private final Logger log = new Logger(getClass().getName());

    private Folder emailFolder;

    public String getEmail(String emailFolder, String subject) {
        openEmailFolder(emailFolder);
        Message[] messages = getAllUnreadEmails();
        log.info("Attempting to find email: " + subject);
        for (Message message : messages) {
            try {
                if (message.getSubject().contains(subject)) {
                    log.info("Email found!");
                    setEmailAsRead(message);
                    String emailContent = getMessageContent(message);
                    closeEmailFolder();
                    return emailContent;
                }
            } catch (MessagingException e) {
                throw new TestException("Email failed to be read", e.getCause());
            }
        }

        log.info("Email not found!");
        closeEmailFolder();
        return null;
    }

    public int getRetryLimit() {
        return 15;
    }

    public List<String> getURLs(Message message, String regex) {
        List<String> urls = getURLsFromMessage(message);
        Pattern urlPattern = Pattern.compile(regex);
        Stream<String> matchingURLs = urls.stream().filter(url -> urlPattern.matcher(url).find());
        return matchingURLs.collect(Collectors.toList());
    }

    private void openEmailFolder(String folderName) {
        String host = "imap.gmail.com";
        String port = "993";
        String protocol = "imaps";

        Properties props = new Properties();
        props.put("mail.imaps.host", host);
        props.put("mail.imaps.port", port);
        props.put("mail.imaps.ssl.enable", "true");

        //Create imaps store object
        Session emailSession = Session.getInstance(props);
        Store store;
        try {
            store = emailSession.getStore(protocol);
        } catch (NoSuchProviderException e) {
            throw new TestException("Invalid Protocal: " + protocol, e.getCause());
        }

        //Connect and open Email folder
        Map<String, String> gMailCredentials = emailCredentialUtility.getGMailCredentials();
        String email = gMailCredentials.get("email");
        String password = gMailCredentials.get("password");
        try {
            store.connect(host, email, password);
            emailFolder = store.getFolder(folderName);
            emailFolder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            throw new TestException("Failed to connect to email host", e.getCause());
        }
    }

    private void closeEmailFolder() {
        try {
            emailFolder.close();
            emailFolder.getStore().close();
        } catch (MessagingException e) {
            throw new TestException("Failed to close email folder");
        }
    }

    private Message[] getAllUnreadEmails() {
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);

        Message[] emails;
        try {
            emails = emailFolder.search(unseenFlagTerm);
        } catch (MessagingException e) {
            throw new TestException("Failed to search through emails", e.getCause());
        }

        return emails;
    }

    private String getMessageContent(Message singleMessage) {
        StringBuilder buffer = new StringBuilder();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(singleMessage.getInputStream()));
        } catch (IOException | MessagingException e) {
            throw new TestException("Failed to open email input stream", e.getCause());
        }

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            throw new TestException("Failed to read message line", e.getCause());
        }

        return buffer.toString();
    }

    private List<String> getURLsFromMessage(Message singleMessage) {
        String html = getMessageContent(singleMessage);
        List<String> allMatches = new ArrayList<>();
        Matcher matcher = Pattern.compile("(<a href=\"([^\"]+)\")").matcher(html);
        while (matcher.find()) {
            allMatches.add(matcher.group(2));
        }
        return allMatches;
    }

    private void setEmailAsRead(Message message) {
        try {
            emailFolder.setFlags(new Message[]{message}, new Flags(Flags.Flag.SEEN), true);
        } catch (MessagingException e) {
            throw new TestException("Failed to set messaged as read", e.getCause());
        }
    }
}