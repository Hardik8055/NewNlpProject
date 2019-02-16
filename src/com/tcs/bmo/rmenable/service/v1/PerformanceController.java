package com.tcs.bmo.rmenable.service.v1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.bmo.rmenable.domain.Category;

@RequestMapping(value = "api/v1")
@RestController
public class PerformanceController {

	@RequestMapping(method = RequestMethod.GET, value = "/revenue")
	public ResponseEntity<List<Category>> getMinimumBalanceOverPeriod() {

		List<Category> catList = new ArrayList<Category>();
		Category dateCat1 = new Category();
		dateCat1.setCategory("AutoNation");
		dateCat1.setMeasure(0.30);
		catList.add(dateCat1);
		
		Category dateCat2 = new Category();
		dateCat2.setCategory("CarMax");
		dateCat2.setMeasure(0.25);
		catList.add(dateCat2);
		
		Category dateCat3 = new Category();
		dateCat3.setCategory("Penske Automotive Group");
		dateCat3.setMeasure(0.15);
		catList.add(dateCat3);
		
		Category dateCat4 = new Category();
		dateCat4.setCategory("Sonic Automotive");
		dateCat4.setMeasure(0.05);
		catList.add(dateCat4);
		
		Category dateCat5 = new Category();
		dateCat5.setCategory("Inchcape plc");
		dateCat5.setMeasure(0.18);
		catList.add(dateCat5);
		
		Category dateCat6 = new Category();
		dateCat6.setCategory("Porsche Piech");
		dateCat6.setMeasure(0.04);
		catList.add(dateCat6);
		
		Category dateCat7 = new Category();
		dateCat7.setCategory("Group 1");
		dateCat7.setMeasure(0.03);
		catList.add(dateCat7);
		
		return new ResponseEntity<List<Category>>(catList, HttpStatus.OK);
	}

}
