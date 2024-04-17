package com.nayya.utilities.email;

import java.util.Map;

public class EmailCredentialUtility {

    public EmailCredentialUtility() {
    }

    public Map<String, String> getAdminCredentials() {
        return getCredentials("testadmin");
    }


//    private Map<String, String> getCredentials(String user) {
//        String username = System.getenv("nayya_USERNAME");
//        String password = System.getenv("nayya_PASSWORD");
//
//        return Map.of("username", username, "password", password);
//    }

    public Map<String, String> getGMailCredentials() {
        return Map.of("email", System.getenv("nayya_EMAIL"), "password", System.getenv("nayya_GMAIL_PASSWORD"));
    }

    private Map<String, String> getCredentials(String user) {
        String email = System.getenv("SELENIUM_ADMIN_EMAIL");
        String[] splitEmail = email.split("@");
        return Map.of(
                "email", splitEmail[0] + "+" + user + "@" + splitEmail[1],
                "password", System.getenv("SELENIUM_ADMIN_ACCOUNT_PASSWORD")
        );
    }
}
