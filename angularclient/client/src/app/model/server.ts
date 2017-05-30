export class Server {
    private url: string = "https://medimager.com/api";

	private terms = {
		"allergy": "Allergier",
		"biopsySite": "Biopsilokalisation",
		"diagDef": "Diagnoser",
		"diagHist": "Histopatologiska diagnoser",
		"diagTent": "Tentativa diagnoser",
		"disNow": "Nuvarande sjukdomar",
		"disPast": "Tidigare sjukdomar",
		"drug": "Regelbundna mediciner",
		"factorNeg": "Negativa faktorer",
		"factorPos": "Positiva faktorer",
		"family": "Anhöriga med liknande besvär",
		"gender": "Kön",
		"lesnOn": "Tid sedan upptäckt",
		"lesnSite": "Förändringslokalisation",
		"skinPbl": "Hudbesvär",
		"smoke": "Rökning",
		"snuff": "Snusning",
		"sympNow": "Nuvarande besvär",
		"sympSite": "Besvärslokalisation",
		"treatType": "Behandling",
		"vasNow": "Besvärssvårighet",
	}

	getUrl(): string  {
		return this.url;
	}

	translateTerm(term: string): string {
		return this.terms[term] ? this.terms[term] : term;
	}
}
