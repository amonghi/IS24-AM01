package it.polimi.ingsw.am01.client.tui.command;

import it.polimi.ingsw.am01.client.tui.command.parser.LiteralParser;
import it.polimi.ingsw.am01.client.tui.command.parser.Parser;
import it.polimi.ingsw.am01.client.tui.command.parser.WhiteSpaceParser;
import it.polimi.ingsw.am01.client.tui.command.validator.PostValidator;
import it.polimi.ingsw.am01.client.tui.command.validator.PreValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandBuilder {
    private final Parser parser;
    private final List<CommandNode> children;
    private Consumer<CommandContext> executor;
    private PreValidator preValidator;
    private PostValidator postValidator;

    private CommandBuilder(Parser parser) {
        this.parser = parser;
        this.children = new ArrayList<>();
    }

    public static CommandBuilder token(Parser parser) {
        return new CommandBuilder(parser);
    }

    public static CommandBuilder root() {
        return token(new WhiteSpaceParser(false));
    }

    public static CommandBuilder literal(String literal) {
        return token(new LiteralParser(literal));
    }

    public static CommandBuilder whiteSpace() {
        return token(new WhiteSpaceParser(true));
    }

    public CommandBuilder executes(Consumer<CommandContext> executor) {
        this.executor = executor;
        return this;
    }

    public CommandBuilder validatePre(PreValidator validator) {
        this.preValidator = validator;
        return this;
    }

    public CommandBuilder validatePost(PostValidator validator) {
        this.postValidator = validator;
        return this;
    }

    public CommandBuilder branch(CommandNode child) {
        this.children.add(child);
        return this;
    }

    public CommandBuilder branch(CommandBuilder child) {
        return branch(child.build());
    }

    public CommandNode build() {
        return new CommandNode(this.parser, this.executor, this.preValidator, this.postValidator, this.children);
    }
}
