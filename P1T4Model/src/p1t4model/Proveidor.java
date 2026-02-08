package p1t4model;

public class Proveidor {

    private String codi;
    private String CIF;
    private String rao_social;
    private String linia_adreca_facturacio;
    private String persona_contacte;
    private String telf_contacte;
    private String municipi;

    public Municipi m_Municipi;

    // Constructor solo con obligatorios
    public Proveidor(String codi, String CIF, String rao_social, String linia_adreca_facturacio, String municipi) throws Exception {
        setCodi(codi);
        setCIF(CIF);
        setRaoSocial(rao_social);
        setLiniaAdrecaFacturacio(linia_adreca_facturacio);
        setMunicipi(municipi);
    }

    // Constructor con obligatorios + persona_contacte
    public Proveidor(String codi, String CIF, String rao_social, String linia_adreca_facturacio,
                     String municipi, String persona_contacte) throws Exception {
        this(codi, CIF, rao_social, linia_adreca_facturacio, municipi);
        setPersonaContacte(persona_contacte);
    }

    // Constructor con obligatorios + telf_contacte
    public Proveidor(String codi, String CIF, String rao_social, String linia_adreca_facturacio,
                     String municipi, String telf_contacte, boolean isTel) throws Exception {
        this(codi, CIF, rao_social, linia_adreca_facturacio, municipi);
        setTelfContacte(telf_contacte);
    }

    // Constructor con obligatorios + ambos opcionales
    public Proveidor(String codi, String CIF, String rao_social, String linia_adreca_facturacio,
                     String municipi, String persona_contacte, String telf_contacte) throws Exception {
        this(codi, CIF, rao_social, linia_adreca_facturacio, municipi);
        setPersonaContacte(persona_contacte);
        setTelfContacte(telf_contacte);
    }

    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) throws Exception {
        if(codi == null || codi.isEmpty() || codi.length() > 10)
            throw new Exception("codi inválido");
        this.codi = codi;
    }

    public String getCIF() {
        return CIF;
    }

    public void setCIF(String CIF) throws Exception {
        if(CIF == null || CIF.isEmpty() || CIF.length() > 20)
            throw new Exception("CIF inválido");
        this.CIF = CIF;
    }

    public String getRaoSocial() {
        return rao_social;
    }

    public void setRaoSocial(String rao_social) throws Exception {
        if(rao_social == null || rao_social.isEmpty() || rao_social.length() > 100)
            throw new Exception("rao_social inválido");
        this.rao_social = rao_social;
    }

    public String getLiniaAdrecaFacturacio() {
        return linia_adreca_facturacio;
    }

    public void setLiniaAdrecaFacturacio(String linia) throws Exception {
        if(linia == null || linia.isEmpty() || linia.length() > 200)
            throw new Exception("linia_adreca_facturacio inválido");
        this.linia_adreca_facturacio = linia;
    }

    public String getPersonaContacte() {
        return persona_contacte;
    }

    public void setPersonaContacte(String persona_contacte) throws Exception {
        if(persona_contacte != null && persona_contacte.length() > 50)
            throw new Exception("persona_contacte demasiado largo");
        this.persona_contacte = persona_contacte;
    }

    public String getTelfContacte() {
        return telf_contacte;
    }

    public void setTelfContacte(String telf_contacte) throws Exception {
        if(telf_contacte != null && telf_contacte.length() > 20)
            throw new Exception("telf_contacte demasiado largo");
        this.telf_contacte = telf_contacte;
    }

    public String getMunicipi() {
        return municipi;
    }

    public void setMunicipi(String municipi) throws Exception {
        if(municipi == null || municipi.isEmpty() || municipi.length() > 10)
            throw new Exception("municipi inválido");
        this.municipi = municipi;
    }

    @Override
    public String toString() {
        return "Proveidor{" +
                "codi='" + codi + '\'' +
                ", CIF='" + CIF + '\'' +
                ", rao_social='" + rao_social + '\'' +
                ", linia_adreca_facturacio='" + linia_adreca_facturacio + '\'' +
                ", persona_contacte='" + persona_contacte + '\'' +
                ", telf_contacte='" + telf_contacte + '\'' +
                ", municipi='" + municipi + '\'' +
                '}';
    }
}
