import p1.t4.daooracle.DAOItem;
import p1t4model.Item;

public class DAOItemTest {

    public static void main(String[] args) {
        DAOItem dao = new DAOItem();

        try {
            Item item = new Item("I51", 'n', "ItemTest", "dummy");

            boolean crear = dao.inserirItem(item);
            if (crear) {
                System.out.println("Creacion del item: ✔");
            } else {
                System.out.println("Creacion del item: ❌");
            }

            Item buscar = dao.obtenirItemPerId("I51");
            if (buscar != null) {
                System.out.println("Buscar item: Encontrado ✔:" + buscar);
            } else {
                System.out.println("Buscar item: No encontrado ❌");
            }

            item.setNom("Prueba");
            boolean actualizado = dao.modificarItem(item);
            if (actualizado) {
                System.out.println("Actualizacion del item: ✔");
            } else {
                System.out.println("Actualizacion del item: ❌");
            }

            System.out.println("Lista de todos los items:");
            if (!dao.llistarItems().isEmpty()) {
                for (Item i : dao.llistarItems()) {
                    System.out.println(i);
                }
            } else {
                System.out.println("No hay items para buscar");
            }

            boolean eliminado = dao.eliminarItem("I51");
            if (eliminado) {
                System.out.println("Eliminacion del item: ✔");
            } else {
                System.out.println("Eliminacion del item: ❌");
            }

        } catch (Exception e) {
            System.out.println("Error test DAOItem: " + e.getMessage());
        }
    }
}
