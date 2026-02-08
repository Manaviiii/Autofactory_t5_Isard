package p1t4idao;

import java.util.List;
import p1t4model.UnitatMesura;

/**
 * @author vvill
 * @version 1.0
 * @created 03-nov.-2025 16:48:43
 */
public interface IDAOMesura {
    
	public List<UnitatMesura> llistarMesures();

	public UnitatMesura obtenirMesuraPerId(String id);

}