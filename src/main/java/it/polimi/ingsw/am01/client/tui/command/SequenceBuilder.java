package it.polimi.ingsw.am01.client.tui.command;

import it.polimi.ingsw.am01.client.tui.command.parser.LiteralParser;
import it.polimi.ingsw.am01.client.tui.command.parser.Parser;
import it.polimi.ingsw.am01.client.tui.command.parser.WhiteSpaceParser;
import it.polimi.ingsw.am01.client.tui.command.validator.PostValidator;
import it.polimi.ingsw.am01.client.tui.command.validator.PreValidator;

import java.util.List;
import java.util.function.Consumer;

public class SequenceBuilder {
    private final SequenceBuilder prev;
    private final Parser parser;
    private Consumer<CommandContext> executor;
    private PreValidator preValidator;
    private PostValidator postValidator;

    private SequenceBuilder(SequenceBuilder prev, Parser parser) {
        this.prev = prev;
        this.parser = parser;
    }

    public static SequenceBuilder token(Parser parser) {
        return new SequenceBuilder(null, parser);
    }

    public static SequenceBuilder root() {
        return token(new WhiteSpaceParser(false));
    }

    public static SequenceBuilder literal(String literal) {
        return token(new LiteralParser(literal));
    }

    public static SequenceBuilder whiteSpace() {
        return token(new WhiteSpaceParser(true));
    }

    public SequenceBuilder executes(Consumer<CommandContext> executor) {
        this.executor = executor;
        return this;
    }

    public SequenceBuilder validatePre(PreValidator preValidator) {
        this.preValidator = preValidator;
        return this;
    }

    public SequenceBuilder validatePost(PostValidator postValidator) {
        this.postValidator = postValidator;
        return this;
    }

    public SequenceBuilder then(Parser parser) {
        return new SequenceBuilder(this, parser);
    }

    public SequenceBuilder thenLiteral(String literal) {
        return this.then(new LiteralParser(literal));
    }

    public SequenceBuilder thenWhitespace() {
        return this.then(new WhiteSpaceParser(true));
    }

    private CommandNode buildWithChild(CommandNode child) {
        CommandNode node = new CommandNode(this.parser, this.executor, this.preValidator, this.postValidator, List.of(child));
        if (this.prev == null) {
            return node;
        }

        return this.prev.buildWithChild(node);
    }

    public CommandNode end() {
        CommandNode node = new CommandNode(this.parser, this.executor, this.preValidator, this.postValidator);
        if (prev == null) {
            return node;
        }

        return this.prev.buildWithChild(node);
    }

    public CommandNode endWithAlternatives(List<CommandNode> children) {
        CommandNode node = new CommandNode(this.parser, this.executor, this.preValidator, this.postValidator, children);
        if (prev == null) {
            return node;
        }

        return this.prev.buildWithChild(node);
    }
}
