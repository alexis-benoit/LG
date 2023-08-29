package exception;

import java.util.List;

public class ChampInvalideException extends Exception {
	private List<String> errors;
    private String resource;

    public ChampInvalideException(List<String> errors, String resource) {
        super("Une erreur de champ invalide est survenue");
        this.errors = errors;
        this.resource = resource;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getResource() {
        return resource;
    }
}