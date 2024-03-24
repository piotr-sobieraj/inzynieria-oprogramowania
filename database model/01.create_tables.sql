-- DROP TABLE PosilkiWCiaguDnia;
-- DROP TABLE Posilek;
-- DROP TABLE Uzytkownik;

CREATE TABLE Posilek (
    IDPosilek       INTEGER NOT NULL PRIMARY KEY,
    Nazwa           VARCHAR2(100 CHAR) NOT NULL
);

COMMENT ON COLUMN posilek.nazwa IS
    'śniadanie, II śniadanie, obiad, podwieczorek, kolacja';



CREATE TABLE Uzytkownik (
    IDUzytkownik                          INTEGER NOT NULL PRIMARY KEY,
    Nazwa                                 VARCHAR2(100) NOT NULL,
    Plec                                  CHAR(1 BYTE) NOT NULL CHECK ( lower(Plec) IN ( 'k', 'm' ) ),
    DataUrodzenia                         DATE NOT NULL,
    ZapotrzebowanieKaloryczne             INTEGER NOT NULL,
    Wzrost                                NUMBER(6, 2) NOT NULL,
    WzrostJednostka                       CHAR(2) NOT NULL CHECK ( lower(WzrostJednostka) IN ( 'cm', 'in' ) ),
    Waga                                  NUMBER(6, 2) NOT NULL,
    WagaJednostka                         CHAR(2) NOT NULL CHECK (WagaJednostka IN ('kg', 'lb') ),
    DocelowaWagaCiala                     NUMBER(6, 2),
    DataDocelowejWagiCiala                DATE NOT NULL,
    DziennyLimitKalorii                   INTEGER NOT NULL
);


COMMENT ON COLUMN uzytkownik.wzrost IS
    'Wzrost w cm lub w stopach i calach';

COMMENT ON COLUMN uzytkownik.wzrostjednostka IS
    'cm lub ft/in';

COMMENT ON COLUMN uzytkownik.wagajednostka IS
    'kg lub funty';




CREATE TABLE PosilkiWCiaguDnia (
    IDPosilkiWCiaguDnia INTEGER NOT NULL PRIMARY KEY,
    UzytkownikID         INTEGER NOT NULL,
    Data                 DATE NOT NULL,
    PosilekID            INTEGER NOT NULL,
    KolejnyNumerWDniu    INTEGER NOT NULL,
    Kalorycznosc		 INTEGER NOT NULL,
    FOREIGN KEY (UzytkownikID) REFERENCES Uzytkownik(IDUzytkownik),
    FOREIGN KEY (PosilekID) REFERENCES Posilek(IDPosilek)    
);
