package p1t4idao;

import p1t4model.Component;
import java.util.List;
import java.util.Optional;

public interface IDAOComponent {

    public boolean crear(Component c);

    public boolean actualizar(Component c);

    public boolean eliminar(String codiCom);

    public Component buscarPorCodigo(String codiCom);

    public List<Component> listarTodos();
}
