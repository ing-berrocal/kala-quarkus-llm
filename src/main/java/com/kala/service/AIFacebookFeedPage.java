package com.kala.service;

import com.kala.model.AssitResponse;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(modelName = "az-llama")
public interface AIFacebookFeedPage {
    @SystemMessage(value = "", fromResource = "/prompt/aifacebook.system.promp")
    @UserMessage(value = "", fromResource = "/prompt/aifacebook.user.promp")
    public AssitResponse query(@MemoryId int memoryId, String product, String comment);
}
