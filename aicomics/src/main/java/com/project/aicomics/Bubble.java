package com.project.aicomics;

public class Bubble {
  
  private String content;
  private String status;

  @Override
    public String toString() {
        return "Bubble{" + "status=" + getStatus() +", content=" + getContent() +"}";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
