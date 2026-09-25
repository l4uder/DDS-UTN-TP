package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import io.github.flbulgarelli.jpa.extras.EntityManagerOps;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerAccess;
import io.github.flbulgarelli.jpa.extras.perthread.WithPerThreadEntityManager;

public interface WithLogisticaPersistenceUnit extends WithPerThreadEntityManager, EntityManagerOps, TransactionalOps {

  String LOGISTICA_PERSISTENCE_UNIT_NAME = "logistica-persistence-unit";
  PerThreadEntityManagerAccess PER_THREAD_ENTITY_MANAGER_ACCESS =
      new PerThreadEntityManagerAccess(LOGISTICA_PERSISTENCE_UNIT_NAME);

  @Override
  default PerThreadEntityManagerAccess perThreadEntityManagerAccess() {
    return PER_THREAD_ENTITY_MANAGER_ACCESS;
  }
}