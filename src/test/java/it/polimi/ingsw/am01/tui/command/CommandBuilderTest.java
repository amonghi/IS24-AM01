package it.polimi.ingsw.am01.tui.command;

import it.polimi.ingsw.am01.client.tui.command.*;
import org.junit.jupiter.api.Test;

import java.util.List;

class CommandBuilderTest {

    // not an actual test, this is just to verify visually that everything is working
    // TODO: actual tests
    @Test
    void test1() {
        CommandNode rootCommandNode = CommandBuilder.root()
                .branch(
                        SequenceBuilder
                                .literal("ping")
                                .thenWhitespace()
                                .thenLiteral("pong")
                                .executes(commandContext -> System.out.println("selected: ping pong"))
                                .end()
                )
                .branch(
                        SequenceBuilder
                                .literal("bing")
                                .thenWhitespace()
                                .thenLiteral("bong")
                                .executes(commandContext -> System.out.println("selected: bing bong"))
                                .end()
                )
                .branch(
                        SequenceBuilder
                                .literal("hello")
                                .thenWhitespace()
                                .then(new WordArgumentParser("name"))
                                .executes(commandContext -> System.out.println("selected: hello, with name=" + commandContext.getString("name")))
                                .end()
                )
                .build();

        List<String> inputs = List.of(
                "",
                "   ",
                "p",
                "ping",
                "bing",
                "ping pong",
                "hello ",
                "hello coso"
        );

        for (String input : inputs) {
            System.out.println("===");
            System.out.println("input: \"" + input + "\"");

            CommandNode.Result result = rootCommandNode.parse(new CommandContext(), input);
            System.out.println(result);
            result.getCommandRunnable().ifPresent(Runnable::run);
        }
    }

}
