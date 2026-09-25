package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import io.github.flbulgarelli.jpa.extras.EntityManagerOps;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerAccess;
import io.github.flbulgarelli.jpa.extras.perthread.WithPerThreadEntityManager;

public interface WithDonacionesPersistenceUnit
    extends WithPerThreadEntityManager, EntityManagerOps, TransactionalOps {

  String DONACIONES_PERSISTENCE_UNIT_NAME = "donaciones-persistence-unit";
  PerThreadEntityManagerAccess PER_THREAD_ENTITY_MANAGER_ACCESS =
      new PerThreadEntityManagerAccess(DONACIONES_PERSISTENCE_UNIT_NAME);

  @Override
  default PerThreadEntityManagerAccess perThreadEntityManagerAccess() {
    return PER_THREAD_ENTITY_MANAGER_ACCESS;
  }
}