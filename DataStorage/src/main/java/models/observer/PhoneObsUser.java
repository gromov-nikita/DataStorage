package models.observer;

public class PhoneObsUser implements IObsUser {
    private String phone;
    public PhoneObsUser(String phone) {
        this.phone = phone;
    }
    @Override
    public String notify(String event) {
        return "Phone: " + phone + "\nEvent: " + event;
    }
}
