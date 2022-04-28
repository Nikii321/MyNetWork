package com.example.detailsapi.service;

import com.example.detailsapi.model.Details;

public interface MessageSender {
    public void send(Details message);
}