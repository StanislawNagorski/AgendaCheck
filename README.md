# AgendaCheck
Project to help me, and my team better adjust work schedule of teammates to customer in-store traffic.
It takes data from to .xlsx files and creates new report.

#Technology stack
- Java
- Maven
- Apache POI

# Główne założenia

- program pozwala na parametryzacje (minimalna obstawa na godziny i dni (np dostawa))
- program pobiera rozkładu godzin na dni miesiąca i konsolidajce z pliku xlsx (pilotaż),
- program pobiera dane z pilotowanego dzinnego obrotu (trafiku?) z googleDoc (czy import do xlsx?)
- dzieli dane na konsolidacje i sklep
- zestawia je ze sobą alertując niedoinwestowane i przeinwestowane dni
- sugeruje zmianę ilości godzin
- sugeruje zmianę obłożenia godzinowego (jeśli trafik lub parametry)
- a może zrobnimy z niego bota podbiętego pod gmaila? eStah po otrzymaniu plików w załaczniku odsyłałby raport.


#To do

- ~~naucz się importu danych z wyzej wymienionych :)~~
- ~~suma godzin na sklep do nowego pliku~~
- ~~obrót na sklep dzień dzień~~ i na sektory
- ~~obrób format kolumny z procentami (czy w ogóle jej potrzebujesz?)~~
- klasa która wykrywa miesiac/rok a nastepnie daje przedział dni od 01.01.1900 +2
- styl dla obrotu w złotówkach
- symulacja godzin wg obrotu
- 