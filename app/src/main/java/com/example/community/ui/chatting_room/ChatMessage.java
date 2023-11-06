package com.example.community.ui.chatting_room;

public class ChatMessage {
    private String content;
    private int type;
    private long time;
    private String username;

    public long getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public ChatMessage(String content, int type, long time, String username) {
        this.content = content;
        this.type = type;
        this.time = time;
        this.username = username;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "content='" + content + '\'' +
                ", type=" + type +
                ", time=" + time +
                ", username='" + username + '\'' +
                '}';
    }
}
