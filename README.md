# AgendaCheck
Project to help me, and my team better adjust work schedule of teammates to customer in-store traffic.
It takes data from to .xlsx files and creates new report.

#Technology stack

- Java
- Maven
- Apache POI

#What I have learn so far, in this project

- Apache POI,
- that dates are counted as days since 01.01.1900,
- testing by printing ends while project grows,
- proper naming matters, or You will lose it,
- class should be dedicated to specyfic actions
- mind-mapping helps to keep track of classes connections, 

# Główne założenia

- program pobiera rozkładu godzin na dni miesiąca i konsolidajce z pliku xlsx (pilotaż),
- program pobiera dane z pilotowanego dzinnego obrotu z pliku xlsx,
- dzieli dane na konsolidacje i sklep
- zestawia je ze sobą alertując niedoinwestowane i przeinwestowane dni pod względem godzin,
- sugeruje zmianę ilości godzin,
- a może zrobnimy z niego bota podbiętego pod gmaila? eStah po otrzymaniu plików w załaczniku odsyłałby raport.

#To do

- ~~czy mogę wydzielić style?~~
- ~~mapa: godzin dziennych i nazw z grafiku~~
- ~~mapa: miesiecznego obtoru i nazwy z pilotażu.~~
- ~~Testy do MAP~~!
- listy w readerach warto zapisać zeby funkacja nie wykonywała się za każdym razem
- zakładki dla konsolidacji - wykrywanie - tworzenie&zapełnianie
- suma godzin na konsolidacje
- wypisać godziny i idealne godziny
- dodać godziny wg potencjału eko - oderwane od godzin sektora (pamiętaj o odjęciu administracji i kas)