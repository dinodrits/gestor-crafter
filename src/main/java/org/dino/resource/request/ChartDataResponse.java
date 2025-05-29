package org.dino.resource.request;

import java.math.BigDecimal;
import java.util.List;

public class ChartDataResponse {
	private List<String> labels;
    private List<BigDecimal> data;
    
    // Construtor
    public ChartDataResponse(List<String> labels, List<BigDecimal> data) {
        this.labels = labels;
        this.data = data;
    }

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<BigDecimal> getData() {
		return data;
	}

	public void setData(List<BigDecimal> data) {
		this.data = data;
	}
    

}
