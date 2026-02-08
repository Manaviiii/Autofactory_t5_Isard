-- Eliminar taula si existeix
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE USUARIS CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

-- Crear taula d'usuaris
CREATE TABLE USUARIS (
    username VARCHAR2(50) PRIMARY KEY,
    password VARCHAR2(100) NOT NULL,
    nom_complet VARCHAR2(100),
    email VARCHAR2(100),
    rol VARCHAR2(20) DEFAULT 'USER',
    actiu CHAR(1) DEFAULT 's' CHECK (actiu IN ('s', 'n')),
    data_creacio DATE DEFAULT SYSDATE
);

INSERT INTO USUARIS (username, password, nom_complet, email, rol) VALUES 
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeKMIuqPr2pPx5xPRmqR.2ZY3lLBGPmHm', 'Administrador', 'admin@autofactory.com', 'ADMIN');

INSERT INTO USUARIS (username, password, nom_complet, email, rol) VALUES 
('user1', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGLcZEiGDMVr5yUP1KUOYTa', 'Usuari de Prova', 'user1@autofactory.com', 'USER');

COMMIT;
