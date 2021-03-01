package org.progmatic.webshop.jpareps;

import org.progmatic.webshop.model.ExtraData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminData extends JpaRepository<ExtraData, String> {
    ExtraData findAdminDataById(int id);
}
