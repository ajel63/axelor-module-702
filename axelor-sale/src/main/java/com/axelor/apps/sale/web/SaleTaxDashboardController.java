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
package com.axelor.apps.sale.web;

import com.axelor.apps.base.service.exception.TraceBackService;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.apps.sale.db.SaleTaxDashboard;
import com.axelor.apps.sale.db.repo.SaleOrderRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.CallMethod;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Singleton;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SaleTaxDashboardController {

  public void setDashbordName(ActionRequest request, ActionResponse response) {
    SaleTaxDashboard saleTaxDashboard = request.getContext().asType(SaleTaxDashboard.class);
    response.setValue("name", LocalDate.now().toString());
    response.setValue("$totalSaleTax", 0.00);
  }

  public void setDateRange(ActionRequest request, ActionResponse response) {
    SaleTaxDashboard saleTaxDashboard = request.getContext().asType(SaleTaxDashboard.class);

    if (saleTaxDashboard.getDateSelection() == 1) {
      LocalDate currentDate = LocalDate.now();
      LocalDate startDate = currentDate.minusMonths(1).withDayOfMonth(1);
      LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
      response.setValue("startDate", startDate);
      response.setValue("endDate", endDate);
    }

    if (saleTaxDashboard.getDateSelection() == 2) {
      LocalDate currentDate = LocalDate.now();
      int currentQuarter = getQuarterOfYear(currentDate);
      int lastQuarter = (currentQuarter == 1) ? 4 : currentQuarter - 1;

      LocalDate startDate = null;
      LocalDate endDate = null;

      // Determine the start and end dates based on the last quarter
      switch (lastQuarter) {
        case 1: // Q1: Jan 1 - Mar 31
          startDate = LocalDate.of(currentDate.getYear(), Month.JANUARY, 1);
          endDate = LocalDate.of(currentDate.getYear(), Month.MARCH, 31);
          break;
        case 2: // Q2: Apr 1 - Jun 30
          startDate = LocalDate.of(currentDate.getYear(), Month.APRIL, 1);
          endDate = LocalDate.of(currentDate.getYear(), Month.JUNE, 30);
          break;
        case 3: // Q3: Jul 1 - Sep 30
          startDate = LocalDate.of(currentDate.getYear(), Month.JULY, 1);
          endDate = LocalDate.of(currentDate.getYear(), Month.SEPTEMBER, 30);
          break;
        case 4: // Q4: Oct 1 - Dec 31
          startDate = LocalDate.of(currentDate.getYear(), Month.OCTOBER, 1);
          endDate = LocalDate.of(currentDate.getYear(), Month.DECEMBER, 31);
          break;
        default:
          throw new IllegalArgumentException("Invalid quarter number.");
      }

      response.setValue("startDate", startDate);
      response.setValue("endDate", endDate);
    }

    if (saleTaxDashboard.getDateSelection() == 3) {
      LocalDate currentDate = LocalDate.now();
      int lastYear = currentDate.getYear() - 1;
      LocalDate startDate = LocalDate.of(lastYear, 1, 1);
      LocalDate endDate = LocalDate.of(lastYear, 12, 31);
      response.setValue("startDate", startDate);
      response.setValue("endDate", endDate);
    }

    if (saleTaxDashboard.getDateSelection() == 4) {
      LocalDate startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
      LocalDate today = LocalDate.now();
      response.setValue("startDate", startOfYear);
      response.setValue("endDate", today);
    }

    if (saleTaxDashboard.getDateSelection() == 5) {
      response.setValue("startDate", null);
      response.setValue("endDate", null);
    }
  }

  public static int getQuarterOfYear(LocalDate date) {
    int month = date.getMonthValue();
    if (month >= 1 && month <= 3) {
      return 1; // Q1
    } else if (month >= 4 && month <= 6) {
      return 2; // Q2
    } else if (month >= 7 && month <= 9) {
      return 3; // Q3
    } else {
      return 4; // Q4
    }
  }

  @CallMethod
  public void calculateTotalSaleTax(ActionRequest request, ActionResponse response) {

    try {
      SaleTaxDashboard saleTaxDashboard = request.getContext().asType(SaleTaxDashboard.class);
      BigDecimal totalSaleTax = BigDecimal.ZERO;

      if (saleTaxDashboard.getStartDate() == null || saleTaxDashboard.getEndDate() == null) {
        response.setValue("$totalSaleTax", "0.00");
        return;
      }

      List<SaleOrder> saleOrders = new ArrayList<SaleOrder>();

      if (saleTaxDashboard.getStatusSelect() <= 0) {
        saleOrders =
            Beans.get(SaleOrderRepository.class)
                .all()
                .filter(
                    "self.creationDate >= ? AND self.creationDate <= ?",
                    saleTaxDashboard.getStartDate(),
                    saleTaxDashboard.getEndDate())
                .fetch();
      } else {
        saleOrders =
            Beans.get(SaleOrderRepository.class)
                .all()
                .filter(
                    "self.creationDate >= ? AND self.creationDate <= ? AND self.statusSelect = ?",
                    saleTaxDashboard.getStartDate(),
                    saleTaxDashboard.getEndDate(),
                    saleTaxDashboard.getStatusSelect())
                .fetch();
      }

      for (SaleOrder saleOrder : saleOrders) {
        totalSaleTax = totalSaleTax.add(saleOrder.getTaxTotal());
      }

      response.setValue("$totalSaleTax", totalSaleTax);

    } catch (Exception e) {
      TraceBackService.trace(response, e);
    }
  }
}
