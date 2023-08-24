package exception;

@SuppressWarnings("serial")
public class ChampNonRenseigneException extends RuntimeException {
	private String champ;

    public ChampNonRenseigneException(String champ) {
        super("Le champ " + champ + " n'est pas renseigné ou est vide.");
        this.champ = champ;
    }

    public String getChamp() {
        return champ;
    }
}
