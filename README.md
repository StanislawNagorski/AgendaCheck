# AgendaCheck
Project to help me, and my team better adjust work schedule of teammates to customer in-store traffic.
It takes data from to .xlsx files and creates new report.

#Technology stack
- Java
- Maven
- Apache POI

# Główne założenia

- program pobiera rozkładu godzin na dni miesiąca i konsolidajce z pliku xlsx (pilotaż),
- program pobiera dane z pilotowanego dzinnego obrotu z pliku xlsx,
- dzieli dane na konsolidacje i sklep
- zestawia je ze sobą alertując niedoinwestowane i przeinwestowane dni pod względem godzin,
- sugeruje zmianę ilości godzin,
- a może zrobnimy z niego bota podbiętego pod gmaila? eStah po otrzymaniu plików w załaczniku odsyłałby raport.


#To do

- czy mogę wydzielić style?
- listy w readerach warto zapisać zeby funkacja nie wykonywała się za każdym razem
- zakładki dla konsolidacji - wykrywanie - tworzenie&zapełnianie
- suma godzin na konsolidacje
- znaleźć w gessefie zakładkę tej konsoliidacji i:
- pobrać z niej godziny a następnie:
- wypisać godziny i idealne godziny
- dodać godziny wg potencjału eko - oderwane od godzin sektora (pamiętaj o odjęciu administracji i kas)