--Borrar
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_component_modificacio'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_compres_before'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_compres_after_row'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER trg_compres_after_stmt'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP FUNCTION GET_PREU_MIG'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP PACKAGE pkg_compres_temp'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

--Función que calcula el precio medio de un componente
CREATE OR REPLACE FUNCTION GET_PREU_MIG(p_codi_comp VARCHAR2)
RETURN NUMBER
IS
    v_preu_mig NUMBER := 0;
BEGIN
    SELECT NVL(AVG(preu),0)
    INTO v_preu_mig
    FROM COMPRES
    WHERE codi_comp = p_codi_comp;

    RETURN v_preu_mig;
END;
/

--Trigger sobre COMPONENT
--En insert calcula el precio medio automáticamente
--En update impide cambiar el código o el precio medio
CREATE OR REPLACE TRIGGER trg_component_modificacio
BEFORE INSERT OR UPDATE ON COMPONENT
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :NEW.preu_mig := GET_PREU_MIG(:NEW.codi_com);
    ELSIF UPDATING THEN
        -- si el cambio lo hace el trigger de compras, no lanza error
        IF NOT pkg_compres_temp.v_actualitzant_preu THEN
            IF :NEW.preu_mig <> :OLD.preu_mig THEN
                RAISE_APPLICATION_ERROR(-20001, 'No es permet modificar preu_mig');
            END IF;
        END IF;

        IF :NEW.codi_com <> :OLD.codi_com THEN
            RAISE_APPLICATION_ERROR(-20002, 'No es permet modificar codi_com');
        END IF;
    END IF;
END;
/

--Trigger BEFORE sobre COMPRES
--Impide cambiar los códigos de componente o proveedor
CREATE OR REPLACE TRIGGER trg_compres_before
BEFORE UPDATE ON COMPRES
FOR EACH ROW
BEGIN
    IF :NEW.codi_comp <> :OLD.codi_comp THEN
        RAISE_APPLICATION_ERROR(-20010, 'No es permet modificar el codi del component.');
    END IF;

    IF :NEW.codi_prov <> :OLD.codi_prov THEN
        RAISE_APPLICATION_ERROR(-20011, 'No es permet modificar el codi del proveïdor.');
    END IF;
END;
/

-- Paquete con una variable de control
CREATE OR REPLACE PACKAGE pkg_compres_temp AS
    TYPE t_codi_comp IS TABLE OF VARCHAR2(10);
    v_codi_comp t_codi_comp := t_codi_comp();
    v_actualitzant_preu BOOLEAN := FALSE; -- bandera para permitir actualización automática
END pkg_compres_temp;
/

--Trigger AFTER ROW sobre COMPRES
--Guarda los componentes afectados en el paquete
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

--Trigger AFTER STATEMENT sobre COMPRES
--Recalcula el precio medio de los componentes afectados
CREATE OR REPLACE TRIGGER trg_compres_after_stmt
AFTER INSERT OR UPDATE OR DELETE ON COMPRES
DECLARE
BEGIN
    pkg_compres_temp.v_actualitzant_preu := TRUE; -- activa permiso interno

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
    pkg_compres_temp.v_actualitzant_preu := FALSE; -- desactiva permiso interno
END;
/
