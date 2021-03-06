package ar.edu.utn.tadp.empresa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.junit.Assert;
import org.junit.Test;

import ar.edu.utn.tadp.agenda.Agenda;
import ar.edu.utn.tadp.agenda.Evento;
import ar.edu.utn.tadp.excepcion.UserException;
import ar.edu.utn.tadp.organizables.Reunion;
import ar.edu.utn.tadp.propiedad.Propiedad;
import ar.edu.utn.tadp.recurso.Persona;
import ar.edu.utn.tadp.recurso.Recurso;
import ar.edu.utn.tadp.requerimiento.Requerimiento;

import com.google.common.collect.Lists;

/**
 * Prueba la creacion de una reunion.
 * 
 * @version 11-05-2012
 */
public class CrearReunionTest {

	private final Propiedad proyectoApollo = new Propiedad("proyecto", "Apollo");
	private final Propiedad proyectoMir = new Propiedad("Proyecto", "Mir");
	private final Propiedad proyectoShuttle = new Propiedad("Proyecto",
			"shuttle");
	private final Propiedad sectorGerencia = new Propiedad("sector", "gerencia");
	private final Propiedad rolProgramador = new Propiedad("rol", "programador");
	private final Propiedad rolArquitecto = new Propiedad("rol", "Arquitecto");
	private final Propiedad rolGerente = new Propiedad("Rol", "Gerente");
	private final Propiedad edificioMadero = new Propiedad("edificio", "Madero");
	private final Propiedad tipoSala = new Propiedad("tipo", "Sala");
	private final Propiedad tipoProyector = new Propiedad("tipo", "Proyector");
	private final Propiedad sectDesarrollo = new Propiedad("Sector",
			"Desarrollo");
	private final Propiedad rolProjectLider = new Propiedad("Rol",
			"Project Leader");
	private final Propiedad edifCatalinas = new Propiedad("edificio",
			"catalinas");

	private final GeneradorDeContexto generador = new GeneradorDeContexto();

	private final Persona programador1 = generador.newProgramador(
			proyectoApollo, sectDesarrollo, edifCatalinas);
	private final Persona programador2 = generador.newProgramador(
			proyectoApollo, sectDesarrollo, edificioMadero);
	private final Persona programador3 = generador.newProgramador(proyectoMir,
			sectDesarrollo, edificioMadero);
	private final Persona arquitecto1 = generador.newArquitecto(proyectoApollo,
			sectDesarrollo, edifCatalinas);
	private final Persona gerente1 = generador.newGerente(proyectoApollo,
			sectorGerencia, edificioMadero);
	private final Persona gerente2 = generador.newGerente(proyectoMir,
			sectorGerencia, edificioMadero);
	private final Persona gerente3 = generador.newGerente(proyectoShuttle,
			sectorGerencia, edificioMadero);
	private final Persona leader = generador.newProjectLeader(proyectoShuttle,
			null, edificioMadero);
	private final Recurso sala = generador.newSala(edificioMadero);
	private final Recurso proyector1 = generador.newProyector(edificioMadero);
	private final Recurso proyector2 = generador.newProyector(edifCatalinas);
	private final Empresa unaEmpresa = generador.newEmpresa(Lists.newArrayList(
			leader, arquitecto1, gerente1, gerente2, programador1,
			programador2, programador3, sala, proyector1, proyector2));

	/**
	 * Se reserva una sala para todos los integrantes de un proyecto
	 * exitosamente.
	 */
	@Test
	public void creaReunionParaIntegrantesDeUnProyecto() {
		final ArrayList<Propiedad> condiciones = new ArrayList<Propiedad>();
		condiciones.add(proyectoApollo);
		final Requerimiento requerimiento = new Requerimiento(condiciones);

		final ArrayList<Requerimiento> requerimientos = new ArrayList<Requerimiento>();
		requerimientos.add(requerimiento);

		final Reunion reunion = unaEmpresa.createReunion(gerente1,
				requerimientos, Hours.THREE, DateTime.now().plusDays(2));

		Assert.assertNotNull(reunion);
	}

	/**
	 * Se reserva una sala para 3 gerentes cualquiera para dentro de 2 dias,
	 * pero falla porque estan todos ocupados.
	 */
	@Test(expected = UserException.class)
	public void fallaCreandoRenionCon3GerentesOcupadosDentroDe2Dias() {
		// Intervalo de ocupacion 5 dias.
		final Interval ocupado3Dias = new Interval(Agenda.HOY,
				Agenda.HOY.plusDays(5));

		Evento evento = new Evento(ocupado3Dias);
		gerente1.ocupate(evento);
		gerente2.ocupate(evento);
		gerente3.ocupate(evento);

		final ArrayList<Propiedad> pripiedadesSala = new ArrayList<Propiedad>();
		pripiedadesSala.add(tipoSala);
		final Requerimiento reqSala = new Requerimiento(pripiedadesSala);

		final ArrayList<Propiedad> pripiedadesGerente = new ArrayList<Propiedad>();
		pripiedadesGerente.add(rolGerente);
		final Requerimiento reqGerente1 = new Requerimiento(pripiedadesGerente);
		final Requerimiento reqGerente2 = new Requerimiento(pripiedadesGerente);
		final Requerimiento reqGerente3 = new Requerimiento(pripiedadesGerente);

		final ArrayList<Requerimiento> requerimientos = new ArrayList<Requerimiento>();
		requerimientos.add(reqSala);
		requerimientos.add(reqGerente1);
		requerimientos.add(reqGerente2);
		requerimientos.add(reqGerente3);

		// Esto tira una excepcion.
		unaEmpresa.createReunion(gerente1, requerimientos, Hours.THREE,
				DateTime.now().plusDays(2));
	}

	/**
	 * Se reserva una sala para 3 programadores y un project leader, la sala se
	 * reserva y se agrega el catering automaticamente.
	 */
	@Test
	public void creaReunionCon3ProgramadoresYProjectLeader() {
		final Collection<Propiedad> propiedadesSala = new ArrayList<Propiedad>();
		propiedadesSala.add(tipoSala);

		final Requerimiento sala = new Requerimiento(propiedadesSala);
		final ArrayList<Propiedad> propiedadesProgramador = new ArrayList<Propiedad>();
		propiedadesProgramador.add(rolProgramador);
		final Requerimiento reqProgramador1 = new Requerimiento(
				propiedadesProgramador);
		final Requerimiento reqProgramador2 = new Requerimiento(
				propiedadesProgramador);
		final Requerimiento reqProgramador3 = new Requerimiento(
				propiedadesProgramador);

		final List<Requerimiento> requerimientos = new ArrayList<Requerimiento>();
		requerimientos.add(reqProgramador1);
		requerimientos.add(reqProgramador2);
		requerimientos.add(reqProgramador3);
		requerimientos.add(sala);
		final Reunion reunion = unaEmpresa.createReunion(leader,
				requerimientos, Hours.THREE, DateTime.now().plusDays(2));

		Assert.assertNotNull(reunion);
		Assert.assertTrue(reunion.tieneCatering());
	}

	/**
	 * Considerando un proyecto X, se crean 2 reuniones (una con personas de
	 * distintos proyectos, y otra solo con persona de un proyecto X) y luego se
	 * verifica que el costo sea el correcto.
	 */
	@Test
	public void crea2ReunionesConLosMismosRolesPeroDistintoCosto() {
		final ArrayList<Propiedad> condiciones1 = new ArrayList<Propiedad>();
		condiciones1.add(proyectoApollo);
		condiciones1.add(edificioMadero);
		condiciones1.add(rolProgramador);
		final Requerimiento programadorApollo = new Requerimiento(condiciones1);

		final ArrayList<Requerimiento> requerimientos1 = new ArrayList<Requerimiento>();
		requerimientos1.add(programadorApollo);

		// Anfintrion es de Apollo y el invitado tambien
		final Reunion reunionBarata = unaEmpresa.createReunion(gerente1,
				requerimientos1, Hours.SEVEN, DateTime.now().plusDays(2));

		final ArrayList<Propiedad> condiciones2 = new ArrayList<Propiedad>();
		condiciones2.add(proyectoMir);
		condiciones2.add(edificioMadero);
		condiciones2.add(rolProgramador);
		final Requerimiento programadorMir = new Requerimiento(condiciones2);

		final ArrayList<Requerimiento> requerimientos2 = new ArrayList<Requerimiento>();
		requerimientos2.add(programadorMir);

		// Anfintrion es de Apollo y el invitado de Mir
		final Reunion reunionCara = unaEmpresa.createReunion(gerente1,
				requerimientos2, Hours.SEVEN, DateTime.now().plusDays(2));

		final float costoReunionBarata = reunionBarata.getCostoTotal()
				.floatValue();
		final float costoReunionCara = reunionCara.getCostoTotal().floatValue();

		Assert.assertTrue(costoReunionBarata < costoReunionCara);
	}

	/**
	 * Se crea una reunion que incluya a un arquitecto del proyecto X y se
	 * verifica que su tiempo este ocupado en ese rango.
	 */
	@Test
	public void creaReunionYValidaQueLaGenteQuedeOcupada() {
		final Interval intervalo = new Interval(Agenda.HOY,
				Agenda.HOY.plusHours(2));
		final ArrayList<Propiedad> propiedadesArq = new ArrayList<Propiedad>();
		propiedadesArq.add(rolArquitecto);
		propiedadesArq.add(proyectoApollo);
		final Requerimiento reqArquitecto = new Requerimiento(propiedadesArq);
		final ArrayList<Requerimiento> requerimientos = new ArrayList<Requerimiento>();
		requerimientos.add(reqArquitecto);
		unaEmpresa
				.createReunion(gerente1, requerimientos, intervalo.toDuration()
						.toStandardHours(), intervalo.getEnd().plusDays(2));

		Assert.assertTrue(arquitecto1.estasOcupadoDurante(intervalo));
	}

	/**
	 * Se validan los recursos de una reunion creada.
	 */
	@Test
	public void creaReunionConPocosRecursos() {
		final ArrayList<Propiedad> condicionesInvitado = new ArrayList<Propiedad>();
		condicionesInvitado.add(edifCatalinas);
		condicionesInvitado.add(rolProgramador);
		final Requerimiento programador = new Requerimiento(condicionesInvitado);

		final ArrayList<Propiedad> condicionesRecurso = new ArrayList<Propiedad>();
		condicionesRecurso.add(tipoProyector);
		final Requerimiento recurso = new Requerimiento(condicionesRecurso);

		final ArrayList<Requerimiento> requerimientos = new ArrayList<Requerimiento>();
		requerimientos.add(programador);
		requerimientos.add(recurso);

		final Reunion reunion = unaEmpresa.createReunion(gerente1,
				requerimientos, Hours.SEVEN, DateTime.now().plusDays(2));

		// Recursos deberian ser: anfitrion, invitado, proyector y la sala.
		Assert.assertEquals(4, reunion.getRecursos().size());
	}

	/**
	 * Se crea una reunion donde gerente esta en madero y programador en
	 * catalinas. Uno de ellos si o si va a pedir transporte.
	 */
	@Test
	public void creaReunionDondeHayQueViajar() {
		final ArrayList<Propiedad> condiciones1 = new ArrayList<Propiedad>();
		condiciones1.add(edifCatalinas);
		condiciones1.add(rolProgramador);
		final Requerimiento programador = new Requerimiento(condiciones1);

		final ArrayList<Requerimiento> requerimientos1 = new ArrayList<Requerimiento>();
		requerimientos1.add(programador);

		final Reunion reunion = unaEmpresa.createReunion(gerente1,
				requerimientos1, Hours.SEVEN, DateTime.now().plusDays(2));

		// Anfitrion y el invitado estan en edificios distintos
		Assert.assertEquals(1,
				reunion.getCantidadDePersonasQueNecesitanTransporte());
	}
}
