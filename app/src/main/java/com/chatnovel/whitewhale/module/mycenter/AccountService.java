package com.chatnovel.whitewhale.module.mycenter;
public class AccountService {

    private static AccountService instance = new AccountService();
    private int uid;
    private boolean isLogin;
    private int loginType;

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public static AccountService getInstance() {
        return instance;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
