package p1t4model;

public class UnitatMesura {

    private String codi_mesura;
    private String nom_unitat;

    public UnitatMesura(String codi_mesura, String nom_unitat) throws Exception
    {
        setCodiMesura(codi_mesura);
        setNomUnitat(nom_unitat);
    }

    public String getCodiMesura()
    {
        return codi_mesura;
    }

    public void setCodiMesura(String codi_mesura) throws Exception
    {
        if(codi_mesura == null || codi_mesura.isEmpty() || codi_mesura.length() > 5)
            throw new Exception("codi_mesura inválido");
        this.codi_mesura = codi_mesura;
    }

    public String getNomUnitat()
    {
        return nom_unitat;
    }

    public void setNomUnitat(String nom_unitat) throws Exception
    {
        if(nom_unitat == null || nom_unitat.isEmpty() || nom_unitat.length() > 20)
            throw new Exception("nom_unitat inválido");
        this.nom_unitat = nom_unitat;
    }

    @Override
    public String toString()
    {
        return "UnitatMesura{" +
                "codi_mesura='" + codi_mesura + '\'' +
                ", nom_unitat='" + nom_unitat + '\'' +
                '}';
    }
}
