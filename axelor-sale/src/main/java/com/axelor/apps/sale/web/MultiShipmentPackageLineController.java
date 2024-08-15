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

import com.axelor.apps.base.AxelorException;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.repo.PartnerRepository;
import com.axelor.apps.sale.db.MultiShipmentPackageLine;
import com.axelor.apps.sale.db.PurchaseLabel;
import com.axelor.apps.sale.db.PurchaseLabelRateLine;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.apps.sale.db.ShipmentLine;
import com.axelor.apps.sale.db.ShippService;
import com.axelor.apps.sale.db.repo.PurchaseLabelRepository;
import com.axelor.apps.sale.db.repo.SaleOrderRepository;
import com.axelor.apps.sale.service.PurchaseLableService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import com.google.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Singleton
public class MultiShipmentPackageLineController {

  public void prtinLable(ActionRequest request, ActionResponse response)
      throws AxelorException {
    Context context = request.getContext();
    MultiShipmentPackageLine multiShipmentPackageLine = request.getContext().asType(MultiShipmentPackageLine.class);
    
  }
}
