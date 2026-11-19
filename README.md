# Photocard Collection Manager

A Java console app for K-pop fans to keep track of the photocards they own and the ones they still want. It shows what each card is worth for trading, sorts the list, rejects bad input and duplicate ids, and saves the collection to a file after every change.

Programming 2 (Java OOP) project, University of Pécs.

## Prerequisites

- **JDK 17 or newer** (developed on JDK 25; the code targets Java 17).
- Internet access the first time you build or run (Maven downloads its tools).
- Nothing else: the Maven Wrapper (`./mvnw`) is included, so you do not need to install Maven.

## Build and test

```
./mvnw clean test
```

This compiles the project and runs the 22 JUnit tests.

## Run

```
./mvnw compile exec:java
```

Type the number of a menu option and press Enter. Your cards are saved to `data/collection.csv` (created on the first save, not committed to git).

To try the app with example cards, copy the sample file first:

```
mkdir -p data
cp data/sample-collection.csv data/collection.csv
./mvnw compile exec:java
```

## Sample usage

```
=== Photocard Collection ===
1. Add a card
2. Remove a card
3. List cards (sorted)
4. Show wishlist
5. Show total trade value
6. Exit
Choose 1-6: 3
Sort by: 1 = member, 2 = name, 3 = trade value (highest first)
Choose 1-3: 3
L1 | Solo Card | Mina (solo) | WANTED | price 10.00 | trade value 15.00
A2 | Jiwoong Card | Jiwoong (AHOF) | WANTED | price 6.50 | trade value 6.50
A1 | Hanbin Card | Hanbin (AHOF) | OWNED | price 5.00 | trade value 5.00
```

- **Add a card:** choose type (A = album, L = lucky draw), then id, name, member, group (empty for a solo artist), price, status (`OWNED` or `WANTED`) and album or event name.
- **Remove a card:** type its id.
- **List cards:** sort by member, name, or trade value (highest first).
- **Wishlist:** shows only the cards marked `WANTED`.
- **Total trade value:** the sum of every card's trade value. An album card is worth its price; a lucky draw card is worth 1.5 times its price.

Bad input (a price that is not a number, a negative price, an unknown status, an id that already exists) prints a message and returns to the menu. If `data/collection.csv` has a broken line, the app prints which line and stops without overwriting the file.

### File format

One card per line, 8 fields, no header: `TYPE,id,name,memberName,groupName,price,status,extra`. `TYPE` is `ALBUM` or `LUCKY`, `groupName` is empty for a solo artist, `extra` is the album name or event name. Example: `ALBUM,A1,Hanbin Card,Hanbin,AHOF,5.0,OWNED,Album One`. Names cannot contain commas.

## Implemented requirements

- **Encapsulation, abstraction, inheritance, polymorphism:** abstract `Card` with `AlbumCard` and `LuckyDrawCard`, each overriding `getTradeValue()` and `toCsvLine()`; `Collection.getTotalValue()` calls them through `Card` references.
- **Relationships with multiplicities:** `Collection` composes `Card` (1 to 0..*); `Card` to `Member` many to 1; `Group` aggregates `Member` (0..1 to 0..*).
- **Generics and sorting:** `List<Card>`, `Optional<Card>`, three `Comparator<Card>` constants.
- **Validation and a custom exception:** constructors reject invalid values; checked `DuplicateCardException`.
- **Persistence:** CSV file with try-with-resources (`CardStorage`).
- **Tests:** 22 JUnit tests that pass from a clean checkout.
- **Design pattern:** Factory (below).

## Design pattern: Factory

The saved file mixes card types, and the loading code should not need to know every card class. **`CardFactory.fromCsvLine(String)`** reads the type in a saved line and builds the right object.

- **Participating classes:** `CardFactory` (the factory), `Card` (the common type it returns), `AlbumCard` and `LuckyDrawCard` (the concrete cards), and `CardStorage` (which uses the factory when loading).
- **Benefit:** a new card type needs only a new class and one new `case` in `CardFactory`; `CardStorage`, `Collection` and the menu stay unchanged, and a bad line becomes a clear error in one place.

## Project structure

```
src/main/java/com/photocard/
  Main.java                   starts the program
  domain/                     Card, AlbumCard, LuckyDrawCard, CardStatus, Member, Group, DuplicateCardException
  service/                    Collection, CardFactory
  persistence/                CardStorage
  ui/                         ConsoleMenu
src/test/java/com/photocard/  JUnit tests
data/sample-collection.csv    example save file
docs/DESIGN.md                UML class diagram, pattern, design decisions, known limitations
docs/USER_STORIES.md          user stories
```

## Contributors

- **Jyrah Azlei Placido**: author.
- **AI assistance:** Claude (Anthropic) was used as an AI assistant for planning, code, tests and documentation.

## License

MIT, see [LICENSE](LICENSE).
