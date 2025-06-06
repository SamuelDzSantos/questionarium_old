package br.com.questionarium.entities;

public class Email {
    private String subject;
    private String message;
    private String emailTo;

    public Email() { }

    public Email(String subject, String message, String emailTo) {
        this.subject = subject;
        this.message = message;
        this.emailTo = emailTo;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }
}
