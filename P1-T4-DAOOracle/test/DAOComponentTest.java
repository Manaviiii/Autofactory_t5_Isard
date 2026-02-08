import p1.t4.daooracle.DAOComponent;
import p1t4model.Component;

public class DAOComponentTest {

    public static void main(String[] args) {
        DAOComponent dao = new DAOComponent();

        try {
            Component comp = new Component("C511", 'n', "ComponenteTest", "dummy", "FAB001", "U");

            boolean creado = dao.crear(comp);
            if (creado) {
                System.out.println("Creacion del componente: ✔");
            } else {
                System.out.println("Creacion del componente: ❌");
            }

            Component buscar = dao.buscarPorCodigo("C511");
            if (buscar != null) {
                System.out.println("Buscar componente: Encontrado ✔:" + buscar);
            } else {
                System.out.println("Buscar componente: No encontrado ❌");
            }

            comp.setNom("Prueba");
            boolean actualizado = dao.actualizar(comp);
            if (actualizado) {
                System.out.println("Actualizacion de componente: ✔");
            } else {
                System.out.println("Actualizacion de componente: ❌");
            }

            System.out.println("Lista de todos los componentes:");
            if (!dao.listarTodos().isEmpty()) {
                for (Component c : dao.listarTodos()) {
                    System.out.println(c);
                }
            } else {
                System.out.println("No hay componentes para buscar");
            }

            boolean eliminado = dao.eliminar("C511");
            if (eliminado) {
                System.out.println("Eliminacion de componente: ✔");
            } else {
                System.out.println("Eliminacion de componente: ❌");
            }

        } catch (Exception e) {
            System.out.println("Error test DAOComponent: " + e.getMessage());
        }
    }
}
