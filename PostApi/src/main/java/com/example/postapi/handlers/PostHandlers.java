package com.example.postapi.handlers;


import com.example.postapi.model.Post;
import com.example.postapi.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PostHandlers {
    @Autowired
    private PostService postService;
    public Mono<ServerResponse> showAll(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(postService.showAll(), Post.class);
    }
    public Mono<ServerResponse> showAllByAuthorId(ServerRequest request) {
        Long id = request.queryParam("id").map(Long::valueOf).orElse(null);
        if(id == null)return ServerResponse.
                badRequest()
                .varyBy("Not corrext id").build();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(postService.showByAuthorId(id),Post.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }


    public Mono<ServerResponse> showByID(ServerRequest request) {
        Long id = request.queryParam("id").map(Long::valueOf).orElse(null);
        if(id == null)return ServerResponse.
                badRequest()
                .varyBy("Not correct id").build();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(postService.showByID(id),Post.class)
               .onErrorResume(
                       e-> ServerResponse.notFound()
                               .varyBy("Not found id"+"\n"+e.getMessage())
                               .build());


    }

    public Mono<ServerResponse> addPost(ServerRequest serverRequest) {
        Mono<Post> newPost = serverRequest.bodyToMono(Post.class);
        log.info("Start add Controller");
        Mono<ServerResponse> serverResponse =ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(postService.save(newPost),Post.class)
                .onErrorResume(e->ServerResponse.
                        badRequest()
                        .varyBy("Not correct data").build());
        log.info("Finish add Controller");

        return serverResponse;
    }
    public Mono<ServerResponse> updatePost(ServerRequest serverRequest) {
        Mono<Post> newPost = serverRequest.bodyToMono(Post.class);
        Long id = serverRequest.queryParam("id").map(Long::valueOf).orElse(null);
        if(id == null)return ServerResponse.
                badRequest()
                .varyBy("Not found id").build();

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(postService.update(newPost, id), Post.class);
    }
    public Mono<ServerResponse> deletePost(ServerRequest serverRequest) {

        Long id = serverRequest.queryParam("id").map(Long::valueOf).orElse(null);
        if(id == null)return ServerResponse.
                badRequest()
                .varyBy("Not correct id").build();

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(postService.delete(id),Void.class);
    }

    public Mono<ServerResponse> showUserNews(ServerRequest serverRequest){
        log.info("nice");
        Flux<Long> flux = serverRequest.bodyToFlux(Long.class);
        log.info("nice2");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_NDJSON).body(postService.showUserNews(flux),Post.class);
    }

}
