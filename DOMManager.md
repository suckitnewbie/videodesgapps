# DOM Manager #
Trieda DOMManager sa stará o prevažnú časť aplikačnej logiky aplikácie.

# Implementácia #
Trieda DOMManager obsahuje všetku funkcionalitu pre prácu s ODF tabulkou prevedenou na dokument vo formáte XML. Metódy tejto triedy budú využívať prevažne XPath na navigáciu v dátach a DOM model na prácu potrebnú pri pridávaní a odoberaní nových položiek. Táto trieda bude mať len jeden atribút typu Document, ktorý bude reprezentovať tabulku vo formáte XML a na ňom bude vykonávať všetky potrebné transformácie.

# Funkcionalita #
Trieda DOMManager bude obsahovať nasledujúce metódy:

  1. **addMediaType**
    * pridáva nový typ méda do tabulky
    * parametrom tejto funkcie je názov nového typu a zoznam atribútov
  1. **deleteMediaType**
    * vymaže z tabulky typ média podla mena
  1. **addRecord**
    * pridáva nový záznam na médium
    * parametrom tejto funkcie je názov média a zoznam atribútov tohto média
  1. **deleteRecord**
    * vymaže z typu média záznam
    * parametrom tejto funkcie je názov média a id záznamu na vymazanie
  1. **editRecord**
    * upraví záznam na niektorom z médií
    * parametrom tejto funkcie je názov média, id záznamu a zoznam nových atribútov pre toto médium
  1. **searchRecord**
    * táto metóda dostane ako parameter kľúč, podľa ktorého prehladá tabulky a vráti všety záznamy, v ktorých sa daný kľúč nachádza

# Diagramy #
  * **Class Diagram**
![http://i44.tinypic.com/s4apma.jpg](http://i44.tinypic.com/s4apma.jpg)