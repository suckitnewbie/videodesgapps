# Google Drive #
Služba Google Drive poskytuje uživatelům webové úložiště pro vytváření, načítání, ukládání a sdílení souborů. V aplikaci VideoDesGAppas bude služba využita pro ukládání dokumentů s domácí videotékou a jejich opětovné načítání.

# Připojení ke Google Drive #
Všechny požadavky na Google Drive musí být autorizovány autentizovaným uživatelem pomocí OAuth 2.0 protokolu.

Postup:
  1. Uživatel je přesměrován na OAuth dialog, jehož prostřednictvím může aplikaci poskytnout přístup ke svým datům uloženým na Google Drive
  1. Pokud jej schválí, je vygenerován autorizační kód.
  1. Uživatel vloží autorizační kód do aplikace
  1. Aplikace vytvoří autorizované připojení ke Google Drive

# Implementace #
Komponeneta pro práci se službou Google Drive se skládá ze dvou tříd:
  * GoogleConnection
  * GoogleDriveService

## Třída GoogleConnection ##
Každá instance třídy reprezentuje jedinečné připojení ke službám Google. Vzhledem k tomu, že se jedná o desktopovou aplikaci, bude v každém okamžiku existovat pouze jediné připojení ke službám Google -> využijeme návrhový vzor singleton.
Metody:
  * public static GoogleConnection getConnection(). Vrací instanci jedinou instanci GoogleConnection v aplikaci.
  * public String getAuthentizationUrl(). Vrací odkaz na OAuth dialog pro přihlášení uživatele ke svému účtu Google a autorizování aplikace VideoDesGApps.
  * public boolean connect(String code). Vytvoří připojení ke službě Google Drive a autorizuje jej pomocí předaného autorizačního kódu.
  * public GoogleDriveService buildService(). Pokud je vytvořeno připojení, vrátí instanci GoogleDriveService, jinak null.
  * public boolean close(). Uzavře připojení.

## Třída GoogleDriveService ##
Reprezentuje síťové úložiště Google Drive. Poskytuje metody pro stahování, otevírání a ukládání souborů.

# Příklad použití #
```
GoogleConnection gc = new GoogleConnection();
GoogleDriveService gds;

String url = gc.getAuthentizationUrl();
System.out.println("Otevrete nasledujici URL a zadejte vygenerovany kod:");
System.out.println("  " + url);
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
String code = br.readLine();
if (gc.connect(code)) {
  gds = gc.buildService();
  // Prace se sluzbou Google Drive
  gds.getFiles();
  // ...
}
```