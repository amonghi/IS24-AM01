package it.polimi.ingsw.am01.client.tui.command;

import it.polimi.ingsw.am01.client.tui.command.parser.LiteralParser;
import it.polimi.ingsw.am01.client.tui.command.parser.Parser;
import it.polimi.ingsw.am01.client.tui.command.parser.WhiteSpaceParser;
import it.polimi.ingsw.am01.client.tui.command.validator.PostValidator;
import it.polimi.ingsw.am01.client.tui.command.validator.PreValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A builder that, in conjunction with {@link SequenceBuilder} is used to build a command tree.
 * <p>
 * This builder is useful for creating branching parts of the command tree,
 * while {@link SequenceBuilder} is useful for creating sequences.
 */
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

    /**
     * Creates a new {@link CommandBuilder} with the given parser.
     *
     * @param parser The parser to use.
     */
    public static CommandBuilder token(Parser parser) {
        return new CommandBuilder(parser);
    }

    /**
     * Creates a new {@link CommandBuilder} that is meant to be used as the root of the command tree.
     * It uses a {@link WhiteSpaceParser} as the parser because commands can start with any amount of whitespace.
     */
    public static CommandBuilder root() {
        return token(new WhiteSpaceParser(false));
    }

    /**
     * Creates a new {@link CommandBuilder} with a {@link LiteralParser} as the parser, and the given string as literal.
     *
     * @param literal The literal to use.
     */
    public static CommandBuilder literal(String literal) {
        return token(new LiteralParser(literal));
    }

    /**
     * Creates a new {@link CommandBuilder} with a {@link WhiteSpaceParser} as the parser.
     */
    public static CommandBuilder whiteSpace() {
        return token(new WhiteSpaceParser(true));
    }

    /**
     * Sets the executor for the command.
     *
     * @param executor The executor to use.
     * @return This builder, for chaining.
     */
    public CommandBuilder executes(Consumer<CommandContext> executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Sets the pre-validator for the command.
     *
     * @param validator The pre-validator to use.
     * @return This builder, for chaining.
     */
    public CommandBuilder validatePre(PreValidator validator) {
        this.preValidator = validator;
        return this;
    }

    /**
     * Sets the post-validator for the command.
     *
     * @param validator The post-validator to use.
     * @return This builder, for chaining.
     */
    public CommandBuilder validatePost(PostValidator validator) {
        this.postValidator = validator;
        return this;
    }

    /**
     * Adds a child to the command.
     *
     * @param child The child to add.
     * @return This builder, for chaining.
     */
    public CommandBuilder branch(CommandNode child) {
        this.children.add(child);
        return this;
    }

    /**
     * Adds a child to the command.
     * This is a convenience method that takes a {@link CommandBuilder} instead of a {@link CommandNode}.
     *
     * @param child The child to add.
     * @return This builder, for chaining.
     */
    public CommandBuilder branch(CommandBuilder child) {
        return branch(child.build());
    }

    /**
     * Builds the command node.
     *
     * @return The built command node.
     */
    public CommandNode build() {
        return new CommandNode(this.parser, this.executor, this.preValidator, this.postValidator, this.children);
    }
}
