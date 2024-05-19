package it.polimi.ingsw.am01.model.game.selectionphase;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This strategy is responsible for
 * <ul>
 *     <li>determining what options are available to each selector in the {@link SelectionPhase}</li>
 *     <li>whether the selectors are allowed to change the expressed preference</li>
 *     <li>determining when the {@link SelectionPhase} can be considered concluded</li>
 * </ul>
 *
 * @param <O> the type of the options.
 * @param <I> the type that represents the identity of the "choosers".
 */
public interface SelectionPhaseStrategy<O, I> {
    /**
     * Creates the selector for the {@link SelectionPhase}
     *
     * @param selectionPhase the instance of {@link SelectionPhase} that is requesting that the selector be created.
     * @return a list of selectors
     * @see SelectionPhase#getSelectorFor(I)
     */
    List<SelectionPhase<O, I>.Selector> createSelectors(SelectionPhase<O, I> selectionPhase);

    /**
     * Determines whether the {@link SelectionPhase} should be considered concluded.
     * <p>
     * If this method returns and empty Optional, then the {@link SelectionPhase} is not considered concluded.
     * If the optional is not empty the {@link SelectionPhase} is considered concluded
     * and the contents of the optional are used as the results of the selection.
     *
     * @param selectors the list of selector associated with this {@link SelectionPhase}.
     *                  (This is the list that was previously returned by {@link #createSelectors(SelectionPhase)})
     * @return An optional that will be used to decide whether the selection phase is concluded and what its results are.
     * @see SelectionPhase#isConcluded()
     * @see SelectionPhase#getResults()
     */
    Optional<Map<I, O>> tryConclude(List<SelectionPhase<O, I>.Selector> selectors);
}
