package p1.t4.daooracle;

public class DAOFactory {

    public DAOComponent getDAOComponent() {
        return new DAOComponent();
    }

    public DAOItem getDAOItem() {
        return new DAOItem();
    }

    public DAOProducte getDAOProducte() {
        return new DAOProducte();
    }

    public DAOProveidor getDAOProveidor() {
        return new DAOProveidor();
    }

    public DAOProvincia getDAOProvincia() {
        return new DAOProvincia();
    }

    public DAOMunicipi getDAOMunicipi() {
        return new DAOMunicipi();
    }

    public DAOMesura getDAOMesura() {
        return new DAOMesura();
    }
}
