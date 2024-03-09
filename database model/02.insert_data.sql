INSERT INTO NazwyPosilkow VALUES(1, 'śniadanie');
INSERT INTO NazwyPosilkow VALUES (2, 'II śniadanie');
INSERT INTO NazwyPosilkow VALUES (3, 'obiad');
INSERT INTO NazwyPosilkow VALUES (4, 'kolacja');
INSERT INTO NazwyPosilkow VALUES (5, 'przekąska');




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



SELECT * FROM Uzytkownik;
SELECT * FROM NazwyPosilkow;




