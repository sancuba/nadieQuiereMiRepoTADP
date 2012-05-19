package ar.edu.utn.tadp.recurso;

import java.math.BigDecimal;
import java.util.ArrayList;

import ar.edu.utn.tadp.empresa.Empresa;
import ar.edu.utn.tadp.recurso.roles.Rol;
import ar.edu.utn.tadp.reunion.Reunion;

public class Persona extends Recurso {

	private Rol rol;

	public Persona(Rol rol) {
		this.rol = rol;
	}

	@Override
	public BigDecimal dameTuCostoPara(Reunion reunion) {
		return this.rol.getCostoPorHora().dameTuCostoPara(reunion);
	}
	
	@Override
	public void apuntateALaReunion(ArrayList<Recurso> recursos) {
		super.apuntateALaReunion(recursos);
		this.rol.necesitasRecurso(recursos);
	}
	// Propiedades de una Persona serian: proyecto rol sector nombre empresa

	// TODO alguna diferencia de Recurso? Viajar a otro edificio?
}
