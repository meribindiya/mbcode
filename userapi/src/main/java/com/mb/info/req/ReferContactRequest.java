package com.mb.info.req;

import java.util.List;

public class ReferContactRequest {
    private List<String> mobileNumbers;
    private Long userId;

    public List<String> getMobileNumbers() {
        return mobileNumbers;
    }

    public void setMobileNumbers(List<String> mobileNumbers) {
        this.mobileNumbers = mobileNumbers;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ReferContactRequest{" +
                "mobileNumbers=" + mobileNumbers +
                ", userId=" + userId +
                '}';
    }
}
