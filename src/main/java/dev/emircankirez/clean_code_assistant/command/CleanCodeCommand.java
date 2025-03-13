package dev.emircankirez.clean_code_assistant.command;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.shell.command.annotation.Command;

@Command
public class CleanCodeCommand {

    private final ChatClient chatClient;

    public CleanCodeCommand(VectorStore vectorStore, ChatClient.Builder chatClient) {
        this.chatClient = chatClient
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    @Command(command = "welcome")
    public String welcome() {
        return "Welcome to Clean Code Assistant!";
    }

    @Command(command = "q")
    public String askQuestion(String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();

    }
}
