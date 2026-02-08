package p1t4model;

public class Producte extends Item {

    public Producte(String codi, char es_producte, String nom, String foto) throws Exception {
        super(codi, es_producte, nom, foto);
    }

    public Producte(String codi, char es_producte, String nom, String foto, int stock) throws Exception {
        super(codi, es_producte, nom, foto, stock);
    }

    public Producte(String codi, char es_producte, String nom, String foto, int stock, String descrip) throws Exception {
        super(codi, es_producte, nom, foto, stock, descrip);
    }

    @Override
    public String toString() {
        return super.toString() + ", Producte{}";
    }
}
