package p1t4model;

public class Provincia {

    private String pr_codi;
    private String pr_nom;

    public Provincia(String pr_codi, String pr_nom) throws Exception
    {
        setPrCodi(pr_codi);
        setPrNom(pr_nom);
    }

    public String getPrCodi()
    {
        return pr_codi;
    }

    public void setPrCodi(String pr_codi) throws Exception
    {
        if(pr_codi == null || pr_codi.isEmpty() || pr_codi.length() > 10)
            throw new Exception("pr_codi inválido");
        this.pr_codi = pr_codi;
    }

    public String getPrNom()
    {
        return pr_nom;
    }

    public void setPrNom(String pr_nom) throws Exception
    {
        if(pr_nom == null || pr_nom.isEmpty() || pr_nom.length() > 20)
            throw new Exception("pr_nom inválido");
        this.pr_nom = pr_nom;
    }

    @Override
    public String toString()
    {
        return "Provincia{" +
                "pr_codi='" + pr_codi + '\'' +
                ", pr_nom='" + pr_nom + '\'' +
                '}';
    }
}
