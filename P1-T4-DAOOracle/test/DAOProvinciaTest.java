import p1.t4.daooracle.DAOProvincia;
import p1t4model.Provincia;
import java.util.List;

public class DAOProvinciaTest {

    public static void main(String[] args) {
        DAOProvincia dao = new DAOProvincia();

        try {
            Provincia buscar = dao.obtenirProvinciaPerId("P1");
            if (buscar != null) {
                System.out.println("Buscar provincia: Encontrada ✔: " + buscar.getPrNom());
            } else {
                System.out.println("Buscar provincia: No encontrada ❌");
            }

            // Listar todas las provincias
            List<Provincia> lista = dao.llistarProvincies();
            System.out.println("Lista de provincias:");
            if (!lista.isEmpty()) {
                for (Provincia p : lista) {
                    System.out.println(p);
                }
            } else {
                System.out.println("No hay provincias registradas ❌");
            }

        } catch (Exception e) {
            System.out.println("Error en test DAOProvincia: " + e.getMessage());
        }
    }
}
