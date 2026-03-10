-- ============================================================
-- AUTOFACTORY - SCRIPT COMPLET DE BASE DE DADES
-- Oracle Database
-- Autor: Victor Villagra
-- ============================================================
-- INSTRUCCIONS:
-- 1. Executar aquest script complet a SQL Developer o SQL*Plus
-- 2. Connectar com a usuari SYSTEM o amb privilegis DBA
-- 3. L'script elimina i recrea totes les taules
-- ============================================================

-- ============================================================
-- PART 1: ELIMINACIÓ D'OBJECTES EXISTENTS
-- ============================================================

-- Eliminar triggers
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_component_modificacio'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_compres_before'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_compres_after_row'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_compres_after_stmt'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- Eliminar funció i paquet
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION GET_PREU_MIG'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PACKAGE pkg_compres_temp'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- Eliminar taules (ordre invers per respectar FK)
BEGIN EXECUTE IMMEDIATE 'DROP TABLE COMPRES CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE PROVEIDOR CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE FORMACIO_PRODUCTE CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE PRODUCTE CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE COMPONENT CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE MUNICIPI CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE ITEM CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE PROVINCIA CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE UNITAT_MESURA CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE USUARIS CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- ============================================================
-- PART 2: CREACIÓ DE TAULES
-- ============================================================

-- Taula USUARIS (autenticació amb BCrypt)
CREATE TABLE USUARIS (
    username VARCHAR2(50) PRIMARY KEY,
    password VARCHAR2(100) NOT NULL,
    nom_complet VARCHAR2(100),
    email VARCHAR2(100),
    rol VARCHAR2(20) DEFAULT 'USER',
    actiu CHAR(1) DEFAULT 's' CHECK (actiu IN ('s', 'n')),
    data_creacio DATE DEFAULT SYSDATE
);

-- Taula UNITAT_MESURA
CREATE TABLE UNITAT_MESURA (
    codi_mesura VARCHAR2(5) PRIMARY KEY,
    nom_unitat VARCHAR2(20) NOT NULL
);

-- Taula PROVINCIA
CREATE TABLE PROVINCIA (
    codi_prov VARCHAR2(10) PRIMARY KEY,
    nom VARCHAR2(20) NOT NULL
);

-- Taula MUNICIPI
CREATE TABLE MUNICIPI (
    codi_mun VARCHAR2(10) PRIMARY KEY,
    codi_prov VARCHAR2(10) NOT NULL,
    nom VARCHAR2(20) NOT NULL,
    FOREIGN KEY (codi_prov) REFERENCES PROVINCIA(codi_prov)
);

-- Taula ITEM (superclasse per Components i Productes)
CREATE TABLE ITEM (
    codi_item VARCHAR2(10) PRIMARY KEY,
    es_producte CHAR(1) NOT NULL CHECK (es_producte IN ('s','n')),
    nom VARCHAR2(20) NOT NULL,
    descripcio VARCHAR2(100),
    stock NUMBER DEFAULT 0 NOT NULL,
    foto BLOB NOT NULL
);

-- Taula COMPONENT (hereta d'ITEM)
CREATE TABLE COMPONENT (
    codi_com VARCHAR2(10) PRIMARY KEY,
    codi_fabricant VARCHAR2(10) NOT NULL,
    preu_mig NUMBER DEFAULT 0 NOT NULL,
    unitat_mesura VARCHAR2(5) NOT NULL,
    FOREIGN KEY (unitat_mesura) REFERENCES UNITAT_MESURA(codi_mesura),
    FOREIGN KEY (codi_com) REFERENCES ITEM(codi_item)
);

-- Taula PRODUCTE (hereta d'ITEM)
CREATE TABLE PRODUCTE (
    codip VARCHAR2(10) PRIMARY KEY,
    FOREIGN KEY (codip) REFERENCES ITEM(codi_item)
);

-- Taula FORMACIO_PRODUCTE (BOM - Bill of Materials)
CREATE TABLE FORMACIO_PRODUCTE (
    codi_producte VARCHAR2(10),
    codi_item VARCHAR2(10),
    quantitat NUMBER NOT NULL,
    PRIMARY KEY (codi_producte, codi_item),
    FOREIGN KEY (codi_producte) REFERENCES PRODUCTE(codip),
    FOREIGN KEY (codi_item) REFERENCES ITEM(codi_item),
    CHECK (quantitat > 0),
    CHECK (codi_producte <> codi_item)
);

-- Taula PROVEIDOR
CREATE TABLE PROVEIDOR (
    codi VARCHAR2(10) PRIMARY KEY,
    CIF VARCHAR2(20) UNIQUE NOT NULL,
    rao_social VARCHAR2(100) NOT NULL,
    linia_adreca_facturacio VARCHAR2(200) NOT NULL,
    persona_contacte VARCHAR2(50),
    telf_contacte VARCHAR2(20),
    municipi VARCHAR2(10) NOT NULL,
    FOREIGN KEY (municipi) REFERENCES MUNICIPI(codi_mun)
);

-- Taula COMPRES (relació Component-Proveïdor amb preu)
CREATE TABLE COMPRES (
    codi_comp VARCHAR2(10) NOT NULL,
    codi_prov VARCHAR2(10) NOT NULL,
    preu NUMBER NOT NULL,
    PRIMARY KEY (codi_comp, codi_prov),
    FOREIGN KEY (codi_comp) REFERENCES COMPONENT(codi_com),
    FOREIGN KEY (codi_prov) REFERENCES PROVEIDOR(codi),
    CHECK (preu > 0)
);

-- ============================================================
-- PART 3: FUNCIONS, PAQUETS I TRIGGERS
-- ============================================================

-- Funció per calcular el preu mig d'un component
CREATE OR REPLACE FUNCTION GET_PREU_MIG(p_codi_comp VARCHAR2)
RETURN NUMBER
IS
    v_preu_mig NUMBER := 0;
BEGIN
    SELECT NVL(AVG(preu), 0)
    INTO v_preu_mig
    FROM COMPRES
    WHERE codi_comp = p_codi_comp;
    RETURN v_preu_mig;
END;
/

-- Paquet amb variables de control per triggers
CREATE OR REPLACE PACKAGE pkg_compres_temp AS
    TYPE t_codi_comp IS TABLE OF VARCHAR2(10);
    v_codi_comp t_codi_comp := t_codi_comp();
    v_actualitzant_preu BOOLEAN := FALSE;
END pkg_compres_temp;
/

-- Trigger BEFORE sobre COMPONENT
-- En INSERT calcula el preu mig automàticament
-- En UPDATE impedeix canviar el codi o el preu mig manualment
CREATE OR REPLACE TRIGGER trg_component_modificacio
BEFORE INSERT OR UPDATE ON COMPONENT
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :NEW.preu_mig := GET_PREU_MIG(:NEW.codi_com);
    ELSIF UPDATING THEN
        IF NOT pkg_compres_temp.v_actualitzant_preu THEN
            IF :NEW.preu_mig <> :OLD.preu_mig THEN
                RAISE_APPLICATION_ERROR(-20001, 'No es permet modificar preu_mig directament');
            END IF;
        END IF;
        IF :NEW.codi_com <> :OLD.codi_com THEN
            RAISE_APPLICATION_ERROR(-20002, 'No es permet modificar codi_com');
        END IF;
    END IF;
END;
/

-- Trigger BEFORE sobre COMPRES
-- Impedeix canviar els codis de component o proveïdor
CREATE OR REPLACE TRIGGER trg_compres_before
BEFORE UPDATE ON COMPRES
FOR EACH ROW
BEGIN
    IF :NEW.codi_comp <> :OLD.codi_comp THEN
        RAISE_APPLICATION_ERROR(-20010, 'No es permet modificar el codi del component');
    END IF;
    IF :NEW.codi_prov <> :OLD.codi_prov THEN
        RAISE_APPLICATION_ERROR(-20011, 'No es permet modificar el codi del proveïdor');
    END IF;
END;
/

-- Trigger AFTER ROW sobre COMPRES
-- Guarda els components afectats al paquet
CREATE OR REPLACE TRIGGER trg_compres_after_row
AFTER INSERT OR UPDATE OR DELETE ON COMPRES
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        pkg_compres_temp.v_codi_comp.EXTEND;
        pkg_compres_temp.v_codi_comp(pkg_compres_temp.v_codi_comp.COUNT) := :NEW.codi_comp;
    ELSIF UPDATING THEN
        pkg_compres_temp.v_codi_comp.EXTEND;
        pkg_compres_temp.v_codi_comp(pkg_compres_temp.v_codi_comp.COUNT) := :NEW.codi_comp;
    ELSIF DELETING THEN
        pkg_compres_temp.v_codi_comp.EXTEND;
        pkg_compres_temp.v_codi_comp(pkg_compres_temp.v_codi_comp.COUNT) := :OLD.codi_comp;
    END IF;
END;
/

-- Trigger AFTER STATEMENT sobre COMPRES
-- Recalcula el preu mig dels components afectats
CREATE OR REPLACE TRIGGER trg_compres_after_stmt
AFTER INSERT OR UPDATE OR DELETE ON COMPRES
DECLARE
BEGIN
    pkg_compres_temp.v_actualitzant_preu := TRUE;
    FOR i IN 1 .. pkg_compres_temp.v_codi_comp.COUNT LOOP
        UPDATE COMPONENT c
        SET preu_mig = (
            SELECT NVL(AVG(preu), 0)
            FROM COMPRES co
            WHERE co.codi_comp = pkg_compres_temp.v_codi_comp(i)
        )
        WHERE c.codi_com = pkg_compres_temp.v_codi_comp(i);
    END LOOP;
    pkg_compres_temp.v_codi_comp.DELETE;
    pkg_compres_temp.v_actualitzant_preu := FALSE;
END;
/

-- ============================================================
-- PART 4: DADES INICIALS
-- ============================================================

-- Usuaris (contrasenyes BCrypt: admin=admin123, user1=user123)
INSERT INTO USUARIS (username, password, nom_complet, email, rol) VALUES 
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeKMIuqPr2pPx5xPRmqR.2ZY3lLBGPmHm', 'Administrador', 'admin@autofactory.com', 'ADMIN');
INSERT INTO USUARIS (username, password, nom_complet, email, rol) VALUES 
('user1', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGLcZEiGDMVr5yUP1KUOYTa', 'Usuari de Prova', 'user1@autofactory.com', 'USER');

-- Unitats de mesura
INSERT INTO UNITAT_MESURA VALUES ('U', 'Unitat');
INSERT INTO UNITAT_MESURA VALUES ('KG', 'Quilogram');
INSERT INTO UNITAT_MESURA VALUES ('L', 'Litre');

-- Provincies
INSERT INTO PROVINCIA VALUES ('P1', 'Barcelona');
INSERT INTO PROVINCIA VALUES ('P2', 'Girona');

-- Municipis
INSERT INTO MUNICIPI VALUES ('M1', 'P1', 'Sabadell');
INSERT INTO MUNICIPI VALUES ('M2', 'P1', 'Terrassa');
INSERT INTO MUNICIPI VALUES ('M3', 'P1', 'Barcelona');
INSERT INTO MUNICIPI VALUES ('M4', 'P2', 'Girona');
INSERT INTO MUNICIPI VALUES ('M5', 'P2', 'Figueres');

-- Items (Productes)
INSERT INTO ITEM VALUES ('P1', 's', 'Ordinador', 'Producte complet ordinador', 0, EMPTY_BLOB());
INSERT INTO ITEM VALUES ('P2', 's', 'Telèfon', 'Smartphone bàsic', 0, EMPTY_BLOB());
INSERT INTO ITEM VALUES ('P3', 's', 'Impressora', 'Impressora multifunció', 0, EMPTY_BLOB());

-- Items (Components)
INSERT INTO ITEM VALUES ('C1', 'n', 'Processador', 'CPU Intel', 0, EMPTY_BLOB());
INSERT INTO ITEM VALUES ('C2', 'n', 'Memòria RAM', '8GB DDR4', 0, EMPTY_BLOB());
INSERT INTO ITEM VALUES ('C3', 'n', 'Disc Dur', '1TB SSD', 0, EMPTY_BLOB());
INSERT INTO ITEM VALUES ('C4', 'n', 'Font Alimentació', '650W', 0, EMPTY_BLOB());
INSERT INTO ITEM VALUES ('C5', 'n', 'Bateria Telèfon', 'Li-Ion 3000mAh', 0, EMPTY_BLOB());
INSERT INTO ITEM VALUES ('C6', 'n', 'Càmera Telèfon', '12MP', 0, EMPTY_BLOB());
INSERT INTO ITEM VALUES ('C7', 'n', 'Cartutx Tinta', 'Negre', 0, EMPTY_BLOB());
INSERT INTO ITEM VALUES ('C8', 'n', 'Rodets Paper', 'Per impressora', 0, EMPTY_BLOB());
INSERT INTO ITEM VALUES ('C9', 'n', 'Teclat extern', 'USB', 0, EMPTY_BLOB());
INSERT INTO ITEM VALUES ('C10', 'n', 'Ratolí extern', 'USB', 0, EMPTY_BLOB());

-- Productes
INSERT INTO PRODUCTE VALUES ('P1');
INSERT INTO PRODUCTE VALUES ('P2');
INSERT INTO PRODUCTE VALUES ('P3');

-- Components
INSERT INTO COMPONENT VALUES ('C1', 'FAB1', 0, 'U');
INSERT INTO COMPONENT VALUES ('C2', 'FAB2', 0, 'U');
INSERT INTO COMPONENT VALUES ('C3', 'FAB3', 0, 'U');
INSERT INTO COMPONENT VALUES ('C4', 'FAB4', 0, 'U');
INSERT INTO COMPONENT VALUES ('C5', 'FAB5', 0, 'U');
INSERT INTO COMPONENT VALUES ('C6', 'FAB6', 0, 'U');
INSERT INTO COMPONENT VALUES ('C7', 'FAB7', 0, 'U');
INSERT INTO COMPONENT VALUES ('C8', 'FAB8', 0, 'U');
INSERT INTO COMPONENT VALUES ('C9', 'FAB9', 0, 'U');
INSERT INTO COMPONENT VALUES ('C10', 'FAB10', 0, 'U');

-- Formació de Productes (BOM)
-- Ordinador: Processador + 2x RAM + Telèfon (subproducte)
INSERT INTO FORMACIO_PRODUCTE VALUES ('P1', 'C1', 1);
INSERT INTO FORMACIO_PRODUCTE VALUES ('P1', 'C2', 2);
INSERT INTO FORMACIO_PRODUCTE VALUES ('P1', 'P2', 1);

-- Telèfon: Bateria + Càmera + Teclat
INSERT INTO FORMACIO_PRODUCTE VALUES ('P2', 'C5', 1);
INSERT INTO FORMACIO_PRODUCTE VALUES ('P2', 'C6', 1);
INSERT INTO FORMACIO_PRODUCTE VALUES ('P2', 'C9', 1);

-- Impressora: 2x Cartutx + Rodets + Ratolí
INSERT INTO FORMACIO_PRODUCTE VALUES ('P3', 'C7', 2);
INSERT INTO FORMACIO_PRODUCTE VALUES ('P3', 'C8', 1);
INSERT INTO FORMACIO_PRODUCTE VALUES ('P3', 'C10', 1);

-- Proveïdors
INSERT INTO PROVEIDOR VALUES ('PR1', 'B12345678', 'TechSupply S.L.', 'Carrer Major 123, Sabadell', 'Joan García', '600111111', 'M1');
INSERT INTO PROVEIDOR VALUES ('PR2', 'B23456789', 'Components Plus', 'Avinguda Catalunya 45, Terrassa', 'Maria López', '600222222', 'M2');
INSERT INTO PROVEIDOR VALUES ('PR3', 'B34567890', 'ElectroParts BCN', 'Rambla Catalunya 78, Barcelona', 'Pere Martí', '600333333', 'M3');
INSERT INTO PROVEIDOR VALUES ('PR4', 'B45678901', 'IndustrialTech', 'Carrer Nou 56, Girona', 'Laura Vidal', '600444444', 'M4');
INSERT INTO PROVEIDOR VALUES ('PR5', 'B56789012', 'MicroComponents', 'Plaça Major 12, Figueres', 'Jordi Puig', '600555555', 'M5');
INSERT INTO PROVEIDOR VALUES ('PR6', 'B67890123', 'Hardware Express', 'Carrer Indústria 89, Sabadell', 'Carla Ferrer', '600666666', 'M1');
INSERT INTO PROVEIDOR VALUES ('PR7', 'B78901234', 'InfoParts Global', 'Avinguda Diagonal 234, Barcelona', 'Arnau Serra', '600777777', 'M3');
INSERT INTO PROVEIDOR VALUES ('PR8', 'B89012345', 'ProTech Solutions', 'Carrer del Comerç 67, Terrassa', 'Marta Roca', '600888888', 'M2');

-- Compres (preus components per proveïdor)
-- El trigger actualitzarà automàticament el preu_mig dels components
INSERT INTO COMPRES VALUES ('C1', 'PR1', 210);
INSERT INTO COMPRES VALUES ('C1', 'PR2', 205);
INSERT INTO COMPRES VALUES ('C2', 'PR3', 65);
INSERT INTO COMPRES VALUES ('C2', 'PR4', 63);
INSERT INTO COMPRES VALUES ('C3', 'PR4', 95);
INSERT INTO COMPRES VALUES ('C4', 'PR5', 75);
INSERT INTO COMPRES VALUES ('C5', 'PR6', 45);
INSERT INTO COMPRES VALUES ('C6', 'PR7', 55);
INSERT INTO COMPRES VALUES ('C7', 'PR8', 35);
INSERT INTO COMPRES VALUES ('C8', 'PR1', 22);
INSERT INTO COMPRES VALUES ('C9', 'PR2', 28);
INSERT INTO COMPRES VALUES ('C9', 'PR3', 30);
INSERT INTO COMPRES VALUES ('C10', 'PR5', 18);
INSERT INTO COMPRES VALUES ('C10', 'PR6', 20);

COMMIT;

-- ============================================================
-- FI DE L'SCRIPT
-- ============================================================
-- Verificació: executar aquestes consultes per comprovar
-- SELECT * FROM USUARIS;
-- SELECT * FROM COMPONENT;
-- SELECT * FROM PRODUCTE;
-- SELECT * FROM PROVEIDOR;
-- SELECT * FROM COMPRES;
-- SELECT * FROM FORMACIO_PRODUCTE;
-- ============================================================
