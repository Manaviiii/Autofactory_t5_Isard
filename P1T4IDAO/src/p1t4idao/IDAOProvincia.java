package p1t4idao;

import java.util.List;
import p1t4model.Provincia;

public interface IDAOProvincia {

    public List<Provincia> llistarProvincies();

    public Provincia obtenirProvinciaPerId(String codiProv);
}
