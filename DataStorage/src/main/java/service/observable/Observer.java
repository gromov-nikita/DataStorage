package service.observable;

import models.observer.IObsUser;
import models.observer.PhoneObsUser;

import java.util.LinkedList;
import java.util.List;

public class Observer {
    private List<IObsUser> iObsUsers = new LinkedList<IObsUser>();
    public void pushObsUser(IObsUser iObsUser) {
        iObsUsers.add(iObsUser);
    }
    public void deleteObsUser(IObsUser iObsUser) {
        iObsUsers.remove(iObsUser);
    }
    public void notify(String event) {
        for(IObsUser obs : iObsUsers) {
            System.out.println(obs.notify(event));
        }
    }
}
