package com.example.postapi.config;


import com.example.postapi.handlers.PostHandlers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class PostRouter {
    @Bean
    public RouterFunction<ServerResponse> routerFunctionGetShowALL(PostHandlers postHandlers){
        return RouterFunctions
                .route(RequestPredicates.GET("/post").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        postHandlers::showAll);
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionGetShowById(PostHandlers postHandlers){
        return RouterFunctions
                .route(RequestPredicates.GET("/post/id").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        postHandlers::showByID);
    }
    @Bean
    public RouterFunction<ServerResponse> routerFunctionPostAddOnePost(PostHandlers postHandlers){
        return RouterFunctions
                .route(RequestPredicates.POST("/post").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        postHandlers::addPost);
    }
    @Bean
    public RouterFunction<ServerResponse> routerFunctionPostShowUserNews(PostHandlers postHandlers){
        return RouterFunctions
                .route(RequestPredicates.POST("/post/news").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        postHandlers::showUserNews);
    }
    @Bean
    public RouterFunction<ServerResponse> routerFunctionPostShowPostById(PostHandlers postHandlers){
        return RouterFunctions
                .route(RequestPredicates.GET("/post/author").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        postHandlers::showAllByAuthorId);
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionPostUpdatePost(PostHandlers postHandlers){
        return RouterFunctions
                .route(RequestPredicates.PATCH("/post").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        postHandlers::updatePost);
    }
    @Bean
    public RouterFunction<ServerResponse> routerFunctionDeletePost(PostHandlers postHandlers){
        return RouterFunctions
                .route(RequestPredicates.DELETE("/post").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        postHandlers::deletePost);
    }


}
