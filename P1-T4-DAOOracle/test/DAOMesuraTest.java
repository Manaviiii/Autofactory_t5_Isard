import p1.t4.daooracle.DAOMesura;
import p1t4model.UnitatMesura;

public class DAOMesuraTest {

    public static void main(String[] args) {
        DAOMesura dao = new DAOMesura();

        try {

            // Probar listado de medidas
            System.out.println("Lista de todas las medidas:");
            if (!dao.llistarMesures().isEmpty()) {
                for (UnitatMesura m : dao.llistarMesures()) {
                    System.out.println(m);
                }
            } else {
                System.out.println("No hay medidas para buscar");
            }

            // Probar búsqueda por id
            UnitatMesura buscar = dao.obtenirMesuraPerId("U");
            if (buscar != null) {
                System.out.println("Buscar medida: Encontrada ✔:" + buscar);
            } else {
                System.out.println("Buscar medida: No encontrada ❌");
            }

        } catch (Exception e) {
            System.out.println("Error test DAOMesura: " + e.getMessage());
        }
    }
}
