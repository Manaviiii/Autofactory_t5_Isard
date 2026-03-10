# AutoFactory T5

Sistema de gestió de components i productes per a una fàbrica.

**Autor:** Victor Villagra

---

## Descripció

AutoFactory és una aplicació web Java (JSP/Servlets) que permet gestionar:
- **Components**: peces individuals amb preus per proveïdor
- **Productes**: conjunts de components i/o altres productes (BOM recursiu)
- **Proveïdors**: empreses subministradores amb preus per component
- **BOM (Bill of Materials)**: estructura jeràrquica de productes

---

## Requisits

| Software | Versió |
|----------|--------|
| Java JDK | 17+ |
| Oracle Database XE | 21c |
| GlassFish Server | 7.x |
| NetBeans IDE | 17+ (opcional) |

---

## Instal·lació

### 1. Base de Dades Oracle

1. Obrir **SQL Developer** o **SQL*Plus**
2. Connectar com a usuari `SYSTEM` (o amb privilegis DBA)
3. Executar l'script complet:

```sql
@script_autofactory.sql
```

O copiar i enganxar el contingut de `script_autofactory.sql` directament.

### 2. Configuració de Connexió

Verificar la configuració a `db.properties` o `DBConfig.java`:

```properties
db.url=jdbc:oracle:thin:@localhost:1521:xe
db.user=SYSTEM
db.password=12345
```

### 3. Desplegament

1. Obrir el projecte a **NetBeans**
2. Construir els projectes en aquest ordre:
   - `P1T4Model` (classes model)
   - `P1T4IDAO` (interfícies DAO)
   - `P1-T4-DAOOracle` (implementació DAO)
   - `AutoFactory_t5` (aplicació web)
3. Copiar el JAR de `P1-T4-DAOOracle` a `AutoFactory_t5/web/WEB-INF/lib/`
4. Desplegar `AutoFactory_t5` a GlassFish

### 4. Accés

- **URL:** http://localhost:8081/AutoFactory_t5/
- **Usuari:** `admin`
- **Contrasenya:** `admin123`

---

## Estructura del Projecte

```
T5_completo/
├── AutoFactory_t5/           # Aplicació web principal
│   ├── src/java/             # Servlets
│   │   ├── ComponentServlet.java
│   │   ├── ProductServlet.java
│   │   ├── ProveidorServlet.java
│   │   ├── BomServlet.java
│   │   ├── ArbreServlet.java
│   │   ├── CompresServlet.java
│   │   ├── ImageServlet.java
│   │   ├── LoginServlet.java
│   │   └── LogoutServlet.java
│   └── web/                  # Vistes JSP i recursos
│       ├── gestioComponents.jsp
│       ├── gestioProductes.jsp
│       ├── gestioProveidors.jsp
│       ├── formComponent.jsp
│       ├── formProducte.jsp
│       ├── formProveidor.jsp
│       ├── bom.jsp
│       ├── vistaArbre.jsp
│       ├── menu.jsp
│       ├── login.jsp
│       └── css/
├── P1-T4-DAOOracle/          # Capa d'accés a dades
│   └── src/p1/t4/daooracle/
│       ├── DAOComponent.java
│       ├── DAOProducte.java
│       ├── DAOProveidor.java
│       ├── DAOBom.java
│       ├── DAOCompres.java
│       ├── DAOMesura.java
│       ├── DAOMunicipi.java
│       └── DBConfig.java
├── P1T4Model/                # Classes model
└── P1T4IDAO/                 # Interfícies DAO
```

---

## Funcionalitats

### Gestió de Components
- CRUD complet (crear, llegir, actualitzar, eliminar)
- Pujada de fotos (BLOB)
- Paginació (5 per pàgina)
- Filtre per nom i codi fabricant
- Gestió de preus per proveïdor

### Gestió de Productes
- CRUD complet
- Pujada de fotos
- Paginació
- Gestió de BOM

### Gestió de Proveïdors
- CRUD complet
- Selecció de municipi

### BOM (Bill of Materials)
- Afegir components i subproductes
- Modificar quantitats
- Eliminar elements
- Vista d'arbre recursiva (fins a 10 nivells)
- Navegació entre subproductes

### Càlcul Automàtic de Preu Mig
- Triggers Oracle calculen automàticament el `preu_mig` dels components
- S'actualitza en inserir/modificar/eliminar registres a COMPRES

### Autenticació
- Login amb BCrypt
- Filtre d'autenticació per protegir totes les pàgines
- Logout

---

## Base de Dades

### Taules

| Taula | Descripció |
|-------|------------|
| USUARIS | Usuaris del sistema (login amb BCrypt) |
| UNITAT_MESURA | Unitats: U, KG, L |
| PROVINCIA | Províncies |
| MUNICIPI | Municipis (FK a Provincia) |
| ITEM | Superclasse per Components i Productes |
| COMPONENT | Components (FK a Item) |
| PRODUCTE | Productes (FK a Item) |
| FORMACIO_PRODUCTE | BOM - relació Producte-Item |
| PROVEIDOR | Proveïdors |
| COMPRES | Preus components per proveïdor |

### Triggers

| Trigger | Descripció |
|---------|------------|
| trg_component_modificacio | Calcula preu_mig en INSERT, impedeix modificar-lo manualment |
| trg_compres_before | Impedeix canviar codis a COMPRES |
| trg_compres_after_row | Registra components afectats |
| trg_compres_after_stmt | Recalcula preu_mig automàticament |

### Usuaris per defecte

| Username | Password | Rol |
|----------|----------|-----|
| admin | admin123 | ADMIN |
| user1 | user123 | USER |

---

## Tecnologies

- **Backend:** Java 17, JSP, Servlets, JDBC
- **Frontend:** HTML5, CSS3, JavaScript
- **Base de Dades:** Oracle Database XE 21c
- **Servidor:** GlassFish 7
- **Seguretat:** BCrypt per contrasenyes
- **Estil:** Google Fonts, Material Icons

---

## Resolució de Problemes

### Error: ORA-28000 El compte està bloquejat

Connectar com SYSDBA i executar:
```sql
ALTER USER SYSTEM ACCOUNT UNLOCK;
ALTER USER SYSTEM IDENTIFIED BY 12345;
```

### Error: No es troba la classe del driver Oracle

Afegir `ojdbc11.jar` a `WEB-INF/lib/`

### Les fotos no es mostren

1. Verificar que el BLOB no està buit
2. Comprovar que `ImageServlet` està desplegat
3. Revisar permisos de connexió a la BD

### Error 500 al editar component

Verificar que `DAOCompres.java` està compilat i el JAR actualitzat a `WEB-INF/lib/`

---



© 2024 Victor Villagra
