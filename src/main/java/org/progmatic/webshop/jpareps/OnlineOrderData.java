package org.progmatic.webshop.jpareps;

import org.progmatic.webshop.model.OnlineOrder;
import org.progmatic.webshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnlineOrderData extends JpaRepository<OnlineOrder, String> {
    OnlineOrder findOnlineOrderByUser(User user);
}
