package it.polimi.ingsw.am01.client.tui.component.layout.flex;

import it.polimi.ingsw.am01.client.tui.component.Component;

/**
 * A child of a Flex layout.
 * A child can be either "fixed" or "flexible".
 * <p>
 * FlexChild contains the actual component that is a child of the flex layout.
 *
 * @see Flex
 */
public sealed interface FlexChild {

    /**
     * @return The child component that this FlexChild represents.
     */
    Component child();

    /**
     * A fixed child of a Flex layout.
     *
     * @param child The child component.
     */
    record Fixed(Component child) implements FlexChild {
    }

    /**
     * A flexible child of a Flex layout.
     *
     * @param growFactor The "grow factor" of the child.
     *                   This is a number that determines how much space the child should take up relative to other flexible children.
     * @param child      The child component.
     */
    record Flexible(int growFactor, Component child) implements FlexChild {
    }
}
