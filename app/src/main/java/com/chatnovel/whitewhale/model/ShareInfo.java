package com.chatnovel.whitewhale.model;

import java.io.Serializable;

/**
 * Created by Wyatt on 2017/6/15/015.
 */

public class ShareInfo implements Serializable {

    private String shareUrl;
    private String title;
    private String shareText;
    private String icon;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
