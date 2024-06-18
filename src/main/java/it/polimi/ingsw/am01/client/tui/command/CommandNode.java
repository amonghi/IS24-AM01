package it.polimi.ingsw.am01.client.tui.command;

import it.polimi.ingsw.am01.client.tui.command.parser.ParseException;
import it.polimi.ingsw.am01.client.tui.command.parser.Parser;
import it.polimi.ingsw.am01.client.tui.command.validator.PostValidator;
import it.polimi.ingsw.am01.client.tui.command.validator.PreValidator;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A node in the command tree.
 * <p>
 * A command tree, in short a <i>command</i>,
 * is a tree of nodes where each node represents a token in the command string.
 * <p>
 * Each token is parsed by a {@link Parser} and validated by a {@link PreValidator} and a {@link PostValidator}.
 * If a CommandNode has a {@link Consumer} attached to it, it is considered a valid exit point,
 * that is: if the command string ends with this token, then it is a valid command.
 * <p>
 * During parsing, a {@link CommandContext} is passed around and modified by each node.
 * At time of execution, the context is passed to the attached {@link Consumer}.
 *
 * @see CommandBuilder
 * @see SequenceBuilder
 */
public class CommandNode {
    private final Parser parser;
    private final List<CommandNode> children;
    private final Consumer<CommandContext> executor;
    private final PreValidator preValidator;
    private final PostValidator postValidator;

    /**
     * Create a new CommandNode with no children.
     * <p>
     * Note that not providing an executor means that this node is not a valid exit point.
     * However, since no other nodes will follow this one, you will create a dead end
     * (that is, a command that can never be executed).
     *
     * @param parser        the parser to use for this node
     * @param executor      the consumer to execute when the command is fully parsed
     * @param preValidator  the validator to use before parsing
     * @param postValidator the validator to use after parsing
     */
    public CommandNode(Parser parser, Consumer<CommandContext> executor, PreValidator preValidator, PostValidator postValidator) {
        this(parser, executor, preValidator, postValidator, List.of());
    }

    /**
     * Create a new CommandNode with children.
     *
     * @param parser        the parser to use for this node
     * @param executor      the consumer to execute when the command is fully parsed
     * @param preValidator  the validator to use before parsing
     * @param postValidator the validator to use after parsing
     * @param children      the children of this node
     */
    public CommandNode(Parser parser, Consumer<CommandContext> executor, PreValidator preValidator, PostValidator postValidator, List<CommandNode> children) {
        this.parser = parser;
        this.executor = executor;
        this.preValidator = preValidator;
        this.postValidator = postValidator;
        this.children = children;
    }

    /**
     * Parse a command string, starting with a fresh {@link CommandContext}.
     *
     * @param command the command string to parse
     * @return a {@link Result} object containing the result of the parsing
     * @see CommandNode#parse(CommandContext, String)
     */
    public Result parse(String command) {
        return parse(new CommandContext(), command);
    }

    /**
     * Parse a command string with a given {@link CommandContext}.
     * <p>
     * The parsing is done on four steps:
     * <ol>
     *     <li>Pre-validation: the {@link PreValidator} is called to validate the context before parsing</li>
     *     <li>Parsing: the {@link Parser} is called to parse the command string</li>
     *     <li>Post-validation: the {@link PostValidator} is called to validate the context after parsing</li>
     *     <li>Children: if the command string is not fully parsed, the children are called to parse the remaining part</li>
     * </ol>
     * <p>
     * If any of the steps fails, the parsing is stopped.
     * <ul>
     *      <li>The validation steps fail if a {@link ValidationException} is thrown by the validator.</li>
     *      <li>The parsing step fails if a {@link ParseException} is thrown by the parser.</li>
     * </ul>
     * <p>
     * When the parsing is done, either because the command string is fully parsed or because the parsing failed,
     * completions are collected and a {@link Result} object is returned.
     *
     * @param context the context to use during parsing
     * @param command the command string to parse
     * @return a {@link Result} object containing the result of the parsing
     */
    public Result parse(CommandContext context, String command) {
        try {
            if (this.preValidator != null) {
                this.preValidator.validate();
            }
        } catch (ValidationException e) {
            return new Result(0, getCompletionsForPartial(command));
        }

        Parser.Result tokenResult;
        try {
            tokenResult = this.parser.parse(context, command);
        } catch (ParseException e) {
            return new Result(0, getCompletionsForPartial(command));
        }

        // we match partially, suggest how to complete the command
        if (!tokenResult.isFullMatch()) {
            return new Result(tokenResult.consumed(), getCompletionsForPartial(command));
        }

        try {
            if (this.postValidator != null) {
                this.postValidator.validate(context);
            }
        } catch (ValidationException e) {
            // TODO: show error message
            return new Result(0, getCompletionsForPartial(command));
        }

        int consumed = tokenResult.consumed();
        String remaining = command.substring(consumed);

        // we match fully, and we have finished parsing
        if (remaining.isBlank()) {
            Runnable commandRunnable = this.executor != null
                    ? () -> this.executor.accept(context)
                    : null;

            // if the command is runnable, we only provide a completion if the user has already started typing a space after
            boolean noCompletion = commandRunnable != null && remaining.isEmpty();

            List<Parser.Completion> completions = List.of();
            if (!noCompletion) {
                completions = this.children.stream()
                        .flatMap(bCommandNode -> bCommandNode.getCompletionsForPartial(remaining).stream())
                        .toList();
            }

            return new Result(consumed, completions, commandRunnable);
        }

        // we match fully, but the there is still more to parse
        // we search for the first child that matches the command string and can provide a Runnable
        // or, failing that, the one with the longest match
        Result bestResult = null;
        for (CommandNode node : children) {
            Result result = node.parse(context, remaining);
            // we found a branch that matches the command, done
            if (result.getCommandRunnable().isPresent()) {
                return result.accumulateConsumed(consumed);
            }

            // this branch does not result the command
            // we keep track of the best partial result
            if (bestResult == null || bestResult.getConsumed() < result.getConsumed()) {
                bestResult = result;
            }
        }

        // If we have a partial match, we use that.
        // If we don't (because we don't have any child node),
        // we create a new one here and try to suggest something to complete the command.
        return bestResult != null
                ? bestResult.accumulateConsumed(consumed)
                : new Result(consumed, getCompletionsForPartial(remaining));
    }

    private List<Parser.Completion> getDefaultCompletions() {
        return getCompletionsForPartial("");
    }

    private List<Parser.Completion> getCompletionsForPartial(String partial) {
        try {
            if (this.preValidator != null) {
                this.preValidator.validate();
            }
        } catch (ValidationException e) {
            return List.of();
        }

        Parser.Completion completion;
        try {
            completion = this.parser.complete(partial);
        } catch (ParseException e) {
            return List.of();
        }

        // if this is a valid exit point, we don't need to suggest further
        if (this.executor != null) {
            return List.of(completion);
        }

        return this.children.stream()
                .flatMap(child -> child.getDefaultCompletions().stream())
                .map(completion::concat)
                .toList();
    }

    /**
     * A result of the parsing.
     * <p>
     * The result contains
     * <ul>
     *     <li>the number of characters consumed by the parser</li>
     *     <li>a list of possible completions for the command string</li>
     *     <li>an optional {@link Runnable} to execute the command.
     *     The runnable is present iff the command string that was parsed represented a valid command
     *     (that is, if the last parsed token was a valid exit node of the command).</li>
     * </ul>
     */
    public static class Result {
        private final int consumed;
        private final List<Parser.Completion> completions;
        private final Runnable commandRunnable;

        protected Result(int consumed, List<Parser.Completion> completions) {
            this(consumed, completions, null);
        }

        protected Result(int consumed, List<Parser.Completion> completions, Runnable commandRunnable) {
            this.consumed = consumed;
            this.completions = completions;
            this.commandRunnable = commandRunnable;
        }

        /**
         * @return the number of characters consumed
         */
        public int getConsumed() {
            return consumed;
        }

        /**
         * @return a list of possible completions for the command string
         */
        public List<Parser.Completion> getCompletions() {
            return completions;
        }

        /**
         * @return the {@link Runnable} to execute the command, if the command string was a valid command
         */
        public Optional<Runnable> getCommandRunnable() {
            return Optional.ofNullable(commandRunnable);
        }

        private Result accumulateConsumed(int consumed) {
            return new Result(this.consumed + consumed, this.completions, this.commandRunnable);
        }

        @Override
        public String toString() {
            return "Result{" +
                    "consumed=" + consumed +
                    ", completions=" + completions +
                    ", commandRunnable=" + commandRunnable +
                    '}';
        }
    }
}
