package it.multicoredev.sti.twitch;

import com.google.common.base.Defaults;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Copyright © 2020 by Lorenzo Magni
 * This file is part of MCLib.
 * MCLib is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

public class TwitchEvent {
    private final String type;
    private final String account;

    private String nickname;
    private String msg;
    private Set<String> badges;

    private double donationAmount;
    private String donationCurrency;
    private String formattedAmount;

    private int subscriptionMonths;
    private int subscriptionStreakMonths;
    private int subscriptionTier = -1;
    private boolean gifted;
    private boolean resubbed;

    private int viewerCount;
    private int raiderCount;

    public int rewardID;
    public String rewardTitle;
    public Set<String> chatBadges = new HashSet<>();

    public TwitchEvent(String type, String account) {
        this.type = type;
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public String getAccount() {
        return account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public double getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(double donationAmount) {
        this.donationAmount = donationAmount;
    }

    public String getDonationCurrency() {
        return donationCurrency;
    }


    public void setDonationCurrency(String donationCurrency) {
        this.donationCurrency = donationCurrency;
    }

    public int getSubscriptionMonths() {
        return subscriptionMonths;
    }

    public void setSubscriptionMonths(int subscriptionMonths) {
        this.subscriptionMonths = subscriptionMonths;
    }

    public int getSubscriptionTier() {
        return subscriptionTier;
    }

    public void setSubscriptionTier(int subscriptionTier) {
        this.subscriptionTier = subscriptionTier;
    }

    public boolean isGifted() {
        return gifted;
    }

    public void setGifted(boolean gifted) {
        this.gifted = gifted;
    }

    public int getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(int viewerCount) {
        this.viewerCount = viewerCount;
    }

    public int getRaiderCount() {
        return raiderCount;
    }

    public void setRaiderCount(int raiderCount) {
        this.raiderCount = raiderCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        String delimiter = "";

        try {
            for (Field field : getClass().getDeclaredFields()) {
                Object value = field.get(this);
                Object defaultValue = Defaults.defaultValue(field.getType());

                if (value == null) continue;

                if (!value.equals(defaultValue)) {
                    sb.append(delimiter);
                    sb.append(field.getName()).append("=").append(value);
                    delimiter = ", ";
                } else if (field.getName().equalsIgnoreCase("tier") && value.equals(0)) {
                    sb.append(delimiter);
                    sb.append(field.getName()).append("=").append(value);
                    delimiter = ", ";
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        sb.append("}");

        return sb.toString();
    }

    public int getSubscriptionStreakMonths() {
        return subscriptionStreakMonths;
    }

    public void setSubscriptionStreakMonths(int subscriptionStreakMonths) {
        this.subscriptionStreakMonths = subscriptionStreakMonths;
    }

    public boolean isResubbed() {
        return resubbed;
    }

    public void setResubbed(boolean resubbed) {
        this.resubbed = resubbed;
    }

    public String getFormattedAmount() {
        return formattedAmount;
    }

    public void setFormattedAmount(String formatted_amount) {
        this.formattedAmount = formatted_amount;
    }

    public Set<String> getBadges() {
        return badges;
    }

    public void setBadges(Set<String> badges) {
        this.badges = badges;
    }
}
