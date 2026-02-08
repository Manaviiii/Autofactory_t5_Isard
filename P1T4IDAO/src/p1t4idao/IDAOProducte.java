package p1t4idao;

import java.util.List;
import p1t4model.Producte;

public interface IDAOProducte {

    public boolean eliminarProducte(String codip);

    public List<Producte> filtrarPerCategoria(String codiCategoria);

    public List<Producte> filtrarPerProveidor(String codiProveidor);

    public boolean inserirProducte(Producte p);

    public List<Producte> llistarProductes();

    public boolean modificarProducte(Producte p);

    public Producte obtenirProductePerId(String codip);
}
