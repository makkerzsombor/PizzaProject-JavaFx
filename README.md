# Pizza Rendelő Adminisztrációs Felület
Ez a projekt egy Pizza Rendelő Adminisztrációs felületet valósít meg, amely lehetővé teszi az adminisztrátorok számára a pizza rendelések kezelését. A felhasználók nem tudnak regisztrálni, csak az adminisztrátorok tudnak bejelentkezni. Az adminisztrátorok létrehozhatnak, frissíthetnek és törölhetnek pizzákat, valamint felhasználókat is módosíthatnak és törölhetnek.

A projekt JavaFX, Maven és IntelliJ használatával készült. A működéshez el kell indítni a [Backend projektet](https://github.com/SBalint2002/PizzaProject-spring.git) illetve egy [MariaDB](https://mariadb.org/) adatbázis-kezelőt.

## Futtatás
A projekt Java 11-ben íródott. A futtatáshoz szükséges:

- Backend: [PizzaProject Backend](https://github.com/SBalint2002/PizzaProject-spring.git)
- Fejlesztői környezet: [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- Keretrendszer: JavaFX
- Adatbázis kezelő: [MariaDB](https://mariadb.org/)
- Csomagkezelő: [Maven](https://mvnrepository.com/artifact/org.springframework.boot)

### Forditás/Telepítés
Ha nincs telepítve az alkalmazás, akkor a következő lépéseket kell végrehajtani:

- Klónozza le a projektet a Gitből a következő paranccsal:

```bash
git clone https://github.com/makkerzsombor/PizzaProject-JavaFx
```
- Indítsa el az adatbázist és a Spring Boot backendet.
- Nyissa meg az IntelliJ IDEA-t és töltse be a projektet.

Futtassa az alkalmazást az [_Application.java_](https://github.com/makkerzsombor/PizzaProject-JavaFx/blob/main/src/main/java/hu/pizza/pizzaproject/Application.java) fájlból (hu.pizza.pizzaproject).

### Teszt felhasználó
Az alábbi adatokkal lehet bejelentkezni a teszteléshez egy Adminisztrátor fiókkal:

Email: ```tesztelek@gmail.com```

Jelszó: ```Adminadmin1```

## Funkciók
Az adminisztrátorok számára a következő funkciók érhetők el:

- Bejelentkezés
- Pizza létrehozása, frissítése és törlése
- Felhasználók módosítása és törlése
- Új rendelések listázása, amelyek még nincsenek készre jelölve

Az alkalmazás bal oldalán található egy módosító gomb, amely mindig látható. A kiválasztott elemet a módosító gombra kattintva lehet szerkeszteni (például pizza vagy felhasználó).
