/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2005-2023 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.axelor.apps.sale.service;

import com.axelor.apps.base.AxelorException;
import com.axelor.apps.sale.db.SaleDashboard;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.apps.sale.db.repo.SaleDashboardRepository;
import com.axelor.apps.sale.db.repo.SaleOrderRepository;
import com.axelor.inject.Beans;
import java.math.BigDecimal;
import java.util.List;

public class SaleDashboardServiceImpl implements SaleDashboardService {

  @Override
  public BigDecimal getTotalInTaxTotal(Long saleDashbordId, Long locationId, Long companyId)
      throws AxelorException {
    BigDecimal totalInTaxTotal = BigDecimal.ZERO;

    SaleDashboard saleDashboard = Beans.get(SaleDashboardRepository.class).find(saleDashbordId);
    List<SaleOrder> saleOrders =
        Beans.get(SaleOrderRepository.class)
            .all()
            .filter("self.clientPartner = ?", saleDashboard.getCustomer())
            .fetch();

    for (SaleOrder saleOrder : saleOrders) {
      totalInTaxTotal = totalInTaxTotal.add(saleOrder.getInTaxTotal());
    }

    System.err.println(totalInTaxTotal);

    return totalInTaxTotal;
  }

  @Override
  public BigDecimal getZdsTotalInTaxTotal(Long saleDashbordId, Long locationId, Long companyId)
      throws AxelorException {
    BigDecimal zdsTotalInTaxTotal = BigDecimal.ZERO;

    return zdsTotalInTaxTotal;
  }

  @Override
  public Long getWooTotalInTaxTotal(Long saleDashbordId, Long locationId, Long companyId)
      throws AxelorException {
    BigDecimal zdsTotalInTaxTotal = BigDecimal.ZERO;
    SaleDashboard saleDashboard = Beans.get(SaleDashboardRepository.class).find(saleDashbordId);
    if (saleDashboard.getCustomer() != null) {
      return saleDashboard.getCustomer().getId();
    }
    return new Long(0);
  }
}
