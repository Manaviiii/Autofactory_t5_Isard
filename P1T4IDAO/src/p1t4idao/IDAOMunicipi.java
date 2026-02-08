package p1t4idao;

import java.util.List;
import p1t4model.Municipi;

public interface IDAOMunicipi {

    public List<Municipi> llistarMunicipis();

    public Municipi obtenirMunicipiPerId(String codiMun);
}
