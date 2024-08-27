package dev.questionarium.interfaces;

public interface PasswordEncodable<T> {

    public T getEncodedObject(String password);

    public String getPassword();

}
