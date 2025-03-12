PannonSet Feladatsor

# Előismeretek

Nézd meg a „feladatsor segédlet.odt"-ben hogy milyen témák vannak
felsorolva. Ha valamelyik nem ismerős, akkor nézz utána a témának
(mindegyik témához vannak ajánlott linkek is).

# Projekt létrehozása eclipse-ben, debug beállítása

Ha megvolt a telepítés a telepítési útmutató segítségével, akkor az
„Eclipse projekt" almappában az „Eclipse projekt.odt" fájl leírja hogyan
kell a projektet létrehozni

Ellenőrzés:

1.  A böngészőben meg lehet nyitni (apache szimbolikus link létre lett
    hozva war mappára)
2.  Megjelenik teljes képernyőn a panel (kód lefordul, GXT-s CSS mappa
    be lett másolva)
3.  Lehet breakpointot rakni a kódba, és rá is fut (debug konfiguráció
    helyesen lett beállítva)

# **Minden feladathoz érvényes kérések/javaslatok**

- A legtöbb feladatot egy új fülre (TabItem) kérjük, és lehetőleg új
  függvénybe/osztályba, hogy a többitől jól elkülönüljön, az ellenőrzés
  segítésének az érdekében!
- Ha nem kell segítség a haladáshoz akkor is 3-4 feladatonként
  ellenőriztesd le őket! Így ha a kódon javítani/bővíteni kell, akkor
  nem olyankor derül ki, amikor már több feladat is azt a kódot
  használja.
- Ha valami megjelenítő elemet használsz, és van belőle GWT és GXT-s
  osztály, akkor a GXT-set használd! Ez alól az első fájlban a Window
  osztály a kivétel, de új (virtuális) ablak feldobásához már a GXT-s
  Window-ot kell használni!
- A <https://app.ps.hu/gxt-example> oldalon lévő példákat érdemes nézni,
  a grideknél pedig szükség is lesz rájuk.
- Legyen hibakezelés arra, ha a felhasználó rossz sorrendben kattingat,
  rosszul vagy hiányosan ad meg adatokat. Nem kell egyáltalán, vagy
  túlzásba vinni a hibakezelést a rendszerrel kapcsolatos hibákra, pl
  nem elérhető az adatbázis mert elment a hálózat, szintaktikai hibát
  küldött vissza a php, stb.
- A keretrendszerben http hibakódokat sem küldünk vissza ha nem sikerül
  valami, de az önálló részben persze használható.
- A Grid-es részeknél a honlapon a példák saját entitás osztállyal
  vannak, de mi a GXT-s BaseModelData-t használjuk, ami egy HashMap-re
  hasonlít a set() függvényével. Saját entitás osztályt használni a
  GXT-s osztályokkal extra munkát igényel, ezért inkább a BaseModelDatát
  javaslom a saját részre is.
- Ha bármelyik feladat leírása nem világos, akkor azonnal kérdezz!
- A bónusz részek megoldása nem kötelező

# **Ismerkedés GWT-vel, PHP-val**

## Gomb létrehozása

Az „1."-es számú fülre rakj egy gombot!

Ellenőrzés:

- GXT-s osztályt használtál a gombhoz

## Kattintásra írja ki a beírt szöveget

**a.** Hozz létre egy második fület a főoldalon, egy új Feladat2
osztállyal!  
Csinálj bele egy szöveges mezőt, és három gombot. Mindhárom gombra
kattintva felugró üzenet jelenjen meg, ami a szöveges mező aktuális
tartalmát jeleníti meg, méghozzá a következők használatával:

- Az első gomb felirata „MessageBox-al" és a MessageBox.alert függvényt
  használja
- A második gomb felirata „Info-val" és az Info.display függvényt
  használja
- A harmadik gomb felirata „Window-al" és egy GXT-s Window-on jelenítse
  meg a szöveget

ld. a gxt példáknál a Window mappában lévők

Ellenőrzés:

- Szöveges mező és a gomb a GXT-s osztályokat használta
- A felugró üzenet és ablak követi a szöveges mező változását (nem csak
  egyszer eltárolt szöveget ír ki)

**b.** A felugró üzenet a szövegnek a fordítottját írja ki!

## A kattintás helyére rakjon egy ablakot

Hozz létre egy harmadik fület a főoldalon, egy új Feladat3 osztállyal!  
Csinálj egy LayoutContainer-t, amire ha rákattintanak, akkor a kattintás
helyétől rajzolódjon rá egy ablak (GXT-s Window). Ehhez a megfelelő
eseménykezelőt kell megtalálnod.

Tipp: addListener() függvény és Events globális változó

Ellenőrzés:

- A létrehozott LayoutContainer betölti a képernyőt (nem 0\*0 a mérete)
- A kattintás helyén van az ablak bal felső sarka

## 3\*3-as rács {#as-rács}

Hozz létre egy negyedik fület a főoldalon, egy új Feladat4 osztállyal!

**a.** Layout-ok segítségével rakj 3 gombot egy oszlopba!

**b.** Ez után bővítsd ki úgy, hogy minden sorban 3 gomb legyen 1
helyett (így összesen 9 darab).

Mindegyik gombnak a felirata legyen egy szám. Amikor rákattintunk az
egyik gombra, akkor a vele egy sorban, és egy oszlopban lévő gombokon
lévő számot növeljük meg a most kattintott gombon lévő számmal! Például
ha rákattintunk a középső számra a következőben:

|     |     |     |
|-----|-----|-----|
| 1   | 1   | 1   |
| 1   | 1   | 1   |
| 1   | 1   | 1   |

Akkor ezt várjuk:

|     |     |     |
|-----|-----|-----|
| 1   | 2   | 1   |
| 2   | 1   | 2   |
| 1   | 2   | 1   |

Majd a felső sor középső számára:

|     |     |     |
|-----|-----|-----|
| 3   | 2   | 3   |
| 2   | 3   | 2   |
| 1   | 2   | 1   |

ld. a gxt példák közül a Layouts/HBoxLayout és VBoxLayout

Bónusz: A szomszédok megtalálásához az if/else-el beégetett kinek ki a
szomszédja módszernél kifinomultabb megoldás. Kifinomult ha nem kell
lényegesen módosítani ahhoz, ha mondjuk 9\*9-esre növelnénk a rácsot.

## PHP alapok, GWT\<-\>PHP kommunikáció

**a.** Típusok, tömbök, ciklusok, algoritmusok gyakorlása. Mi az a Json,
json_encode() és json_decode() kipróbálása.

Linkekhez lásd a segédletet!

Gyakorlásnak a war mappa alá hozz létre egy alapok.php fájlt. Ezt a
böngészőből a következő url-en tudod megnézni:
http://localhost/TanProjekt/alapok.php  
következőket csináld meg benne:

- Min/max kiválasztás, keresés, x legnagyobb elem megkeresése,
  faktoriális, euklideszi sor
- Tömbbe ágyazott tömbben keresés
- Tömbbe ágyazott tömbök sorba rendezése uasort függvény segítségével
- json_encode és json_decode kipróbálása tömbbel

Kiiratáshoz az echo és a print_r függvényeket érdemes használni,
debughoz a var_dump is jól jöhet.

**b.** Az alapok.php oldal kimenetét jelenítsük meg GWT-ben GET kérést
használva egy új fülön, Feladat5 osztállyal!

Mi nem használjuk a GWT RPC-t. GET/POST kérés indításához a GWT-s
RequestBuilder osztályt kell használni. Az URL a
GWT.getHostPageBaseURL()-hoz képest legyen relatív.

Megjelenítéshez például a GXT-s TextArea osztály ajánlott.

## Számológép

**a.** Feladat6 osztályban hozz létre 2 szám beviteli mezőt, 1 szöveges
mezőt és egy gombot!  
Amikor a gombra rákattintunk, akkor az első kettőben lévő számot küldje
el a PHP-nek GET kéréssel, a php adja össze a két számot és írassa ki,
majd a php-ben kiírt eredmény GWT-ben a harmadik mezőbe kerüljön bele. A
harmadik mezőnek a tartalmát a felhasználó ne tudja módosítani, csak a
kód írjon bele. A php fájlt a war mappa alá szamologep.php-ként hozd
létre!

**b.** A két módosítható mezőre állítsd be, hogy kötelező legyen őket
kitölteni, illetve a második mezőbe ne lehessen negatív számot megadni.
Amikor a gombra rákattintanak, akkor a mezőkkel ellenőriztesd le, hogy
ezek teljesüljenek, és csak akkor küldd el a kérést!

Bónusz1: Legyen még egy szöveges mező, amibe a következő műveletek
egyikét lehet beírni: + - % \*. A GET kérésben ezt is küldjük el, és az
eredményt ennek megfelelően adja vissza. Azt fogjuk tapasztalni, hogy
valami gond van az összeadás + jelével a php oldalon. Keress rá
megoldást!

Bónusz2: Hozz létre még egy gombot, ami POST-ot használ a kérés
elküldéséhez, és php-ben kezeld le ezt is, a GET mellett! Azt fogjuk
tapasztalni, hogy a \$\_POST változó üres marad a PHP oldalon. Ez miért
van, és hogyan lehet orvosolni? Tipp: POST változó php dokumentációja

## Készíts egy Grid-et

Hozz létre egy GXT Gridet, és töltsed fel adatokkal! Az adatokhoz
ajánlott a BaseModelData használata saját entitás helyett. Nézd meg a
példákat!

A Gridnek vagy az ős ad méretet a layout-jával (pl: FitLayout) vagy
kézzel kell beállítani méretet, különben csak a fejléce fog látszódni!

Legalább 5 oszlop legyen benne, és ezek közül legyen Number, String,
Date jellegű is!

Hozz létre egy Toolbar-t és legyen rajta \'Új\' és \'Törlés\' gomb. Az
\'Új\' gomb egyelőre csak egy üres sort adjon hozzá, a \'Törlés\' gomb
pedig a kijelölt sort törölje!

Ellenőrzés:

- A GXT-s Gridet használtad, nem a GWT-set
- A kijelölt sor megtalálására a GXT-t használtad, nem saját logikát
- A törlés gomb a megfelelő sort törli

## Készíts űrlapot a Grid-hez

Az \'Új\' gombra kattintva ugorjon fel egy GXT-s Window, amin az előző
feladatban megfelelő oszlopokhoz legyenek szerkeszthető mezők, illetve
mentés/mégse gomb. A mentés gombra jelenjen meg a Gridben a felvitt
adat, és záródjon be az ablak.

Próbáld ki a különböző mezőkre a megkötéseket, pl kötelező legyen
kitölteni a mezőt, csak pozitív számot lehessen megadni stb. Addig ne
engedd menteni az űrlapot, amíg ezek nem teljesülnek (lásd validate()
függvény a mezőkön).

Ajánlott az új ablakra FormPanel-t rakni, mert az ezekhez hozzáadott
mezők szépen egymás alá igazítva rendeződnek.

## A módosításhoz is használd ugyan azt az űrlapot

A gridben ha kettőt kattintunk egy soron, akkor ugorjon fel az előző
feladatban csinált űrlap a mezőkkel kitöltve, és mentésre módosítsuk a
megfelelő adatokat.

Bónusz: az űrlap ne kommunikáljon direkte a Grid vagy a Store-ral, hanem
kapjon egy BaseModelData-t, és ha a mentésre kattintunk, akkor
eseménnyel jelezze, hogy módosítás történt. Az esemény kezelőjében
módosítsuk csak az eredeti adatot!

## Adatok beolvasása PHP-ből

Készíts egy php oldalt pár példa adattal, ami az előzőekben létrehozott
Gridnek megfelel, és írasd ki. Egy GET kéréssel töltsed fel a Gridet az
adatokkal.

## Adatok beolvasása MySQL-ből

A MySQL-ben hozz létre egy adatbázist, és egy táblát az előző
feladatokban használt Gridnek megfelelően. Töltsed fel az előző
feladatban használt példa adatokkal a táblát.

Hozz létre egy új php oldalt, ami az adatbázisból kéri le az összes
adatot, majd ezt Json formátumban kiírja (echo json_encode(\...))!

Az adatbázis lekérdezésekhez használd a Pear DB csomagot, ha nem lenne
feltelepítve akkor kérj hozzá segítséget!

Ez után a Grid innen töltse be az adatokat. Nézd meg a példákat hozzá
(Grid példák forrásában a JsonReader és a többi osztály)!

## Beszúrás/módosítás megvalósítása PHP-ben

Amikor elmentjük az űrlapot, akkor POST kéréssel küldjük el a PHP-nek,
és mentődjön el az adatbázisba (INSERT/UPDATE query)! Ez után a Grid
tartalmát teljesen újratölthetjük, nem kell kézzel módosítani a sorokat
helyben.

## Rendezés, szűrés

A Gridet lehessen rendezni, szűrni és az adatokat \*Loader-rel töltse be
kézi parse helyett, és használj PagingToolbar-t is! A szűrés és
rendezést a szerver oldalon csinálja! Nézd meg a példákat!

## Több sor kiválasztása, törlése

A gridben lehessen több sort checkbox-al kijelölni, és a törlés gomb az
összes kijelöltet törölje!

Ehhez a GXT-t használjuk, ne saját logikát! Tipp: nézz utána a
SelectionModel-nek

## Drag&Drop Gridek között

Csinálj két gridet egymás mellé, drag&drop segítségével lehessen átrakni
elemet az egyikből a másikba. (Érdemes új grideket létrehozni, nem kell
adatbázissal működnie egyelőre!)

Egyelőre használj kézzel feltöltött adatokat.

Tipp: Ha nem működne akkor az előző feladat SelectionModel-jét vedd le,
mert a checkbox-ossal nekem nem működött.

## Drag&Drop adatbázisba szinkronizálással

Töltődjön a két grid adatbázisból. Amikor áthúzunk egy elemet az
egyikből a másikba, akkor annak az elemnek a mozgatása az adatbázisba is
kerüljön bele! Amíg a mentés nem fejeződik be, addig ne engedj további
módosítást, jelenjen meg egy töltő karika a grideken.

# ~~Új projekt~~, saját konfigolás kipróbálása

A 18 vagy 19-es feladatnak megfelelő Gridet másold át, és próbáld ki a
RowEditor osztályt!
