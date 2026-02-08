import p1.t4.daooracle.DAOProveidor;
import p1t4model.Proveidor;
import java.util.List;

public class DAOProveidorTest {

    public static void main(String[] args) {
        DAOProveidor dao = new DAOProveidor();

        try {
            // Crear proveedor de prueba
            Proveidor prov = new Proveidor("PR51", "CIF001", "ProveedorTest", "Calle Falsa 123", "M1", "Juan Pérez", "600123123");

            // Probar inserción
            boolean insertado = dao.inserirProveidor(prov);
            if (insertado) {
                System.out.println("Input de proveedor: ✔");
            } else {
                System.out.println("Input de proveedor: ❌");
            }

            // Probar búsqueda
            Proveidor buscado = dao.obtenirProveidorPerId("PR51");
            if (buscado != null) {
                System.out.println("Buscar proveedor: Encontrado ✔: " + buscado);
            } else {
                System.out.println("Buscar proveedor: No encontrado ❌");
            }

            // Probar actualización
            prov.setPersonaContacte("Carlos Gómez");
            boolean modificado = dao.modificarProveidor(prov);
            if (modificado) {
                System.out.println("Modificación de proveedor: ✔");
            } else {
                System.out.println("Modificación de proveedor: ❌");
            }

            // Probar lista
            List<Proveidor> lista = dao.llistarProveidors();
            System.out.println("Lista de proveedores:");
            if (!lista.isEmpty()) {
                for (Proveidor p : lista) {
                    System.out.println(p);
                }
            } else {
                System.out.println("No hay proveedores disponibles");
            }

            //Borrar
            boolean eliminado = dao.eliminarProveidor("PR51");
            if (eliminado) {
                System.out.println("Eliminación de proveedor: ✔");
            } else {
                System.out.println("Eliminación de proveedor: ❌");
            }

        } catch (Exception e) {
            System.out.println("Error en test DAOProveidor: " + e.getMessage());
        }
    }
}
