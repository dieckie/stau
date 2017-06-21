package de.lassus.engine;

public interface AI {
	
    /**
     * Wird beim Erstellen der AI aufgerufen
     * @param c Das Auto, das die AI kontrollieren soll
     */
	void init(Car c);
	/**
	 * Wird bei jedem Simulationsschritt aufgerufen.
	 * @return Die Beschleunigung, welche das Auto ausüben soll.
	 */
	double act();

}
