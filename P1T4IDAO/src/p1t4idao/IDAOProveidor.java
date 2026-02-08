package p1t4idao;

import java.util.List;
import p1t4model.Proveidor;

public interface IDAOProveidor {

    public boolean eliminarProveidor(String codi);

    public boolean inserirProveidor(Proveidor p);

    public List<Proveidor> llistarProveidors();

    public boolean modificarProveidor(Proveidor p);

    public Proveidor obtenirProveidorPerId(String codi);
}
