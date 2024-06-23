package it.polimi.ingsw.am01.client.tui.command;

import it.polimi.ingsw.am01.client.tui.command.parser.LiteralParser;
import it.polimi.ingsw.am01.client.tui.command.parser.Parser;
import it.polimi.ingsw.am01.client.tui.command.parser.WhiteSpaceParser;
import it.polimi.ingsw.am01.client.tui.command.validator.PostValidator;
import it.polimi.ingsw.am01.client.tui.command.validator.PreValidator;

import java.util.List;
import java.util.function.Consumer;

/**
 * A builder that, in conjunction with {@link CommandBuilder} is used to build a command tree.
 * <p>
 * This builder is useful for creating sequences,
 * while {@link CommandBuilder} is useful for creating branching parts of the command tree.
 * A sequence is a command tree where each node has only one child.
 * <p>
 * This class has two kinds of methods:
 * <ul>
 *     <li>Methods that set properties of the current node in the sequence, like {@link #executes(Consumer)}.</li>
 *     <li>Methods that create a new node in the sequence, like {@link #then(Parser)} and {@link #thenLiteral(String)}.</li>
 * </ul>
 * <p>
 * The second kind of methods returns a new {@link SequenceBuilder} that represents the new node.
 * However, when calling {@link #end()} on the last node in the sequence,
 * the returned {@link CommandNode} will be the root of the sequence.
 */
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

    /**
     * Creates a new {@link SequenceBuilder} with the given parser.
     *
     * @param parser The parser to use.
     */
    public static SequenceBuilder token(Parser parser) {
        return new SequenceBuilder(null, parser);
    }

    /**
     * Creates a new {@link SequenceBuilder} that is meant to be used as the root of the command tree.
     * It uses a {@link WhiteSpaceParser} as the parser because commands can start with any amount of whitespace.
     */
    public static SequenceBuilder root() {
        return token(new WhiteSpaceParser(false));
    }

    /**
     * Creates a new {@link SequenceBuilder} with a {@link LiteralParser} as the parser, and the given string as literal.
     *
     * @param literal The literal to use.
     */
    public static SequenceBuilder literal(String literal) {
        return token(new LiteralParser(literal));
    }

    /**
     * Creates a new {@link SequenceBuilder} with a {@link WhiteSpaceParser} as the parser.
     */
    public static SequenceBuilder whiteSpace() {
        return token(new WhiteSpaceParser(true));
    }

    /**
     * Sets the executor for the current node in the sequence.
     *
     * @param executor The executor to use.
     * @return This builder, for chaining.
     */
    public SequenceBuilder executes(Consumer<CommandContext> executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Sets the pre-validator for the current node in the sequence.
     *
     * @param preValidator The pre-validator to use.
     * @return This builder, for chaining.
     */
    public SequenceBuilder validatePre(PreValidator preValidator) {
        this.preValidator = preValidator;
        return this;
    }

    /**
     * Sets the post-validator for the current node in the sequence.
     *
     * @param postValidator The post-validator to use.
     * @return This builder, for chaining.
     */
    public SequenceBuilder validatePost(PostValidator postValidator) {
        this.postValidator = postValidator;
        return this;
    }

    /**
     * Appends a new node to the sequence that uses the given parser.
     *
     * @param parser The parser to use for the new node.
     * @return A new {@link SequenceBuilder} that represents the new node.
     */
    public SequenceBuilder then(Parser parser) {
        return new SequenceBuilder(this, parser);
    }

    /**
     * Appends a new node to the sequence that uses a {@link LiteralParser} with the given literal.
     *
     * @param literal The literal to use for the new node.
     * @return A new {@link SequenceBuilder} that represents the new node.
     */
    public SequenceBuilder thenLiteral(String literal) {
        return this.then(new LiteralParser(literal));
    }

    /**
     * Appends a new node to the sequence that uses a {@link WhiteSpaceParser}.
     *
     * @return A new {@link SequenceBuilder} that represents the new node.
     */
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

    /**
     * Ends the sequence with the current node.
     * Builds all the nodes in the sequence and returns the root of the sequence.
     *
     * @return The root of the sequence.
     */
    public CommandNode end() {
        CommandNode node = new CommandNode(this.parser, this.executor, this.preValidator, this.postValidator);
        if (prev == null) {
            return node;
        }

        return this.prev.buildWithChild(node);
    }

    /**
     * Ends the sequence with the current node, but adds to it the given children.
     * Builds all the nodes in the sequence and returns the root of the sequence.
     * <p>
     * This is useful to add branches at the end of a sequence.
     *
     * @param children The children to add to the current node.
     * @return The root of the sequence.
     */
    public CommandNode endWithAlternatives(List<CommandNode> children) {
        CommandNode node = new CommandNode(this.parser, this.executor, this.preValidator, this.postValidator, children);
        if (prev == null) {
            return node;
        }

        return this.prev.buildWithChild(node);
    }
}
