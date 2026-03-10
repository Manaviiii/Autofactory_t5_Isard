-- ============================================
-- PROVEÏDORS - Taula i dades
-- ============================================

-- Inserir Provincies (necessari per Municipis)
INSERT INTO PROVINCIA VALUES ('P1', 'Barcelona');
INSERT INTO PROVINCIA VALUES ('P2', 'Girona');

-- Inserir Municipis (necessari per Proveïdors)
INSERT INTO MUNICIPI VALUES ('M1', 'P1', 'Sabadell');
INSERT INTO MUNICIPI VALUES ('M2', 'P1', 'Terrassa');
INSERT INTO MUNICIPI VALUES ('M3', 'P1', 'Barcelona');
INSERT INTO MUNICIPI VALUES ('M4', 'P2', 'Girona');
INSERT INTO MUNICIPI VALUES ('M5', 'P2', 'Figueres');

-- Inserir Proveïdors
INSERT INTO PROVEIDOR VALUES ('PR1', 'B12345678', 'TechSupply S.L.', 'Carrer Major 123, Sabadell', 'Joan García', '600111111', 'M1');
INSERT INTO PROVEIDOR VALUES ('PR2', 'B23456789', 'Components Plus', 'Avinguda Catalunya 45, Terrassa', 'Maria López', '600222222', 'M2');
INSERT INTO PROVEIDOR VALUES ('PR3', 'B34567890', 'ElectroParts BCN', 'Rambla Catalunya 78, Barcelona', 'Pere Martí', '600333333', 'M3');
INSERT INTO PROVEIDOR VALUES ('PR4', 'B45678901', 'IndustrialTech', 'Carrer Nou 56, Girona', 'Laura Vidal', '600444444', 'M4');
INSERT INTO PROVEIDOR VALUES ('PR5', 'B56789012', 'MicroComponents', 'Plaça Major 12, Figueres', 'Jordi Puig', '600555555', 'M5');
INSERT INTO PROVEIDOR VALUES ('PR6', 'B67890123', 'Hardware Express', 'Carrer Indústria 89, Sabadell', 'Carla Ferrer', '600666666', 'M1');
INSERT INTO PROVEIDOR VALUES ('PR7', 'B78901234', 'InfoParts Global', 'Avinguda Diagonal 234, Barcelona', 'Arnau Serra', '600777777', 'M3');
INSERT INTO PROVEIDOR VALUES ('PR8', 'B89012345', 'ProTech Solutions', 'Carrer del Comerç 67, Terrassa', 'Marta Roca', '600888888', 'M2');

COMMIT;