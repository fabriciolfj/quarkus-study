package com.github.fabricio.resources;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ResourceContext;
import jakarta.ws.rs.core.MediaType;

import java.net.URI;

@Path("/extract")
public class ScreenShotResource {

    @Inject
    ChatModel chatModel;
    @Inject
    ResourceContext resourceContext;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String extract() {
        UserMessage userMessage = UserMessage.from(
                TextContent.from(
                        """ 
                                esta imagem e um codigo regitrado no github
                                se a linhas forem enumeradas, retire
                                extraia o texto da imagem
                                """
                ),
                ImageContent.from(
                        URI.create("https://i.postimg.cc/fL6x1MK9/screenshot.png")
                ));

        ChatResponse chatResponse = chatModel.chat(userMessage);
        return chatResponse.aiMessage().text();
    }
}
