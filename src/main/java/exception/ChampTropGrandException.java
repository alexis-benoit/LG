package exception;

public class ChampTropGrandException extends RuntimeException {
	private String champ;

    public ChampTropGrandException(String champ, int tailleMax, int tailleVal) {
        super("Le champ " + champ + " (" + tailleVal +") dépasse la taille limite (" + tailleMax +").");
        this.champ = champ;
    }

    public String getChamp() {
        return champ;
    }
}
