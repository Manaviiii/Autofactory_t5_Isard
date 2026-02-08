package p1t4model;

public class Municipi {

    private String codi_mun;
    private String nom;
    private String codi_prov;

    public Provincia m_Provincia;

    public Municipi(String codi_mun, String codi_prov, String nom) throws Exception
    {
        setCodiMun(codi_mun);
        setCodiProv(codi_prov);
        setNom(nom);
    }
    
    public String getCodiMun()
    {
        return codi_mun;
    }
    
    public void setCodiMun(String codi_mun) throws Exception
    {
        if(codi_mun == null || codi_mun.isEmpty() || codi_mun.length() > 10)
            throw new Exception("codi_mun inválido");
        this.codi_mun = codi_mun;
    }

    public String getCodiProv()
    {
        return codi_prov;
    }

    public void setCodiProv(String codi_prov) throws Exception
    {
        if(codi_prov == null || codi_prov.isEmpty() || codi_prov.length() > 10)
            throw new Exception("codi_prov inválido");
        this.codi_prov = codi_prov;
    }

    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom) throws Exception
    {
        if(nom == null || nom.isEmpty() || nom.length() > 20)
            throw new Exception("nom inválido");
        this.nom = nom;
    }

    @Override
    public String toString()
    {
        return "Municipi{" +
                "codi_mun='" + codi_mun + '\'' +
                ", codi_prov='" + codi_prov + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }
}
