package com.example.MyNetWork.service;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface LikeServiceMongo {
    public CompletableFuture<List<BigInteger>> getUserLike();
    public void  saveLike(Long id, List<BigInteger> listLikes);
    public void addOrRemoveLike(BigInteger postId, Long userId, String action, Long authorId);
}
