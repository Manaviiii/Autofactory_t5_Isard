package p1t4model;

public class Item {

    private String codi;          
    private char es_producte;     
    private String nom;           
    private String descrip;       
    private int stock;            
    private String foto;          

    // Constructor mínimo
    public Item(String codi, char es_producte, String nom, String foto) throws Exception
    {
        setCodi(codi);
        setEs_producte(es_producte);
        setNom(nom);
        setFoto(foto);
        setStock(0);
        setDescrip(null);
    }

    // Constructor intermedio
    public Item(String codi, char es_producte, String nom, String foto, int stock) throws Exception
    {
        this(codi, es_producte, nom, foto);
        setStock(stock);
    }

    // Constructor completo
    public Item(String codi, char es_producte, String nom, String foto, int stock, String descrip) throws Exception
    {
        this(codi, es_producte, nom, foto, stock);
        setDescrip(descrip);
    }

    // Getters
    public String getCodi()
    {
        return codi;
    }

    public char getEs_producte()
    {
        return es_producte;
    }

    public String getNom()
    {
        return nom;
    }

    public String getDescrip()
    {
        return descrip;
    }

    public int getStock()
    {
        return stock;
    }

    public String getFoto()
    {
        return foto;
    }

    // Setters
    public void setCodi(String codi) throws Exception
    {
        if(codi == null || codi.isEmpty() || codi.length() > 10)
            throw new Exception("codi no puede ser null, vacío ni mayor de 10 caracteres");
        this.codi = codi;
    }

    public void setEs_producte(char es_producte) throws Exception
    {
        if(es_producte != 's' && es_producte != 'n')
            throw new Exception("es_producte debe ser 's' o 'n'");
        this.es_producte = es_producte;
    }

    public void setNom(String nom) throws Exception
    {
        if(nom == null || nom.isEmpty() || nom.length() > 20)
            throw new Exception("nom no puede ser null, vacío ni mayor de 20 caracteres");
        this.nom = nom;
    }

    public void setDescrip(String descrip) throws Exception
    {
        if(descrip != null && descrip.length() > 100)
            throw new Exception("descrip no puede tener más de 100 caracteres");
        this.descrip = descrip;
    }

    public void setStock(int stock) throws Exception
    {
        if(stock < 0)
            throw new Exception("stock no puede ser negativo");
        this.stock = stock;
    }

    public void setFoto(String foto) throws Exception
    {
        if(foto == null || foto.isEmpty())
            throw new Exception("foto no puede ser null o vacío");
        this.foto = foto;
    }

    @Override
    public String toString()
    {
        return "Item{" +
                "codi='" + codi + '\'' +
                ", es_producte=" + es_producte +
                ", nom='" + nom + '\'' +
                ", descrip='" + descrip + '\'' +
                ", stock=" + stock +
                ", foto='" + foto + '\'' +
                '}';
    }
}
