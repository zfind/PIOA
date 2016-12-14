Zadatak 1.
Bolja rjesenja dobivam kada se koristi 2-turnirska selekcija za obje selekcije. (Implementacija slucajne selekcije ne fali nego stavim k=1 za turnirsku)
Kod vecih turnira prije zapinje u lokalnom optimumu.
Radi za 100.


Zadatak 2.
Koristim slicnu implementaciju kao kod prethodnog zadatka.
Ovdje sam za rjesenja koristio listu jer tako mogu efikasno inicijalizirati veliku pocetnu populaciju.
Zanimljivo je da sam najbolje rezultate dobio uz parametar mutacije koji se linearno smanjuje. To ima smisla jer je cilj na pocetku dobro uzorkovati prostor rjesenja, dok je na kraju cilj iz trenutne populacije izluciti najbolje rjesenje.
Kod skupa Els19 nisam shvatio format (u drugoj tablici broj redaka i stupaca je razlicit).
Uz dane parametre za skupove Nug12 i Nug25 program pronalazi optimalna rjesenja.


Kod implementacije sam pazio na performanse. Trudio sam se koristiti klasicna polja i kolekcije gdje god je moguce. 
Problem duplikata sam rijesio koristenjem HashSet-a, uz efikasno racunanje hash vrijednosti.
Najbolja jedinka nalazi se uz slozenost O(n).
