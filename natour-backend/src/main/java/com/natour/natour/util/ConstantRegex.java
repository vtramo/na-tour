package com.natour.natour.util;

public abstract class ConstantRegex {
    public final static String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
    public final static String NAME_REGEX = "^(?=.{1,40}$)[a-zA-Z]+(?:[-'\\s][a-zA-Z]+)*$";
    public final static String USERNAME_REGEX = "^(?=.{5,16}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
}
