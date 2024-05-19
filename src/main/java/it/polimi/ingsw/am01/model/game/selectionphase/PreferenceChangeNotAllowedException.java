package it.polimi.ingsw.am01.model.game.selectionphase;

public class PreferenceChangeNotAllowedException extends IllegalStateException {
    public PreferenceChangeNotAllowedException() {
        super("Changing preference is not allowed");
    }
}
