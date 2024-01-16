package com.practice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GreetingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);

    private final ChatClient chatClient;

    public GreetingController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/greeting")
    public String greet(@RequestParam(name = "greetingType", defaultValue = "greeting") String greetingType,
                        @RequestParam(name = "format", defaultValue = "text") String format,
                        @RequestParam(name = "language", defaultValue = "English") String language){

        LOGGER.info("Greeting request { greetingType: {}, format: {}, language: {} }", greetingType, format, language);

        final String greetingTemplate = """
                As a modern generative AI model,
                Generate a 5 liner {greetingType} in your style for a human in {language} in {format} form.
                Thanks in advance.
                """;

        final PromptTemplate promptTemplate = new PromptTemplate(greetingTemplate);

        final Prompt prompt = promptTemplate.create(Map.of("greetingType", greetingType, "format", format, "language", language));

        final String aiClientResponse = chatClient.generate(prompt)
                .getGeneration()
                .getContent();

        LOGGER.info("AI Response: \n{}", aiClientResponse);

        return aiClientResponse;
    }
}