
INSERT INTO Posilek VALUES(1, 'śniadanie');
INSERT INTO Posilek VALUES (2, 'II śniadanie');
INSERT INTO Posilek VALUES (3, 'obiad');
INSERT INTO Posilek VALUES (4, 'kolacja');
INSERT INTO Posilek VALUES (5, 'przekąska');




INSERT INTO Uzytkownik (IDUzytkownik, 
    Nazwa,                                 
    Plec,                                  
    DataUrodzenia,
    ZapotrzebowanieKaloryczne,
    Wzrost,            
    WzrostJednostka,                       
    Waga,                      
    WagaJednostka,                         
    DocelowaWagaCiala,                     
    DataDocelowejWagiCiala,                
    DziennyLimitKalorii)
VALUES (
1, 
'Anna',
'k',
DATE'1998-05-17',
2100,
172,
'cm',
68,
'kg',
63,
DATE'2024-04-01',
1800);


INSERT INTO PosilkiWCiaguDnia (
    IDPosilkiWCiaguDnia,
    UzytkownikID,
    Data,
    PosilekID,
    KolejnyNumerWDniu,
    Kalorycznosc
)
VALUES(1, 
       1,
       DATE'2024-03-07',
       1,
       1,
       250);

INSERT INTO PosilkiWCiaguDnia (
    IDPosilkiWCiaguDnia,
    UzytkownikID,
    Data,
    PosilekID,
    KolejnyNumerWDniu,
    Kalorycznosc
)
VALUES(2, 
       1,
       DATE'2024-03-07',
       2,
       2,
       200);

INSERT INTO PosilkiWCiaguDnia (
    IDPosilkiWCiaguDnia,
    UzytkownikID,
    Data,
    PosilekID,
    KolejnyNumerWDniu,
    Kalorycznosc
)
VALUES(3, 
       1,
       DATE'2024-03-07',
       5,
       3,
       500);

INSERT INTO PosilkiWCiaguDnia (
    IDPosilkiWCiaguDnia,
    UzytkownikID,
    Data,
    PosilekID,
    KolejnyNumerWDniu,
    Kalorycznosc
)
VALUES(4, 
       1,
       DATE'2024-03-07',
       4,
       4,
       500);



INSERT INTO PosilkiWCiaguDnia (
    IDPosilkiWCiaguDnia,
    UzytkownikID,
    Data,
    PosilekID,
    KolejnyNumerWDniu,
    Kalorycznosc
)
VALUES(5, 
       1,
       DATE'2024-03-09',
       1,
       1,
       320);



INSERT INTO PosilkiWCiaguDnia (
    IDPosilkiWCiaguDnia,
    UzytkownikID,
    Data,
    PosilekID,
    KolejnyNumerWDniu,
    Kalorycznosc
)
VALUES(6, 
       1,
       DATE'2024-03-09',
       3,
       2,
       450);

SELECT * FROM Uzytkownik;
SELECT * FROM Posilek;
SELECT * FROM PosilkiWCiaguDnia;

SELECT U.Nazwa, PWD.Data, PWD.KolejnyNumerWDniu, P.Nazwa, PWD.Kalorycznosc
--,U.*, PWD.*, P.* 
FROM PosilkiWCiaguDnia PWD
JOIN Uzytkownik U ON IDUzytkownik = UzytkownikID
JOIN Posilek P ON IDPosilek = PWD.PosilekID
ORDER BY Data DESC, KolejnyNumerWDniu ASC;
