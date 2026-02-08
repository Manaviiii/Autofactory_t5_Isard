package p1t4idao;

import java.util.List;
import p1t4model.Item;

public interface IDAOItem {

    public boolean eliminarItem(String codi);

    public List<Item> filtrarPerProducte(String codiProducte);

    public boolean inserirItem(Item item);

    public List<Item> llistarItems();

    public boolean modificarItem(Item item);

    public Item obtenirItemPerId(String codi);
}
