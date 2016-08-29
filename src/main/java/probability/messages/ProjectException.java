package probability.messages;

import java.util.Objects;

public class ProjectException extends Exception {

    public enum ProjectError implements Displayable {

        TOO_MANY_CARDS_IN_DECK,
        NO_SPELLS_IN_DECK,
        INVALID_MULLIGAN_RULE
    }

    private ProjectError _error;

    public ProjectException(ProjectError error) {

        super(Objects.requireNonNull(error.name()));

        _error = error;
    }

    public ProjectException(ProjectError error, Throwable cause) {

        this(error);

        initCause(cause);
    }

    @Override
    public String getLocalizedMessage() {
        return Messages.getEnumText(_error);
    }
}
