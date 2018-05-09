package org.coop.api.controllers;

import org.coop.api.model.loan.Quotation;
import org.coop.api.services.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coop/api/quotation")
public class QuotationController {

	@Autowired
	private QuotationService quotationService;

	@PostMapping("/loan/compute")
	@ResponseStatus(HttpStatus.OK)
	public Quotation compute(@RequestBody Quotation quotation) {
		return quotationService.compute(quotation);
	}
}
