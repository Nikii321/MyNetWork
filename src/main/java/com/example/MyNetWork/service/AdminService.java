package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.User;

import java.util.List;

public interface AdminService {
    public List<User> allUsersForAdmin();
    public List<User> allBanForAdmin();
    public boolean unbanUser(Long userId);
    public boolean banUser(Long userId);
    public List<User> usergtList(Long idMin);
}
