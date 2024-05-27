package it.polimi.ingsw.am01.tui.command;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class CommandNode {
    private final Parser parser;
    private final List<CommandNode> children;
    private final Consumer<CommandContext> executor;

    public CommandNode(Parser parser, Consumer<CommandContext> executor) {
        this(parser, executor, List.of());
    }

    public CommandNode(Parser parser, Consumer<CommandContext> executor, List<CommandNode> children) {
        this.parser = parser;
        this.executor = executor;
        this.children = children;
    }

    public Result parse(CommandContext context, String command) {
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

        int consumed = tokenResult.consumed();
        String remaining = command.substring(consumed);

        // we match fully, and we have finished parsing
        if (remaining.isBlank()) {
            Runnable commandRunnable = this.executor != null
                    ? () -> this.executor.accept(context)
                    : null;

            // if the command is runnable, we only provide a completion if the user has already started typing a space after
            boolean noCompletion = commandRunnable != null && remaining.isEmpty();

            List<String> completions = List.of();
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

    private List<String> getDefaultCompletions() {
        return getCompletionsForPartial("");
    }

    private List<String> getCompletionsForPartial(String partial) {
        Optional<String> completion = this.parser.complete(partial);
        if (completion.isEmpty()) {
            return List.of();
        }

        // if this is a valid exit point, we don't need to suggest further
        if (this.executor != null) {
            return List.of(completion.get());
        }

        return this.children.stream()
                .flatMap(child -> child.getDefaultCompletions().stream()
                        .map(childCompletion -> completion.get() + childCompletion))
                .toList();
    }

    public static class Result {
        private final int consumed;
        private final List<String> completions;
        private final Runnable commandRunnable;

        public Result(int consumed, List<String> completions) {
            this(consumed, completions, null);
        }

        public Result(int consumed, List<String> completions, Runnable commandRunnable) {
            this.consumed = consumed;
            this.completions = completions;
            this.commandRunnable = commandRunnable;
        }

        public int getConsumed() {
            return consumed;
        }

        public List<String> getCompletions() {
            return completions;
        }

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
