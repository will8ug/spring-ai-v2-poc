package io.will.springai2poc.controller.model;

public record CustomChatRequest(
        String query,
        String conversationId
) {
    public static CustomChatRequest withQuery(String query) {
        return new CustomChatRequest(query, null);
    }
}
