import p1.t4.daooracle.DAOMunicipi;
import p1t4model.Municipi;

public class DAOMunicipiTest {

    public static void main(String[] args) {
        DAOMunicipi dao = new DAOMunicipi();

        try {

            // Probar busqueda de municipios
            System.out.println("Lista de todos los municipios:");
            if (!dao.llistarMunicipis().isEmpty()) {
                for (Municipi m : dao.llistarMunicipis()) {
                    System.out.println(m);
                }
            } else {
                System.out.println("No hay municipios para listar");
            }

            // Probar búsqueda por id
            Municipi buscar = dao.obtenirMunicipiPerId("M4");
            if (buscar != null) {
                System.out.println("Buscar municipio: Encontrado ✔:" + buscar);
            } else {
                System.out.println("Buscar municipio: No encontrado ❌");
            }

        } catch (Exception e) {
            System.out.println("Error test DAOMunicipi: " + e.getMessage());
        }
    }
}
