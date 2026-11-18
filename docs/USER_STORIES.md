# User stories

Each story has a short acceptance check you can try in the running app (`./mvnw compile exec:java`).

| # | As a... | I want to... | So that... | Acceptance check |
|---|---|---|---|---|
| 1 | collector | add a card with its member, group, price and status | my binder is recorded | Menu 1 shows "Added: ..." and the card appears in the list. |
| 2 | collector | add a card for a solo artist with no group | solo artists work too | Leave the group empty; the list shows the member as "(solo)". |
| 3 | collector | see my card from an album and my card from a lucky draw valued differently | I know what each is worth for trading | An album card shows trade value = price; a lucky draw card shows price x 1.5. |
| 4 | collector | be told when I add an id that already exists | I do not record the same card twice | Adding id `A1` twice prints "A card with id A1 already exists." and keeps one card. |
| 5 | collector | see only the cards I still want | I know what to look for | Menu 4 lists only `WANTED` cards. |
| 6 | collector | sort my cards by member, name or trade value | I can find cards quickly | Menu 3, then 1, 2 or 3; trade value shows the highest first. |
| 7 | collector | see the total trade value of my collection | I know what the binder is worth | Menu 5 prints "Total trade value: ...". |
| 8 | collector | remove a card | I can fix mistakes or sell a card | Menu 2 with an id removes it; an unknown id prints "No card with id ...". |
| 9 | collector | have my cards saved automatically | I do not lose my record | Add a card, exit, start again: the card is still there. |
| 10 | collector | get a clear message instead of a crash when I type something wrong | I can fix it and continue | A price like `abc` or `-5`, or a status like `X`, prints a message and returns to the menu. |
| 11 | collector | be told which line of the file is broken | I can fix my file | A bad line makes the app print "Line n: ..." and stop without overwriting the file. |
