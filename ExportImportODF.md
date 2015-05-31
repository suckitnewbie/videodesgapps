# Export/Import dat z ODF souboru #

Pro manipulaci s ODF budeme využívat ODFDOM Toolkit API (součást Apache ODF Toolkit Project). Knihovna ODFDOM (odfdom.jar) umožňuje vytváření, přístup a manipulaci s ODF soubory bez nutnosti detailní znalosti ODF specifikace.

# Open Document Format (ODF) #

Standart ODF byl vyvinut sdružením OASIS, je to otevřený souborový formát (založený na XML) pro vytváření a výměnu dokumentů vytvořených kancelářskými aplikacemi. Formát ODF zahrnuje textové dokumenty, prezentace, grafy, tabulky i databáze. Každý ODF soubor je ve skutečnosti balíček několika XML souborů, které obsahují všechny informace o vzhledu a obsahu dokumentu. V našem projektu budeme využívat soubory typu Open Document Spreadsheet (ODS).


# Struktura ODS souboru #

Data tabulkového dokumentu jsou uložena v souboru content.xml. Všechna data jsou uložena v dceřiných elementech elementu `<office:spreadsheet>`. Každý list dokumentu, včetně informací o jeho vzhledu, je uložen v elementu `<table:table>`. Struktura tabulky je poté rozdělena na sérii elementů `<table:table-column>` (specifikace vlastností jednotlivých sloupců), poté následuje série elementů `<table:table-row>` (specifikace vlastností jednotlivých řádků) a každý řádek obsahuje sérii elementů `<table:table-cell>` (specifikace jednotlivých buněk a jejich obsahu).

# ODFDOM #
Pomocí jednotlivých funkcí této knihovny je možné manipulovat s ODS soubory. Tedy je možné vytvářet nové nebo otevírat již vytvořené soubory, následně pak měnit jejich vzhled, strukturu, obsah a tyto změny poté také ukládat. S ODFDOM se pracuje stejně jako se standardním XML DOM a jako XML parser potřebujeme Apache Xerces-J.