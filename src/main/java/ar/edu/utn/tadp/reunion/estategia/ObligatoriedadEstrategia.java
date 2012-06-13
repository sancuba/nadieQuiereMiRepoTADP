package ar.edu.utn.tadp.reunion.estategia;

import ar.edu.utn.tadp.empresa.Empresa;
import ar.edu.utn.tadp.reunion.Reunion;

/**
 * Trata de evitar cancelar la reunion a menos que se dio de baja alguna de las
 * personas o recursos obligatorios.
 * 
 * @version 13-06-2012
 */
public class ObligatoriedadEstrategia implements TratarCancelacionEstrategia {

	@Override
	public boolean tratarCancelacion(final Reunion reunion,
			final Empresa empresa) {
		// TODO falta hacerlo.
		return false;
	}

}
