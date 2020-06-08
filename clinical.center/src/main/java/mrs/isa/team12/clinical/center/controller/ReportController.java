package mrs.isa.team12.clinical.center.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mrs.isa.team12.clinical.center.service.interfaces.ReportService;

@RestController
@RequestMapping("theGoodShepherd/report")
public class ReportController {

	private ReportService reportService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}
	
	
}
