package p1t4model;

public class Component extends Item {

    private String codi_fabricant;
    private int preu_mig;
    private String unitat_mesura;

    // Constructor mínimo (solo obligatorios del Item + Component)
    public Component(String codi, char es_producte, String nom, String foto, String codi_fabricant, String unitat_mesura) throws Exception
    {
        super(codi, es_producte, nom, foto);
        setCodiFabricant(codi_fabricant);
        setUnitatMesura(unitat_mesura);
        this.preu_mig = 0;
    }

    // Constructor con stock
    public Component(String codi, char es_producte, String nom, String foto, int stock, String codi_fabricant, String unitat_mesura) throws Exception
    {
        super(codi, es_producte, nom, foto, stock);
        setCodiFabricant(codi_fabricant);
        setUnitatMesura(unitat_mesura);
        this.preu_mig = 0;
    }

    // Constructor completo (stock + descripción)
    public Component(String codi, char es_producte, String nom, String foto, int stock, String descrip, String codi_fabricant, String unitat_mesura) throws Exception
    {
        super(codi, es_producte, nom, foto, stock, descrip);
        setCodiFabricant(codi_fabricant);
        setUnitatMesura(unitat_mesura);
        this.preu_mig = 0;
    }

    public String getCodiFabricant()
    {
        return codi_fabricant;
    }

    public void setCodiFabricant(String codi_fabricant) throws Exception
    {
        if(codi_fabricant == null || codi_fabricant.isEmpty() || codi_fabricant.length() > 10)
            throw new Exception("codi_fabricant no puede ser null, vacío ni mayor de 10 caracteres");
        this.codi_fabricant = codi_fabricant;
    }

    public int getPreuMig()
    {
        return preu_mig;
    }

    public void setPreuMig(int preu_mig) throws Exception
    {
        if(preu_mig < 0)
            throw new Exception("preu_mig no puede ser negativo");
        this.preu_mig = preu_mig;
    }

    public String getUnitatMesura()
    {
        return unitat_mesura;
    }

    public void setUnitatMesura(String unitat_mesura) throws Exception
    {
        if(unitat_mesura == null || unitat_mesura.isEmpty() || unitat_mesura.length() > 5)
            throw new Exception("unitat_mesura no puede ser null, vacío ni mayor de 5 caracteres");
        this.unitat_mesura = unitat_mesura;
    }

    @Override
    public String toString()
    {
        return super.toString() +
                ", Component{" +
                "codi_fabricant='" + codi_fabricant + '\'' +
                ", preu_mig=" + preu_mig +
                ", unitat_mesura='" + unitat_mesura + '\'' +
                '}';
    }
}
