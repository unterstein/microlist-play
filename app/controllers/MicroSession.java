package controllers;

import java.io.Serializable;

import play.mvc.Controller;
import security.Secured;

import models.User;

public class MicroSession implements Serializable {

    public static User getUser() {
        return User.getUserByEMail(Controller.session(Secured.AUTH_SESSION));
    }
}
