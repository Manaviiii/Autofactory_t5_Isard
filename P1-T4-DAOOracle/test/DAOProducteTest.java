import p1.t4.daooracle.DAOProducte;
import p1t4model.Producte;
import java.util.List;

public class DAOProducteTest {

    public static void main(String[] args) {
        DAOProducte dao = new DAOProducte();

        try {
            // Crear producto de prueba
            Producte prod = new Producte("P111", 's', "ProducteTest", "2323", 10, "Producto de prueba");

            // Probar input
            boolean insertado = dao.inserirProducte(prod);
            if (insertado) {
                System.out.println("Input de producto: ✔");
            } else {
                System.out.println("Input de producto: ❌");
            }

            // Probar búsqueda
            Producte buscado = dao.obtenirProductePerId("P111");
            if (buscado != null) {
                System.out.println("Buscar producto: Encontrado ✔: " + buscado);
            } else {
                System.out.println("Buscar producto: No encontrado ❌");
            }

            // Probar actualización
            prod.setNom("Producto Actualizado");
            boolean modificado = dao.modificarProducte(prod);
            if (modificado) {
                System.out.println("Modificación de producto: ✔");
            } else {
                System.out.println("Modificación de producto: ❌");
            }

            // Probar lista
            List<Producte> lista = dao.llistarProductes();
            System.out.println("Lista de productos:");
            if (!lista.isEmpty()) {
                for (Producte p : lista) {
                    System.out.println(p);
                }
            } else {
                System.out.println("No hay productos disponibles");
            }

            //Borrar
            boolean eliminado = dao.eliminarProducte("P111");
            if (eliminado) {
                System.out.println("Eliminación de producto: ✔");
            } else {
                System.out.println("Eliminación de producto: ❌");
            }

        } catch (Exception e) {
            System.out.println("Error en test DAOProducte: " + e.getMessage());
        }
    }
}
